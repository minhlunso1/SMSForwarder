package minhna.android.smsforwarder.model

import com.squareup.moshi.Json

/**
 * Created by minhnguyen on 3/29/18.
 */
data class Response(
        @Json(name = "error_code") val errorCode: Int,
        @Json(name = "message") val message: String
) {}