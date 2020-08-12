package com.grandefirano.rickandmortycharacterfinder.list

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.grandefirano.rickandmortycharacterfinder.data.Character

@BindingAdapter("characterImage")
fun ImageView.setCharacterImage(item:Character){
    item?.let {
        Glide.with(rootView.context).load(item.imageUrl).into(this)
    }
}