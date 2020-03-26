package com.gaspardeelias.quickreddit.core.service

import com.gaspardeelias.quickreddit.core.kernel.Either
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import io.reactivex.Observable


interface TopListingService {

    fun getTopListing(): Observable<Either<BasicError, PostListingDto>>
}