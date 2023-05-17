package com.example.fooddeliveryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fooddeliveryapp.adapter.MenuListAdapter
import com.example.fooddeliveryapp.models.Menus
import com.example.fooddeliveryapp.models.RestaurentModel
import androidx.recyclerview.widget.RecyclerView


class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener {
    private lateinit var menuRecyclerView: RecyclerView
    lateinit var btnCheckout: TextView
    lateinit var menuRecyclerVuew:RecyclerView

    private var itemsInTheCartList:MutableList<Menus?>? = null
    private var totalItemInCartCount = 0
    private var menuList: List<Menus?>? = null
    private var menuListAdapter:MenuListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)
        btnCheckout = findViewById(R.id.checkoutButton)
        menuRecyclerView = findViewById(R.id.menuRecycleVuew)

        val restaurentModel = intent?.getParcelableExtra<RestaurentModel>("RestaurentModel")

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurentModel?.name)
        actionBar?.setSubtitle(restaurentModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList = restaurentModel?.menus


        initRecyclerView(menuList)
        btnCheckout.setOnClickListener {
            if (itemsInTheCartList != null && itemsInTheCartList!!.size <= 0) {
                Toast.makeText(this@RestaurantMenuActivity, "Please add some items in the cart", Toast.LENGTH_LONG).show()
            }else{
                restaurentModel?.menus = itemsInTheCartList
                val intent = Intent(this@RestaurantMenuActivity, PlaceYourOrderActivity::class.java)
                intent.putExtra("RestaurentModel", restaurentModel)
                startActivityForResult(intent,1000)
            }
        }


    }
    private fun initRecyclerView(menus: List<Menus?>?) {
        menuRecyclerView.layoutManager = GridLayoutManager(this, 2)
        menuListAdapter = MenuListAdapter(menus, this, data = ArrayList())
//        menuRecyclerView.adapter = menuListAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }


    override fun addToCartClickListener(menu: Menus) {
        if (itemsInTheCartList == null){
            itemsInTheCartList = ArrayList()
        }
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!){
            totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
        }
        btnCheckout.text = "Checkout(" + totalItemInCartCount + ") Items"

    }

    override fun updateCartClickListener(menu: Menus) {
        val index = itemsInTheCartList!!.indexOf(menu)
        itemsInTheCartList?.removeAt(index)
        totalItemInCartCount += menu.totalInCart
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!){
            totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
        }
        btnCheckout.text = "Checkout(" + totalItemInCartCount + ") Items"
    }

    override fun removeFromCartClickListener(menu: Menus) {
        if (itemsInTheCartList!!.contains(menu)){
                itemsInTheCartList?.remove(menu)
                totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!){
            totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
        }
        btnCheckout.text = "Checkout(" + totalItemInCartCount + ") Items"
    }}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            finish()
        }
    }
}
