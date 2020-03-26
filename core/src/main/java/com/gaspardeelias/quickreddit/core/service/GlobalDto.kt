package com.gaspardeelias.quickreddit.core.service

open class GlobalDto<T>(val kind: String, val data: T?, val after: String?, val before: String?)

class PostListingDto(kind: String, data: ListingDto<PostDto>, after: String?, before: String?) :
    GlobalDto<ListingDto<PostDto>>(kind, data, after, before)

data class ListingDto<T>(val modhash: String?, val dist: Int?, val children: List<T>?)

data class PostDto(val authorFullname: String?)