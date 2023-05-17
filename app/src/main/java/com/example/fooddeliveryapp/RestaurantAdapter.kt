package com.example.fooddeliveryapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class RestaurantAdapter(var context: Context, var data:ArrayList<Restaurant>):BaseAdapter() {
    private class ViewHolder(row:View?){
        var restaurantName:TextView
        var restaurantAddress:TextView
        var restaurantHours:TextView
        var tvAdMenu:TextView
        var thumbImage:ImageView
        init {
            this.restaurantName = row?.findViewById(R.id.tvRestaurantName) as TextView
            this.restaurantAddress = row?.findViewById(R.id.tvRestaurantAddress) as TextView
            this.restaurantHours = row?.findViewById(R.id.tvRestaurantHours) as TextView
            this.tvAdMenu = row?.findViewById(R.id.tvAdMenu) as TextView
            this.thumbImage = row?.findViewById(R.id.thumbImage) as ImageView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.recycler_restaurant_list_row,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:Restaurant = getItem(position) as Restaurant
        viewHolder.restaurantName.text = item.name
        viewHolder.restaurantAddress.text = item.address
        viewHolder.restaurantHours.text = item.hours
        Glide.with(context).load(item.image).into(viewHolder.thumbImage)
        viewHolder.tvAdMenu.setOnClickListener {
            var intent = Intent(context,AddmenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("id",item.id)
            context.startActivity(intent)
        }
        return view as View
    }

    override fun getItem(position: Int): Any {
        return  data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
}