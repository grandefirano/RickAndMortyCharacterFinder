package com.grandefirano.rickandmortycharacterfinder.details

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.data.Search.GenderOption

@BindingAdapter("genderIcon")
fun ImageView.setGenderImage(item:Character){
    item?.let {
        val icon=when(item.gender){
            GenderOption.MALE.value-> R.drawable.ic_male
            GenderOption.FEMALE.value->R.drawable.ic_woman
            GenderOption.GENDERLESS.value->R.drawable.ic_stop
                else->R.drawable.ic_question
        }
        Glide.with(rootView.context).load(icon).into(this)
    }
}