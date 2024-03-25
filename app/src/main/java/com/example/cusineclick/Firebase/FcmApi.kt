package  com.example.cusineclick.Firebase

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAiD-hD6E:APA91bHoVqJwpRGkjSXdkCYLwqoMiMHyy26OpvOog0aIsa38GIT41VDQ1jjXyaCVypDnM_QXpbPFpiGPiTPRs0fjxxxMZ5Ab1N2jTRlVgn5nBT3tF-vejwt1Rg8Ck_-IaorO5rw0Yywq"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: RequestNotification): Call<ResponseBody>
}