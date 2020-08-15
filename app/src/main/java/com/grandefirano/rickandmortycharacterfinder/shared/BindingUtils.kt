package com.grandefirano.rickandmortycharacterfinder.shared

import android.graphics.Color
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.grandefirano.rickandmortycharacterfinder.GlideApp
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.data.Character
import javax.inject.Inject


@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url:String){

    GlideApp.with(rootView.context)
        .load(url)
        .into(this)
}
