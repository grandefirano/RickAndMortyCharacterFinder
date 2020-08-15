package com.grandefirano.rickandmortycharacterfinder.details

import android.graphics.Color
import android.media.Image
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.grandefirano.rickandmortycharacterfinder.GlideApp
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.data.Search.GenderOption
import com.grandefirano.rickandmortycharacterfinder.data.Search.StatusOption


@BindingAdapter("profilePhoto")
fun ImageView.setProfilePhoto(item: Character){


    val circularProgressDrawable = CircularProgressDrawable(rootView.context)
    circularProgressDrawable.strokeWidth = 30f
    circularProgressDrawable.centerRadius = 200f
    circularProgressDrawable.setColorSchemeColors(Color.parseColor("#82CD08"))
    circularProgressDrawable.start()

    GlideApp.with(rootView.context)
        .load(item.imageUrl)
        .placeholder(circularProgressDrawable)
        .into(this)
}

@BindingAdapter("genderIcon")
fun ImageView.setGenderImage(item:Character){

    Log.d("TAG", "setGenderImage: ${item.gender}")

        val icon=when(item.gender){
            GenderOption.MALE.value-> R.drawable.ic_male
            GenderOption.FEMALE.value->R.drawable.ic_woman
            GenderOption.GENDERLESS.value->R.drawable.ic_stop
                else->R.drawable.ic_question
        }
        Glide.with(rootView.context).load(icon).into(this)

}

@BindingAdapter("statusIcon")
fun ImageView.setStatusImage(item:Character){

    Log.d("TAG", "setGenderImage: ${item.status}")
    val icon=when(item.status){
        StatusOption.ALIVE.value->R.drawable.ic_heart
        StatusOption.DEAD.value->R.drawable.ic_cementery
        else->R.drawable.ic_question
    }
    Glide.with(rootView.context).load(icon).into(this)
}