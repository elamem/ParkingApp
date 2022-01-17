package com.elamparithi.parkypark.data.remote.response

import com.elamparithi.parkypark.data.model.Routes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionResponse(
    @Expose
    @SerializedName("routes")
    val routes: List<Routes>
)
