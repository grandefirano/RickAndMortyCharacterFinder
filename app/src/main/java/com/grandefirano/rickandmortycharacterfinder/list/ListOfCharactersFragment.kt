package com.grandefirano.rickandmortycharacterfinder.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grandefirano.rickandmortycharacterfinder.R

class ListOfCharactersFragment : Fragment() {

    private lateinit var viewModel: ListOfCharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_of_characters_fragment, container, false)
    }

}