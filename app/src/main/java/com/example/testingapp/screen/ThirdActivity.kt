package com.example.testingapp.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.testingapp.R
import com.example.testingapp.adapter.UserAdapter
import com.example.testingapp.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdActivity : AppCompatActivity() {

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private lateinit var rv: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://reqres.in/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        rv = findViewById(R.id.rvUsers)
        rv.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(emptyList())
        rv.adapter = userAdapter
        val btnBack = findViewById<ImageView>(R.id.imgBackThird)
        btnBack.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadNextPage()
                    }
                }
            }
        })
        fetchData()
    }

    private fun loadNextPage() {
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getUsers(currentPage)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val newData = response.body()?.data ?: emptyList()
                        if (newData.isNotEmpty()) {
                            userAdapter.updateData(userAdapter.currentData + newData)
                            currentPage++
                        } else {
                            isLastPage = true
                        }
                    } else {
                        Toast.makeText(this@ThirdActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ThirdActivity, "Error loading next page", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                    isLoading = false
                }
            }
        }
    }

    private fun refreshData() {
        currentPage = 1
        isLastPage = false

        fetchData()
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getUsers(currentPage)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        userAdapter.updateData(apiResponse?.data ?: emptyList())
                    } else {
                        Toast.makeText(this@ThirdActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                    findViewById<SwipeRefreshLayout>(R.id.swipeLayout).isRefreshing = false
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ThirdActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                    findViewById<SwipeRefreshLayout>(R.id.swipeLayout).isRefreshing = false
                    isLoading = false
                }
            }
        }
    }
}