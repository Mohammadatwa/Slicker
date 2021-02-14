package com.example.slickr.ui.utilities

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.slickr.R
import com.example.slickr.model.ItemModel
import com.squareup.picasso.Picasso

class ImageAdapter(var context: Context?, var arrayList: ArrayList<ItemModel>) : BaseAdapter (){

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View = View.inflate(context,R.layout.grid_item, null)
        var images:ImageView = view.findViewById(R.id.image_item)
        var itemModel:ItemModel = arrayList.get(position)

        Picasso.get().load(itemModel.url).into(images)
//        view.setOnClickListener(View.OnClickListener {
//            Toast.makeText(context, position,Toast.LENGTH_LONG).show()
//        })

        return view
    }

}