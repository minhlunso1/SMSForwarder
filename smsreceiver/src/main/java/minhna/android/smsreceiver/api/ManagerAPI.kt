package minhna.android.smsreceiver.api

import io.reactivex.Observable
import minhna.android.smsreceiver.model.Message
import minhna.android.smsreceiver.model.Response
import minhna.android.smsreceiver.model.UserUpdateRequest
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by minhnguyen on 3/8/18.
 */
class ManagerAPI {
    private val appApi: AppAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(UrlAPI.api)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        appApi = retrofit.create(AppAPI::class.java)
    }

    fun registerReceiver(request: UserUpdateRequest): Observable<Response> {
        return Observable.create {
            subscriber ->
            val response = appApi.updateReceiver(request).execute()
            if (response.isSuccessful && response.body() != null) {
                subscriber.onNext(response.body()!!)
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun forwardSms(request: Message): Observable<Void> {
        return Observable.create {
            subscriber ->
            val response = appApi.forwardSms(request).execute()
            if (response.isSuccessful) {
                subscriber.onNext(response.body()!!)
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun getListMessages(userName: String): Observable<List<Message>> {
        return Observable.create {
            subscriber ->
            val response = appApi.getListMessage(userName).execute()
            if (response.isSuccessful && response.body() != null) {
                subscriber.onNext(response.body()!!)
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
