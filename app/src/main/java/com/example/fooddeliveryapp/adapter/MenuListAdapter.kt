package com.example.fooddeliveryapp.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.models.Menus


class MenuListAdapter(var menuList: List<Menus?>?, val clickListener: MenuListClickListener, var data:ArrayList<Menus>):BaseAdapter() {

    private class ViewHolder(row:View?) {
        var thumbImage:ImageView
        val menuName: TextView
        val menuPrice: TextView
        val addToCartButton: TextView
        val addMoreLayout: LinearLayout
        val imageMinus:ImageView
        val imageAddOne: ImageView
        val tvCount: TextView
//        val clickListener:MenuListClickListener

        init {
            this.thumbImage = row?.findViewById(R.id.thumbImage) as ImageView
            this.menuName = row?.findViewById(R.id.menuName) as TextView
            this.menuPrice = row?.findViewById(R.id.menuPrice) as TextView
            this.addToCartButton = row?.findViewById(R.id.addToCartButton) as TextView
            this.addMoreLayout = row?.findViewById(R.id.addMoreLayout) as LinearLayout
            this.imageMinus = row?.findViewById(R.id.imageMinus) as ImageView
            this.imageAddOne = row?.findViewById(R.id.imageAddOne) as ImageView
            this.tvCount = row?.findViewById(R.id.tvCount) as TextView
//            this.clickListener = clickListener

        }

        fun bind(menus: Menus) {
                menuName.text = menus?.name
                menuPrice.text = "Price: $ ${menus?.price}"

                addToCartButton.setOnClickListener {
                    menus?.totalInCart = 1
//                    clickListener.addToCartClickListener(menus)
                    addMoreLayout?.visibility = View.VISIBLE
                    addToCartButton.visibility = View.GONE
                    tvCount.text = menus?.totalInCart.toString()
                }
                imageMinus.setOnClickListener {
                    var total: Int = menus?.totalInCart!!
                    total--
                    if (total > 0) {
                        menus?.totalInCart = total
//                        clickListener.updateCartClickListener(menus)
                        tvCount.text = menus?.totalInCart.toString()
                    } else {
                        menus.totalInCart = total
//                        clickListener.removeFromCartClickListener(menus)
                        addMoreLayout.visibility = View.GONE
                        addToCartButton.visibility = View.VISIBLE
                    }
                }
                imageAddOne.setOnClickListener {
                    var total: Int = menus?.totalInCart!!
                    total++
                    if (total <= 10) {
                        menus.totalInCart = total
//                        clickListener.updateCartClickListener(menus)
                        tvCount.text = total.toString()
                    }
                }
            }
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(parent?.context)
            view = layout.inflate(R.layout.menu_list_row,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:Menus = getItem(position) as Menus
        viewHolder.bind(item)
        viewHolder.menuName.text = item.name
        viewHolder.menuPrice.text = item.price.toString()
        viewHolder.tvCount.text = item.totalInCart.toString()
        Glide.with(viewHolder.thumbImage)
            .load(item.url)
            .into(viewHolder.thumbImage)
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
    interface MenuListClickListener{
        fun addToCartClickListener(menu: Menus)
        fun updateCartClickListener(menu: Menus)
        fun removeFromCartClickListener(menu: Menus)
    }

}