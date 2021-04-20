package dev.ran.MangoStore.Api
import dev.ran.MangoStore.Model.*
import retrofit2.http.GET

interface API {
    @GET("search?term=star&amp;country=au&amp;media=movie&amp;all")
    fun getMango(): retrofit2.Call<Datalist>
}