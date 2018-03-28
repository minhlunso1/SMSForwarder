package minhna.android.androidarchitecturecomponent.api

import io.reactivex.Observable
import minhna.android.androidarchitecturecomponent.model.CoinMarket
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

    fun getCoinMarket(): Observable<List<CoinMarket>> {
        return Observable.create {
            subscriber ->
            val response = appApi.getCoinMarket().execute()
            if (response.isSuccessful && response.body() != null) {
                subscriber.onNext(response.body()!!)
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
