package com.example.slickr.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slickr.api.RetrofitInstance
import com.example.slickr.api.model.SearchModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val myResponse: MutableLiveData<Response<SearchModel>> = MutableLiveData()

    fun getSearchData(text : String){
        viewModelScope.launch {
            val response = getSearchModel(text)
            myResponse.value = response

            val handler = CoroutineExceptionHandler { _, exception ->
                Log.d("Network", "Caught $exception")
            }
        }

    }
    suspend fun getSearchModel(text : String): Response<SearchModel> {
        return RetrofitInstance.client.getSearchModel(text)
    }
}