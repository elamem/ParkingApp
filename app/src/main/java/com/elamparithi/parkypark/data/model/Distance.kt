package com.elamparithi.parkypark.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Distance(
    @Expose
    @SerializedName("text")
    val text:String,
    @Expose
    @SerializedName("value")
    val value:Long
)
