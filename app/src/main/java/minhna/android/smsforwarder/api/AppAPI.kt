package minhna.android.smsforwarder.api

import minhna.android.smsforwarder.model.Response
import minhna.android.smsreceiver.model.Message
import minhna.android.smsreceiver.model.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by minhnguyen on 3/8/18.
 */
interface AppAPI {
    @POST(UrlAPI.userUpdateAPI)
    fun updateReceiver(@Body request: UserUpdateRequest): Call<UserUpdateRequest>

    @POST(UrlAPI.sms)
    fun forwardSms(@Body request: Message): Call<Response>
}