package com.mywebsite.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        // Choose authentication providers
        Log.d("testLogs","RegistrationActivity start registration")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = GridLayoutManager(this, 2)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(ItemsViewModel(R.drawable.common_full_open_on_phone, "Item " + i))
        }



        val apiInterface = ApiInterface.create().getMovies("84111371c9a2163f6f3fb1a7811b44fb")

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue( object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>?, response: Response<Movies>?) {
                Log.d("testLogs", "OnResponse Success ${call.toString()} ${response?.body()?.results?.size}")
                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(response?.body()?.results)

                // Setting the Adapter with the recyclerview
                recyclerview.adapter = adapter
            }

            override fun onFailure(call: Call<Movies>?, t: Throwable?) {
                Log.d("testLogs", "onFailure : ${t?.message}")
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
        Log.d("testLogs","RegistrationActivity Application been closed")
    }
}