package com.gaspardeelias.quickreddit.core.repository.model

import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId

data class Post(override val id: String, val authorFullname: String?) : WithEntityId