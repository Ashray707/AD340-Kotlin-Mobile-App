package AD340.HomeworkPractice

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrafficCamActivity : AppCompatActivity() {
    val API_URL = "https://web6.seattle.gov/Travelers/api/Map/"
    var cameraList = ArrayList<CameraModel>()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_cam)

        // Title bar name
        supportActionBar?.title = "Seattle Traffic Cameras"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // check if wifi or mobile data is on (connectivity)
        // if not connected prints out error message
        checkNetworkConnection()
    }

    // retrofit getting Api data
    fun getAPIData(recyclerView: RecyclerView){
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL) // root url
            .addConverterFactory(GsonConverterFactory.create())
            .build()
                // interface CamApiService takes in sub url
            .create(CamApiService::class.java)

        val request = retrofit.getTrafficCamData()
        request.enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = response.body()?.get("Features")!!.asJsonArray
                cameraList = apiList(data)
                recyclerView.adapter = TrafficAdapter(cameraList)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable){
                val noConnectionText = findViewById<TextView>(R.id.text_traffic_no_connection)
                noConnectionText.text = ("No Connection. Please check your internet connection and try again")
            }
        })
    }

    // adds camera Api into cameralist
    fun apiList(jsonArray: JsonArray): ArrayList<CameraModel> {
        val cameraList = ArrayList<CameraModel>()
        val n = jsonArray.size()-1

        for(i in 0..n){
            val item = jsonArray[i].asJsonObject
            val cameraData = item.getAsJsonArray("Cameras")[0].asJsonObject
            cameraList.add(
                CameraModel(
                    // camera model json file
                    cameraData.get("Id").asString,
                    cameraData.get("Description").asString,
                    cameraData.get("ImageUrl").asString,
                    cameraData.get("Type").asString,)
            )
        }
        return cameraList
    }

    // check for network connection
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkNetworkConnection(){
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)


        val linkProperties = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        if (linkProperties == true) {
            // loads camera images and title
            val recyclerViewTraffic = findViewById<RecyclerView>(R.id.recycler_view_traffic)
            getAPIData(recyclerViewTraffic)
        } else {
            // returns error message String to the user
            val noConnectionText = findViewById<TextView>(R.id.text_traffic_no_connection)
            noConnectionText.text = ("No Connection. Please check your internet connection and try again")
        }
    }
}
