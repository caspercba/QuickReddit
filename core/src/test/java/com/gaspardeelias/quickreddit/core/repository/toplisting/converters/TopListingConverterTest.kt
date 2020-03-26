package com.gaspardeelias.quickreddit.core.repository.toplisting.converters

import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingElementDto
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TopListingConverterTest {

    @Test
    fun convertNullId() {
        Assert.assertNull(TopListingConverter.convertTopListingElement(TopListingElementDto()))
    }
}