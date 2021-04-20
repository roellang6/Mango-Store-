package dev.ran.MangoStore.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ran.MangoStore.Api.MangoServices
import dev.ran.MangoStore.Model.DataGet
import dev.ran.MangoStore.Model.Datalist
import retrofit2.Call
import retrofit2.Response

class IndexViewModel : ViewModel() {
    private val  mangoServices = MangoServices()

    val indexDataGet = MutableLiveData<List<DataGet>>()
    val indexDataLive: LiveData<List<DataGet>> get() = indexDataGet
    val indexLoadError = MutableLiveData<Boolean>()

    //Indirect Call fetch Data
    fun refresh(){
        getData()
    }

    //Fecth Data from Model
    private fun getData(){
        mangoServices.getMango().enqueue(object : retrofit2.Callback<Datalist>{
            override fun onResponse(call: Call<Datalist>, response: Response<Datalist>) {
                indexDataGet.value = response.body()!!.results
                indexLoadError.value = false
            }
            override fun onFailure(call: Call<Datalist>, t: Throwable) {
                indexLoadError.value = true
            }
        })
    }
}