package com.grandefirano.rickandmortycharacterfinder.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.grandefirano.rickandmortycharacterfinder.*
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.databinding.FragmentListOfCharactersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_of_characters.*
import kotlinx.android.synthetic.main.fragment_list_of_characters.view.*
import kotlinx.android.synthetic.main.layout_search.*
import kotlinx.android.synthetic.main.layout_search.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListOfCharactersFragment : Fragment() {

    val viewModel: ListOfCharactersViewModel by viewModels()

    private val TAG = "ListOfCharactersFragmen"

    private var searchJob: Job? = null

    val searchRequest: LiveData<Search> by lazy {
        viewModel.searchRequest
    }

    private val adapter by lazy {
        CharactersListAdapter()
    }

    lateinit var binding:FragmentListOfCharactersBinding

    val itemPerRow: MutableLiveData<Int> = MutableLiveData(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(TAG, "onQueryTextChange: query changed")
                viewModel.onQueryTextChanged(newText)
                return false
            }
        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = DataBindingUtil.inflate<FragmentListOfCharactersBinding>(
            inflater,
            R.layout.fragment_list_of_characters,
            container,
            false
        )




        val manager = GridLayoutManager(context, 3)
        binding.characterList.adapter = adapter
        binding.characterList.layoutManager = manager

        binding.lifecycleOwner = this


        initGenderSpinner()

        searchRequest.observe(viewLifecycleOwner, Observer {
            search(it)
            Log.d(TAG, "onCreateView: searchRequestChanged")
        })

        itemPerRow.observe(viewLifecycleOwner, Observer { itemPerRow ->
            Log.d(TAG, "onCreateView: itemprerov $itemPerRow")
            val manager = GridLayoutManager(context, itemPerRow)
            binding.characterList.adapter = adapter
            binding.characterList.layoutManager = manager
        })

        val scaleDetector = ScaleGestureDetector(this.context, PinchListener())
        binding.characterList.setOnTouchListener { view, motionEvent ->
            scaleDetector.onTouchEvent(motionEvent)
            false
        }





        return binding.root
    }


    private fun initGenderSpinner(){
        val titlesOfGenderSpinner=resources.getStringArray(R.array.gender_list_name)
        val valuesOfGenderSpinner=resources.getStringArray(R.array.gender_list_value)

        val adapterSpinner: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().applicationContext,
            R.layout.item_checked_spinner,
            titlesOfGenderSpinner
        )
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerGender.adapter=adapterSpinner
        binding.spinnerGender.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
               viewModel.onQueryGenderChanged(Search.GenderOption.valueOf(valuesOfGenderSpinner[position]))
            }

        }
    }


    private fun search(query: Search) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchCharacters(query).collectLatest {
                adapter.submitData(it)
            }
        }

    }


    inner class PinchListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            Log.d("TAG", "onScaleEnd:PINCH ")

            detector?.let {
                Log.d("TAG", "onScaleEnd: ${itemPerRow.value}  ${it.scaleFactor}")

                when {
                    detector.scaleFactor > 1 -> {
                        if (itemPerRow.value!! > 1) itemPerRow.value = itemPerRow.value?.minus(1)
                        Log.d("TAG", "onScale: zoom in ${itemPerRow.value}")
                    }
                    detector.scaleFactor < 1 -> {
                        if (itemPerRow.value!! < 5) itemPerRow.value = itemPerRow.value?.plus(1)
                        Log.d("TAG", "onScale: zoom out ${itemPerRow.value}")
                    }
                    else -> {
                        itemPerRow
                    }
                }
            }

        }
    }
}

