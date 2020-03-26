package com.gaspardeelias.quickreddit.core.kernel.list

import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class EndlessListManagerImpl<T : WithEntityId>(
        override var nextPage: String?=null,
        override val publishList: PublishSubject<EndlessList<T>> = PublishSubject.create(),
        override val list: BehaviorSubject<EndlessList<T>> = BehaviorSubject.createDefault(EndlessList.NotReady(emptyList()))) : EndlessListManager<T>
