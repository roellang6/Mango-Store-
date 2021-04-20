package dev.ran.MangoStore.Api
import dev.ran.MangoStore.Model.Datalist
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MangoServices {
    private val baseurl = "https://itunes.apple.com/"
    private val mangoApi:API

    init {
        mangoApi = Retrofit.Builder().baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create()).build().create(API::class.java)
    }
    //return data get to Model
    fun getMango(): Call<Datalist>{return mangoApi.getMango()}
}