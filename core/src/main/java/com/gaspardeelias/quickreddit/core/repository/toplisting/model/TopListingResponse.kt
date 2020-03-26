package com.gaspardeelias.quickreddit.core.repository.toplisting.model

data class TopListingResponse(val elements: List<TopListingElement>, val before: String? = null, val after: String? = null)