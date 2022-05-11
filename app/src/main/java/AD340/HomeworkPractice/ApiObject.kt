package AD340.HomeworkPractice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    fun getApiObject(): CamApiService{
        return Retrofit.Builder()
            .baseUrl("https://web6.seattle.gov/Travelers/api/Map/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CamApiService::class.java)
    }
}