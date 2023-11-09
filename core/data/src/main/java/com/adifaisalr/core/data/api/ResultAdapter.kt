package com.adifaisalr.core.data.api

import com.adifaisalr.core.domain.model.dataholder.ErrorData
import com.adifaisalr.core.domain.model.dataholder.Results
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<Results<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<Results<Type>> = ResultCall(call)
}

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Results<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<Results<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val Results = if (code in 200 until 300) {
                val body = response.body()
                body?.let {
                    val successResult: Results<T> = Results.Success(body)
                    successResult
                } ?: Results.NoData
            } else {
                Results.Failure(ErrorData(response.message(), code))
            }

            callback.onResponse(this@ResultCall, Response.success(Results))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val Results = if (t is IOException) {
                Results.NetworkError
            } else {
                Results.Failure(ErrorData(t.toString(), 0))
            }

            callback.onResponse(this@ResultCall, Response.success(Results))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())
}

class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                Results::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }

                else -> null
            }
        }

        else -> null
    }
}