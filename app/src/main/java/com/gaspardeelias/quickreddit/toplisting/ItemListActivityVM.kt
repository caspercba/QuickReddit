package com.gaspardeelias.quickreddit.toplisting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gaspardeelias.repo.QuickRedditRepo

class ItemListActivityVM(topListingRepository: QuickRedditRepo): ViewModel() {
    val posts = topListingRepository.posts(30).cachedIn(viewModelScope)
}