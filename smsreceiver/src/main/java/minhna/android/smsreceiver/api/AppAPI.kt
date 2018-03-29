package minhna.android.smsreceiver.api

import minhna.android.smsreceiver.model.Message
import minhna.android.smsreceiver.model.Response
import minhna.android.smsreceiver.model.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by minhnguyen on 3/8/18.
 */
interface AppAPI {
    @POST(UrlAPI.userUpdateAPI)
    fun updateReceiver(@Body request: UserUpdateRequest): Call<Response>

    @POST(UrlAPI.sms)
    fun forwardSms(@Body request: Message): Call<Void>

    @GET(UrlAPI.listMessages)
    fun getListMessage(@QueryName user_name: String): Call<List<Message>>
}