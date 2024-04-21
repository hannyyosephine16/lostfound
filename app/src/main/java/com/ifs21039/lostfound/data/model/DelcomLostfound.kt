package com.ifs21039.lostfound.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class DelcomLostfound(
    val id: Int,
    val title: String,
    val description: String,
    var isFinished: Boolean,
    val cover: String?,
) : Parcelable
