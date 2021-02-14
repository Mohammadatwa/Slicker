package com.example.slickr.api

import com.example.slickr.api.model.SearchModel
import com.example.slickr.util.Constants.Companion.FLICKR_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIRequests {
    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY")
    suspend fun getSearchModel (@Query(value = "text") searchTerm: String): Response<SearchModel>
}