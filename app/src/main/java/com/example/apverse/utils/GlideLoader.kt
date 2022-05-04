package com.example.apverse.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.apverse.R
import java.io.IOException

class GlideLoader (val context: Context) {
    fun loadUserProfile(image:Any, imageView: ImageView){
        try{
            Glide.with(context)
                .load(image)
                .placeholder(R.drawable.profile_user)
                .into(imageView)
        }catch(e: IOException){
            e.printStackTrace()
        }
    }
}