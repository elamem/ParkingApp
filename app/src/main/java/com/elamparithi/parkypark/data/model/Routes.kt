package com.elamparithi.parkypark.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Routes (
        @Expose
        @SerializedName("legs")
        val legsList: List<Legs>
        )