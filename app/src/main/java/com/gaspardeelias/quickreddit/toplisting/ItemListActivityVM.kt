package com.gaspardeelias.quickreddit.toplisting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class ItemListActivityVM(val topListingRepository: TopListingRepository): ViewModel() {

    var bag = CompositeDisposable()
    var liveData = MutableLiveData<EndlessList<TopListingElement>>()

    fun attach() {
        bag.add(topListingRepository.listData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe{
                liveData.postValue(it)
        })
        refresh()
    }

    fun detach() {
        bag.clear()
        bag = CompositeDisposable()
    }

    fun refresh() {
        topListingRepository.loadTopListing()
    }

    fun nextPage() {
        topListingRepository.nextPage()
    }
}