package com.gaspardeelias.quickreddit.core.service.toplisting.dto

open class GlobalDto<T>(val kind: String, val data: T)

class TopListingDto(kind: String, data: ListingDto<TopListingElementDto>) :
    GlobalDto<ListingDto<TopListingElementDto>>(kind, data)

data class ListingDto<T>(
    val modhash: String? = null,
    val dist: Int? = null,
    val children: List<ListingItemDto<T>> = arrayListOf(),
    val after: String? = null,
    val before: String? = null
)

data class ListingItemDto<T>(val kind: String, val data: T)

open class TopListingElementDto(
    var id: String? = null,
    val author: String? = null,
    val title: String? = null,
    val createdUtc: Long? = null,
    val thumbnail: String? = null
)