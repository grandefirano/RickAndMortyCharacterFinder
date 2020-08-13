package com.grandefirano.rickandmortycharacterfinder.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

    private var searchJob: Job?=null

    val searchRequest:MutableLiveData<Search> =MutableLiveData()

    private val adapter by lazy {
        CharactersListAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding=DataBindingUtil.inflate<FragmentListOfCharactersBinding>(
            inflater,
            R.layout.fragment_list_of_characters,
            container,
            false
        )



        val manager=GridLayoutManager(context,3)
        binding.characterList.adapter=adapter
        binding.characterList.layoutManager=manager

        binding.lifecycleOwner=this



        val searchRes=Search()
        searchRes.name="rick"
        searchRes.gender=Search.GenderOption.FEMALE

        searchRequest.value=searchRes

        searchRequest.observe(viewLifecycleOwner, Observer {
            search(searchRes)
        })


        return binding.root
    }



    private fun search(query: Search){
        searchJob?.cancel()
        searchJob=lifecycleScope.launch {
            viewModel.searchCharacters(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }


}