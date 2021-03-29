package com.gaspardeelias.quickreddit.toplisting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gaspardeelias.repo.QuickRedditRepo
import com.gaspardeelias.repo.model.TopListingElementDto
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class ItemListActivityVM(topListingRepository: QuickRedditRepo): ViewModel() {


    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    val posts = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<TopListingElementDto>() },
        topListingRepository.posts(30).cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun showPosts() {
        clearListCh.offer(Unit)
    }


}