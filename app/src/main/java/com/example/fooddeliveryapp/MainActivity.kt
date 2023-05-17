package com.example.fooddeliveryapp

/*import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.adapter.RestaurantListAdapter
import com.example.fooddeliveryapp.models.RestaurentModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

class MainActivity : AppCompatActivity(), RestaurantListAdapter.RestaurantListClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Restaurant list"

        val restaurentModel = getRestaurentData()
        initRecyclerView(restaurentModel)
    }

    private fun initRecyclerView(restaurantList: List<RestaurentModel?>?){
        val recylerViewRestaurant = findViewById<RecyclerView>(R.id.recyclerViewRestaurant)
        recylerViewRestaurant.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList, this)
        recylerViewRestaurant.adapter = adapter
    }

    private fun getRestaurentData(): List<RestaurentModel> {
        val inputStream = resources.openRawResource(R.raw.restaurent)
        val writer: Writer =StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader: Reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n : Int
            while (reader.read(buffer).also { n = it } != -1){
                writer.write(buffer, 0 ,n)

            }
        }catch (e: Exception){}
        val jsonStr: String = writer.toString()
        val gson = Gson()
        val restaurentModel = gson.fromJson<Array<RestaurentModel>>(jsonStr,Array<RestaurentModel>::class.java).toList()
        return restaurentModel
    }

    override fun onItemClick(restaurentModel: RestaurentModel) {
        val intent = Intent(this@MainActivity, RestaurantMenuActivity::class.java)
        intent.putExtra("RestaurantModel", restaurentModel)
        startActivity(intent)
    }

}*/



import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var mListRestaurants:ListView
    lateinit var mBtnAddRestaurants:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mListRestaurants = findViewById(R.id.recyclerViewRestaurant)
        mBtnAddRestaurants = findViewById(R.id.mBtnAddRestaurants)
        var restaurants:ArrayList<Restaurant> = ArrayList()
        var myAdapter = RestaurantAdapter(applicationContext,restaurants)
        var progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")

        //Access the table in the database

        var my_db = FirebaseDatabase.getInstance().reference.child("Restaurants")
        //Start retrieving data
        progress.show()
        my_db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //Get the data and put it on the arraylist users
                restaurants.clear()
                for (snap in p0.children){
                    var restaurant = snap.getValue(Restaurant::class.java)
                    restaurants.add(restaurant!!)
                }
                //Notify the adapter that data has changed
                myAdapter.notifyDataSetChanged()
                progress.dismiss()
            }

            override fun onCancelled(p0: DatabaseError) {
                progress.dismiss()
                Toast.makeText(applicationContext,"DB Locked",Toast.LENGTH_LONG).show()
            }
        })

        mListRestaurants.adapter = myAdapter
        mListRestaurants.setOnItemClickListener { adapterView, view, i, l ->
            var intent = Intent(this,RestaurantMenuActivity::class.java)
            intent.putExtra("name",restaurants.get(i).name)
            intent.putExtra("address",restaurants.get(i).address)
            intent.putExtra("hours",restaurants.get(i).hours)
            intent.putExtra("id",restaurants.get(i).id)
            startActivity(intent)
        }


        mBtnAddRestaurants.setOnClickListener {
            startActivity(Intent(this,AddrestaurantActivity::class.java))
        }

    }
}
