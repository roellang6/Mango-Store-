package dev.ran.MangoStore.Model

data class DataGet(

    val artworkUrl100: String,

    val trackName: String,
    val artistName: String,
    val primaryGenreName: String,

    val trackPrice: Double,
    val currency: String,

    val shortDescription: String,
    val longDescription: String,

    val releaseDate: String

)