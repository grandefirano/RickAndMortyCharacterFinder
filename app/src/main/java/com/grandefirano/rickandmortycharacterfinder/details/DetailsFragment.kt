package com.grandefirano.rickandmortycharacterfinder.details

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.databinding.FragmentDetailsBeforeBinding
import kotlinx.android.synthetic.main.fragment_details_before.*


class DetailsFragment : Fragment() {



    private val viewModel: DetailsViewModel by viewModels()

private lateinit var binding:FragmentDetailsBeforeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentDetailsBeforeBinding.inflate(inflater,container,false)

        binding.lifecycleOwner=viewLifecycleOwner
        val args:DetailsFragmentArgs by navArgs()


        binding.character=args.character



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAnimationOperations()
    }


    private fun addAnimationOperations(){
        var set=false
        val constraint1=ConstraintSet()
        constraint1.clone(root)
        val constraint2=ConstraintSet()
        constraint2.clone(context,R.layout.fragment_details_after)

        binding.portalImageView.setOnClickListener {
            Log.d("TAG", "addAnimationOperations:CLICK ${Build.VERSION.SDK_INT}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                TransitionManager.beginDelayedTransition(root)
                if(!set) {
                    constraint2.applyTo(root)
                    set = !set
                }
            }
        }
        binding.photoDetailsImageView.setOnClickListener {
            Log.d("TAG", "addAnimationOperations:CLICK ${Build.VERSION.SDK_INT}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(root)
                if(set) {
                    constraint1.applyTo(root)
                    set = !set
                }
            }
        }

    }


}