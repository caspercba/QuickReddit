package com.gaspardeelias.quickreddit.core.repository.toplisting.converters

import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingElementDto

class TopListingConverter {

    companion object {
        fun convertTopListingElement(it: TopListingElementDto): TopListingElement? =
            if (it.id == null) {
                null
            } else {
                TopListingElement(
                    it.id!!,
                    authorFullname = it.authorFullname ?: "",
                    title = it.title,
                    createdUtc = it.createdUtc,
                    thumbnail = it.thumbnail
                )
            }
    }
}