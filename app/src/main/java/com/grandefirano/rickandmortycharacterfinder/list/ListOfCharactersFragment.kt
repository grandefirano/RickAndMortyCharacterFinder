package com.grandefirano.rickandmortycharacterfinder.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_list_of_characters.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListOfCharactersFragment : Fragment() {

    val viewModel: ListOfCharactersViewModel by viewModels()

    private val TAG = "ListOfCharactersFragmen"

    private var searchJob: Job? = null

    val searchRequest: MutableLiveData<Search> = MutableLiveData()

    private val adapter by lazy {
        CharactersListAdapter()
    }

    val itemPerRow:MutableLiveData<Int> = MutableLiveData(3)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentListOfCharactersBinding>(
            inflater,
            R.layout.fragment_list_of_characters,
            container,
            false
        )


        val manager = GridLayoutManager(context, 3)
        binding.characterList.adapter = adapter
        binding.characterList.layoutManager = manager

        binding.lifecycleOwner = this


        val searchRes = Search()
        searchRes.name = "rick"
        searchRes.gender = Search.GenderOption.MALE

        searchRequest.value = searchRes

        searchRequest.observe(viewLifecycleOwner, Observer {
            search(it)
        })

        itemPerRow.observe(viewLifecycleOwner, Observer {itemPerRow->
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
                        if(itemPerRow.value!! >1) itemPerRow.value=itemPerRow.value?.minus(1)
                        Log.d("TAG", "onScale: zoom in ${itemPerRow.value}")
                    }
                    detector.scaleFactor < 1 -> {
                        if(itemPerRow.value!!<5)itemPerRow.value=itemPerRow.value?.plus(1)
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

