package com.grandefirano.rickandmortycharacterfinder.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {



    private val viewModel: DetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding=FragmentDetailsBinding.inflate(inflater,container,false)

        binding.lifecycleOwner=viewLifecycleOwner
        val args:DetailsFragmentArgs by navArgs()


        binding.character=args.character


        return binding.root
    }


}