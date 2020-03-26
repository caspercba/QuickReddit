package com.gaspardeelias.quickreddit.core.kernel.list


import com.gaspardeelias.quickreddit.core.kernel.Either
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject


object NoChange

sealed class EndlessListEvent {
    object Reset : EndlessListEvent()

    object StartLoading : EndlessListEvent()

    data class Loaded<T : WithEntityId>(
            val position: Int? = null,
            val records: List<T>
    ) : EndlessListEvent()

    data class LoadedToBegginig<T : WithEntityId>(
            val records: List<T>
    ) : EndlessListEvent()

    data class Error<T : WithEntityId>(
            val errorCode: Int? = null,
            val errorMessage: String,
            val error: Throwable? = null
    ) : EndlessListEvent() {
        fun toBasicError(): BasicError = BasicError(errorCode = errorCode
                ?: -1, errorMessage = errorMessage, exception = error)
    }
}


interface EndlessListManager<T : WithEntityId> {
    val publishList: PublishSubject<EndlessList<T>>
    //Do we need a behaviour here?
    val list: BehaviorSubject<EndlessList<T>>
    //next page
    var nextPage: String?

    fun createEmptyList(): List<T> = emptyList()

    private fun getList() = list.value ?: EndlessList.NotReady(createEmptyList())

    fun reset() {
        handle(newListEvent(EndlessList.NotReady(createEmptyList()), EndlessListEvent.Reset))

    }

    fun loading() {
        handle(newListEvent(getList(), EndlessListEvent.StartLoading))
    }

    fun set(list: List<T>, nextPage: String?) {
        this.nextPage = nextPage
        handle(newListEvent(EndlessList.NotReady(createEmptyList()), EndlessListEvent.Loaded<T>(records = list)))
    }

    fun append(position: Int, list: List<T>) {
        handle(newListEvent(getList(), EndlessListEvent.Loaded<T>(position, list)))
    }


    fun append(list: List<T>, nextPage: String?) {
        this.nextPage = nextPage
        handle(newListEvent(getList(), EndlessListEvent.Loaded<T>(records = list)))
    }

    fun appendBegginig(list: List<T>, nextPage: String?) {
        this.nextPage = nextPage
        handle(newListEvent(getList(), EndlessListEvent.LoadedToBegginig<T>(list)))
    }
    fun appendBegginig(item : T) {
        handle(newListEvent(getList(), EndlessListEvent.LoadedToBegginig<T>(listOf(item))))
    }

    fun set(list: List<T>) {
        handle(newListEvent(EndlessList.NotReady(createEmptyList()), EndlessListEvent.Loaded<T>(records = list)))
    }

    fun append(list: List<T>) {
        handle(newListEvent(getList(), EndlessListEvent.Loaded<T>(records = list)))
    }

    fun append(item: T) {
        handle(newListEvent(getList(), EndlessListEvent.Loaded<T>(records = listOf(item))))
    }

    fun error(errorCode: Int? = null, message: String? = null, th: Throwable? = null) {
        handle(newListEvent(getList(), EndlessListEvent.Error<T>(errorCode, message ?: th?.message
        ?: "Error", th)))
    }

    fun handle(newListState: Either<NoChange, EndlessList<T>>) {
        newListState.either({}, {
            publishList.onNext(it)
            list.onNext(it)
        })
    }

    private fun newListEvent(oldList: EndlessList<T>, event: EndlessListEvent) =
            when (oldList) {
                is EndlessList.NotReady -> reduceNotReady(oldList, event)
                is EndlessList.Loading -> reduceLoading(oldList, event)
                is EndlessList.Ready -> reduceReady(oldList, event)
                is EndlessList.Error -> reduceError(oldList, event)
            }


    private fun reduceNotReady(oldList: EndlessList.NotReady<T>, event: EndlessListEvent): Either<NoChange, EndlessList<T>> =
            when (event) {
                is EndlessListEvent.StartLoading -> Either.Right(EndlessList.Loading(oldList.list))
                is EndlessListEvent.Error<*> -> Either.Right(EndlessList.Error(oldList.list, event.toBasicError()))
                is EndlessListEvent.Loaded<*> -> Either.Right(EndlessList.Ready(event.records as List<T>))
                is EndlessListEvent.LoadedToBegginig<*> -> Either.Right(EndlessList.Ready(event.records as List<T>))
                is EndlessListEvent.Reset -> Either.Right(EndlessList.NotReady(emptyList()))
            }

    private fun reduceLoading(oldList: EndlessList.Loading<T>, event: EndlessListEvent): Either<NoChange, EndlessList<T>> =
            when (event) {
                is EndlessListEvent.StartLoading -> Either.Left(NoChange)
                is EndlessListEvent.Error<*> -> Either.Right(EndlessList.Error(oldList.list, event.toBasicError()))
                is EndlessListEvent.Loaded<*> -> mergePages(event.position, oldList, event)
                is EndlessListEvent.LoadedToBegginig<*> -> appendListBeggining(oldList, event)
                is EndlessListEvent.Reset -> Either.Right(EndlessList.NotReady(emptyList()))
            }

    private fun reduceReady(oldList: EndlessList.Ready<T>, event: EndlessListEvent): Either<NoChange, EndlessList<T>> =
            when (event) {
                is EndlessListEvent.StartLoading -> Either.Right(EndlessList.Loading(oldList.list))
                is EndlessListEvent.Error<*> -> Either.Right(EndlessList.Error(oldList.list, event.toBasicError()))
                is EndlessListEvent.Loaded<*> -> mergePages(event.position, oldList, event)
                is EndlessListEvent.LoadedToBegginig<*> -> appendListBeggining(oldList, event)
                is EndlessListEvent.Reset -> Either.Right(EndlessList.NotReady(emptyList()))
            }


    fun appendListBeggining(oldList: EndlessList<T>, event: EndlessListEvent.LoadedToBegginig<*>): Either<NoChange, EndlessList<T>> =
            Either.Right(EndlessList.Ready<T>((event.records as List<T>) + oldList.list))

    fun mergePages(position: Int?, oldList: EndlessList<T>, event: EndlessListEvent.Loaded<*>): Either<NoChange, EndlessList<T>> =
            when (position) {
                null -> Either.Right(EndlessList.Ready<T>(oldList.list + (event.records as List<T>)))
                0 -> Either.Right(EndlessList.Ready<T>((event.records as List<T>) + oldList.list))
                else -> {
                    val mutable = oldList.list.toMutableList()
                    mutable.addAll(position, (event.records as List<T>))
                    Either.Right(EndlessList.Ready<T>(mutable))
                }
            }

    private fun reduceError(oldList: EndlessList.Error<T>, event: EndlessListEvent): Either<NoChange, EndlessList<T>> =
            when (event) {
                is EndlessListEvent.StartLoading -> Either.Right(EndlessList.Loading(oldList.list))
                is EndlessListEvent.Error<*> -> Either.Right(EndlessList.Error(oldList.list, event.toBasicError()))
                is EndlessListEvent.Loaded<*> -> mergePages(event.position, oldList, event)
                is EndlessListEvent.LoadedToBegginig<*> -> appendListBeggining(oldList, event)
                is EndlessListEvent.Reset -> Either.Right(EndlessList.NotReady(emptyList()))
            }

}
