package com.elamparithi.parkypark.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Legs(
    @Expose
    @SerializedName("distance")
    val distance: Distance
)