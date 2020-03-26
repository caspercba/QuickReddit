package com.gaspardeelias.quickreddit.core.kernel.list

import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId

sealed class EndlessList<T : WithEntityId> {
    abstract val list : List<T>

    class NotReady<T : WithEntityId>(override val list: List<T>) : EndlessList<T>()

    class Loading<T : WithEntityId>(override val list: List<T>) : EndlessList<T>()

    class Ready<T : WithEntityId>(override val list: List<T>) : EndlessList<T>()

    class Error<T : WithEntityId>(override val list: List<T>, val error: BasicError) : EndlessList<T>()
}
