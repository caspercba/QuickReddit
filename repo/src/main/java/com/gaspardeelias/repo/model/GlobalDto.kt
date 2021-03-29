package com.gaspardeelias.repo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

open class GlobalDto<T>(val kind: String, val data: T)

class PostListing(kind: String, data: ListingDto<Post>) :
    GlobalDto<ListingDto<Post>>(kind, data)

data class ListingDto<T>(
    val modhash: String? = null,
    val dist: Int? = null,
    val children: List<ListingItemDto<T>> = arrayListOf(),
    val after: String? = null,
    val before: String? = null
)

data class ListingItemDto<T>(val kind: String, val data: T)

@Parcelize
open class Post(
    var id: String? = null,
    val author: String? = null,
    val title: String? = null,
    val createdUtc: Long? = null,
    val thumbnail: String? = null,
    val numComments: Long = 0
) : Parcelable