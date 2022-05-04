package AD340.HomeworkPractice

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface CamApiService {
    // for GET request API URL
    // sub url of the API url
    @GET("Data?zoomId=13&type=2")
    fun getTrafficCamData(): Call<JsonObject>
}