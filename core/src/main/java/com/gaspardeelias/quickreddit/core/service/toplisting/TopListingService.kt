package com.gaspardeelias.quickreddit.core.service.toplisting

import com.gaspardeelias.quickreddit.core.kernel.Either
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import io.reactivex.Observable


interface TopListingService {

    fun getTopListing(): Observable<Either<BasicError, TopListingDto>>
    fun nextPage(next: String): Observable<Either<BasicError, TopListingDto>>
}