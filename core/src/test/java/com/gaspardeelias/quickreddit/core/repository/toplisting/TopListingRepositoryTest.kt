package com.gaspardeelias.quickreddit.core.repository.toplisting

import com.gaspardeelias.quickreddit.core.TestSchedulerRule
import com.gaspardeelias.quickreddit.core.kernel.Either
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.ListingDto
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingElementDto
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class TopListingRepositoryTest {

    @Mock
    lateinit var topListingService: TopListingService

    @Rule
    @JvmField
    var testSchedulerRule = TestSchedulerRule()

    @Mock
    lateinit var element1: TopListingElementDto

    @Mock
    lateinit var element2: TopListingElementDto

    @Mock
    lateinit var element3: TopListingElementDto

    @Test
    fun testInitTopListingState() {
        // GIVEN
        val repo =
            TopListingRepositoryImpl(
                topListingService
            )
        val testObserver = TestObserver<EndlessList<TopListingElement>>()

        // WHEN
        repo.listData().observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .subscribe(testObserver)

        testSchedulerRule.advanceTimeBy(20, TimeUnit.SECONDS)

        // THEN
        testObserver.assertNotComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it is EndlessList.NotReady }
        testObserver.assertValue { it.list.isEmpty() }

    }

    @Test
    fun testLoadListToReadyState() {
        //GIVEN
        val repo =
            TopListingRepositoryImpl(
                topListingService
            )
        val testObserver = TestObserver<EndlessList<TopListingElement>>()
        element1.id = "1"
        element2.id = "2"
        element3.id = "3"
//        Mockito.`when`(element1.id).thenReturn("1")
//        Mockito.`when`(element2.id).thenReturn("2")
//        Mockito.`when`(element3.id).thenReturn("3")

        val topListingDto = TopListingDto(
            "Listing",
            ListingDto(children = arrayListOf(element1, element2, element3))
        )

        // WHEN
        Mockito.`when`(topListingService.getTopListing())
            .thenReturn(Observable.just(Either.Right(topListingDto)))

        repo.listData().observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .subscribe(testObserver)


        //THEN
        testObserver.assertNotComplete()
        testObserver.assertValueCount(3)
        testObserver.assertValueAt(0) { it is EndlessList.NotReady }
        testObserver.assertValueAt(0) { it.list.isEmpty() }
        testObserver.assertValueAt(1) { it is EndlessList.Loading }
        testObserver.assertValueAt(1) { it.list.isEmpty() }
        testObserver.assertValueAt(2) { it is EndlessList.Ready }
        testObserver.assertValueAt(3) { compare(listOf("1", "2", "3"), it.list) }


    }

    @Test
    fun testUpcomingAppendListToErrorState() {
        // GIVEN
        val repo = TopListingRepositoryImpl(topListingService)
        val testObserver = TestObserver<EndlessList<TopListingElement>>()
        Mockito.`when`(element1.id).thenReturn("10")

        val initTopListingDto = TopListingDto("Listing", ListingDto(children = arrayListOf(element1)))

        Mockito.`when`(topListingService.getTopListing())
            .thenReturn(
                Observable.just(
                    Either.Right(initTopListingDto)
                )
            )
        repo.loadTopListing()
        testSchedulerRule.advanceTimeBy(20, TimeUnit.SECONDS) // one element on list


        // WHEN
        val topListingDto = TopListingDto(
            "Listing",
            ListingDto(children = arrayListOf(element2, element3))
        )

        Mockito.`when`(topListingService.getTopListing())
            .thenReturn(
                Observable.just(
                    Either.Left(BasicError(1, "err1"))
                )
            )

        repo.listData()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(testObserver)

        // wait for not ready event
        testSchedulerRule.advanceTimeBy(20, TimeUnit.SECONDS)

        // this will trigger -> loading -> ready
        repo.loadTopListing()
        testSchedulerRule.advanceTimeBy(20, TimeUnit.SECONDS)

        // THEN
        testObserver.assertNotComplete()
        testObserver.assertValueCount(3)
        testObserver.assertValueAt(0) { it is EndlessList.Ready }
        testObserver.assertValueAt(0) { compare(listOf("10"), it.list) }
        testObserver.assertValueAt(1) { it is EndlessList.Loading }
        testObserver.assertValueAt(1) { compare(listOf("10"), it.list) }
        testObserver.assertValueAt(2) { it is EndlessList.Error }
        testObserver.assertValueAt(2) { compare(listOf("10"), it.list) }
        testObserver.assertValueAt(2) { (it as EndlessList.Error).error == BasicError(1, "err1") }
    }

    fun compare(expected: List<String>, actual: List<TopListingElement>): Boolean {
        if (expected.size != actual.size) {
            return false
        }
        actual.forEachIndexed { i, item ->
            if (expected[i] != actual[i].id) {
                print("expected $i: ${expected[i]} != actual: ${actual[i]}")
                return false
            }
        }

        return true
    }

}