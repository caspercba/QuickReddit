package com.gaspardeelias.quickreddit.core.repository.toplisting.model

import android.os.Parcelable
import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopListingElement(
    override val id: String,
    val author: String = "",
    val title: String? = null,
    val createdUtc: Long? = null,
    val thumbnail: String? = null,
    var viewed: Boolean = false
) : WithEntityId, Parcelable