package minhna.android.androidarchitecturecomponent.api

import minhna.android.smsreceiver.model.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by minhnguyen on 3/8/18.
 */
interface AppAPI {
    @POST(UrlAPI.userUpdateAPI)
    @FormUrlEncoded
    fun updateReceiver(@Body request: UserUpdateRequest): Call<UserUpdateRequest>
}