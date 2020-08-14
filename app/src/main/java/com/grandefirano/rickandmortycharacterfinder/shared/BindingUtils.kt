package com.grandefirano.rickandmortycharacterfinder.shared

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.grandefirano.rickandmortycharacterfinder.data.Character

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url:String){
        Glide.with(rootView.context).load(url).into(this)
}