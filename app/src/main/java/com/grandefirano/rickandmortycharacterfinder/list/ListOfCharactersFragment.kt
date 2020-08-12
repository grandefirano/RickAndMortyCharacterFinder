package com.grandefirano.rickandmortycharacterfinder.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.grandefirano.rickandmortycharacterfinder.ApiNetwork
import com.grandefirano.rickandmortycharacterfinder.ApiService
import com.grandefirano.rickandmortycharacterfinder.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListOfCharactersFragment : Fragment() {

    private lateinit var viewModel: ListOfCharactersViewModel

    private val TAG = "ListOfCharactersFragmen"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.list_of_characters_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            Log.d(TAG, ApiNetwork.api.getCharacters().await().toString())
        }
    }

}