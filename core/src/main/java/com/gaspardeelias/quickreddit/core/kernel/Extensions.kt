package com.gaspardeelias.quickreddit.core.kernel

import com.gaspardeelias.quickreddit.core.kernel.model.BasicError

fun Throwable.toBasicError() : BasicError =
    BasicError(-1, this.localizedMessage ?: "unknown", this)