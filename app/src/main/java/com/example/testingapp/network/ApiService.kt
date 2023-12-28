package com.example.testingapp.network

import android.graphics.pdf.PdfDocument.Page
import com.example.testingapp.model.ApiResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int, @Query("per_page") perPage: Int = 10): Response<ApiResponse>
}
