package minhna.android.smsreceiver.model

import com.squareup.moshi.Json

/**
 * Created by minhnguyen on 3/27/18.
 */
data class Message(
        @Json(name = "user_name") var userName: String,
        @Json(name = "from")val from: String,
        @Json(name = "message") val body: String) {

    constructor(from: String, body: String): this("", from, body)
}