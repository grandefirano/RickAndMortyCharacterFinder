package com.grandefirano.rickandmortycharacterfinder.shared

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.grandefirano.rickandmortycharacterfinder.GlideApp


@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url:String){

    GlideApp.with(rootView.context)
        .load(url)
        .into(this)
}
