package com.gaspardeelias.quickreddit.backend.service

import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import com.gaspardeelias.quickreddit.core.kernel.Either
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService

class TopListingServiceImpl(val retrofit: TopListingServiceRetrofit) :
    TopListingService {

    override fun getTopListing() =
        retrofit.getTopListing()
            .map { Either.Right(it) as Either<BasicError, TopListingDto> }
            .onErrorReturn { Either.Left(BasicError(-1, it.localizedMessage ?: "", it)) }

}