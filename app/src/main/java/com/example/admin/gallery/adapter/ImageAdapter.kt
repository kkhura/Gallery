package com.example.admin.gallery.quotesHome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.admin.gallery.R
import com.example.admin.gallery.model.ImageModel
import com.squareup.picasso.Picasso
import java.util.*


class ImageAdapter(val items: ArrayList<ImageModel>?, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val get = items!!.get(position)

        if (get.userImageURL != null) {
            Picasso.with(context)
                    .load(get.userImageURL)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imageView)
        } else {
            holder.imageView.setBackgroundResource(R.drawable.ic_launcher_background)
        }

        holder.imageView.setOnClickListener(View.OnClickListener { Toast.makeText(context, "Save Image" + get.tags, Toast.LENGTH_SHORT).show() })
    }
}

class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.imageView)
//    val imgSave: ImageView = view.findViewById(R.id.imgSave)
}