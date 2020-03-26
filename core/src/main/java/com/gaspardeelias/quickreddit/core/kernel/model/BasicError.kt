package com.gaspardeelias.quickreddit.core.kernel.model

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

data class BasicError(
    val errorCode: Int = -1,
    val errorMessage: String="",
    val exception : Throwable? = null,
    val errors : List<Pair<String, String>> = emptyList()
) {
    fun getError(errorKey : String): String? =
        errors.asSequence()
            .filter { pair -> pair.first == errorKey }
            .map { pair -> pair.second }
            .firstOrNull()

    constructor(throwable: Throwable) :
            this(-1, throwable.localizedMessage, throwable, collectErrors(throwable))


    companion object {
        fun collectErrors(it: Throwable): List<Pair<String, String>> =
            when(it) {
                is HttpException -> {
                    it.response()?.errorBody() ?. let {
                        val list =  mutableListOf<Pair<String, String>>()

                        try {
                            val json = JSONObject(it.string())
                            if (json.has("errors") && !json.isNull("errors")) {
                                val jsonErrors = json.getJSONObject("errors")
                                for(key in jsonErrors.keys()) {
                                    list.add(Pair(key, jsonErrors.getString(key)))
                                }
                            }
                        } catch (ex : Throwable) {
                            Log.e("ERROR", ex.localizedMessage, ex)
                        }
                        list
                    } ?: mutableListOf()
                }
                else -> {
                    mutableListOf<Pair<String, String>>()
                }
            }
    }
}
