package com.gaspardeelias.quickreddit.core.repository.toplisting

import com.gaspardeelias.quickreddit.core.kernel.*
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManager
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManagerImpl
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.converters.TopListingConverter
import com.gaspardeelias.quickreddit.core.repository.toplisting.converters.TopListingConverter.Companion.convertTopListingElement
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingResponse
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class TopListingRepositoryImpl(val service: TopListingService) :
    TopListingRepository {

    private val topListingManager: EndlessListManager<TopListingElement> = EndlessListManagerImpl()

    override fun loadTopListing() {
        buildTopListingResponse(service.getTopListing(), false)
    }

    override fun listData(): Observable<EndlessList<TopListingElement>> = topListingManager.list

    private fun buildTopListingResponse(
        observable: Observable<Either<BasicError, TopListingDto>>,
        append: Boolean,
        endlessManager: EndlessListManager<TopListingElement> = topListingManager
    ) {

        observable
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isRight) {
                    val before = it.asRight()?.data?.before
                    val after = it.asRight()?.data?.after
                    var topList = it.asRight()!!.data?.children?.mapNotNull {
                        convertTopListingElement(it.data)
                    }
                    Either.Right(
                        TopListingResponse(
                            topList,
                            before,
                            after
                        )
                    ) as Either<BasicError, TopListingResponse>
                } else {
                    it as Either<BasicError, TopListingResponse>
                }
            }
            .onErrorReturn {
                Either.Left(it.toBasicError())
            }.subscribe {
                it.either({ basicError ->
                    endlessManager.error(
                        basicError.errorCode,
                        basicError.errorMessage,
                        basicError.exception
                    )
                }, {
                    if (append) {
                        endlessManager.append(it.elements, it.after)
                    } else {
                        endlessManager.set(it.elements, it.after)
                    }
                })
            }
    }

    override fun nextPage() {}

}