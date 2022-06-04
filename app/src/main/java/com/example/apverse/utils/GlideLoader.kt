package com.example.apverse.utils

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.R
import java.io.IOException

class GlideLoader (val fragment: Fragment) {
    fun loadImage(image:Any, imageView: ImageView, size: Int){
        try{
            Glide.with(fragment)
                .load(image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.unknown)
                    .override(size, size))
                .into(imageView)
        }catch(e: IOException){
            e.printStackTrace()
        }
    }
}