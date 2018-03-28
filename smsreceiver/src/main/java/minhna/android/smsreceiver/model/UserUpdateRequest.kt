package minhna.android.smsreceiver.model

import com.squareup.moshi.Json

/**
 * Created by minhnguyen on 3/28/18.
 */
data class UserUpdateRequest(
        @Json(name = "user_name") var userName: String,
        @Json(name = "token") var token: String,
        @Json(name = "uuid") var uuid: String
)