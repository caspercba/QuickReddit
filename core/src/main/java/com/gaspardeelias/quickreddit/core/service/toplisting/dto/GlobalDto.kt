package com.gaspardeelias.quickreddit.core.service.toplisting.dto

open class GlobalDto<T>(val kind: String, val data: T)

class TopListingDto(kind: String, data: ListingDto<TopListingElementDto>) :
    GlobalDto<ListingDto<TopListingElementDto>>(kind, data)

data class ListingDto<T>(
    val modhash: String? = null,
    val dist: Int? = null,
    val children: List<T> = arrayListOf(),
    val after: String? = null,
    val before: String? = null
)

open class TopListingElementDto(
    var id: String? = null,
    val authorFullname: String? = null,
    val createdUtc: Long? = null
)