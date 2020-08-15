package com.grandefirano.rickandmortycharacterfinder.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.grandefirano.rickandmortycharacterfinder.*
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.databinding.FragmentListOfCharactersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ListOfCharactersFragment : Fragment() {

    val viewModel: ListOfCharactersViewModel by viewModels()

    private val TAG = "ListOfCharactersFragmen"

    private var searchJob: Job? = null

    private val searchRequest: LiveData<Search> by lazy {
        viewModel.searchRequest
    }

    private val adapter by lazy {
        CharactersListAdapter(CharacterClickListener { character ->
            viewModel.onCharacterClicked(character)
        })
    }

    private lateinit var binding: FragmentListOfCharactersBinding

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

        (activity as AppCompatActivity).supportActionBar?.show()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list_of_characters,
            container,
            false
        )

        initGridLayout()
        initGenderSpinner()
        initStatusSpinner()
        initSearch()
        observeNavigationState()
        initScaleGesture()


        return binding.root
    }

    private fun initGridLayout(){

        binding.lifecycleOwner = this

        itemPerRow.observe(viewLifecycleOwner, Observer { itemPerRow ->
            Log.d(TAG, "onCreateView: itemprerov $itemPerRow")
            val manager = GridLayoutManager(context, itemPerRow).apply {
                spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if(position>=adapter.itemCount){
                            itemPerRow
                        } else{
                            1
                        }
                    }

                }
            }
            binding.characterList.adapter=adapter.run {
                withLoadStateFooter(
                    footer = CharactersLoadStateAdapter { adapter.retry() }
                )
            }
            adapter.apply {
                addLoadStateListener { loadState ->
                    binding.characterList.isVisible =
                        loadState.source.refresh is LoadState.NotLoading
                    binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorState?.let {
                        Toast.makeText(context, "$errorState", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.characterList.layoutManager = manager
        })
    }

    private fun initSearch() {
        searchRequest.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreateView: ${it.name} ${it.gender} ${it.status}")
            binding.characterList.scrollToPosition(0)
            search(it)
            Log.d(TAG, "onCreateView: searchRequestChanged")
        })

    }



    private fun observeNavigationState() {
        viewModel.navigateToCharacterDetail.observe(viewLifecycleOwner, Observer { character ->
            character?.let {
                this.findNavController().navigate(
                    ListOfCharactersFragmentDirections
                        .actionListOfCharactersFragmentToDetailsFragment(it)
                )
                viewModel.onCharacterDetailNavigated()
            }
        })

    }


    private fun initScaleGesture() {
        val scaleDetector = ScaleGestureDetector(this.context, PinchListener())
        binding.characterList.setOnTouchListener { view, motionEvent ->
            scaleDetector.onTouchEvent(motionEvent)
            false
        }
    }


    private fun initGenderSpinner() {
        val titlesOfGenderSpinner = resources.getStringArray(R.array.gender_list_name)
        val valuesOfGenderSpinner = resources.getStringArray(R.array.gender_list_value)

        val adapterSpinner: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_checked_spinner,
            titlesOfGenderSpinner
        )
        adapterSpinner.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerGender.adapter = adapterSpinner
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onQueryGenderChanged(Search.GenderOption.valueOf(valuesOfGenderSpinner[position]))
            }

        }
    }

    private fun initStatusSpinner() {
        val titlesOfStatusSpinner = resources.getStringArray(R.array.status_list_name)
        val valuesOfStatusSpinner = resources.getStringArray(R.array.status_list_value)

        val adapterSpinner: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_checked_spinner,
            titlesOfStatusSpinner
        )
        adapterSpinner.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerStatus.adapter = adapterSpinner
        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onQueryStatusChanged(Search.StatusOption.valueOf(valuesOfStatusSpinner[position]))
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

