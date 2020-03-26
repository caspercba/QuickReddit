package com.gaspardeelias.quickreddit.core.repository.toplisting.model

import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId

data class TopListingElement(
    override val id: String,
    val authorFullname: String = "",
    val createdUtc: Long? = null
) : WithEntityId