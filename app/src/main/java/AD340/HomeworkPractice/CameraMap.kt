package AD340.HomeworkPractice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


internal class CameraMap: AppCompatActivity(), OnMapReadyCallback {
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

    private lateinit var gMap: GoogleMap
    private var locationPermissionGranted = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // default location in Seattle
    private val SEATTLE = LatLng(47.6081206,-122.3370977)
    //private val LYNNWOOD = LatLng(47.827868,  -122.305391)

    private val DEFAULT_ZOOM = 12f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retrieve content view rendering the map
        setContentView(R.layout.fragment_camera_map)

        // Set action bar title
        supportActionBar?.title = "Traffic Camera Map"

        // check permissions
        getLocationPermission()

        // FusedLocationProviderClient Construct
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun getLocationPermission(){
        if(ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(){
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            gMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude), 13f)
                            )
                            // add marker current location
                            gMap.apply {
                                val position = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                                gMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title("Current Location")
                                        .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE))
                                )
                            }
                        }
                    } else {
                        Log.d("ERROR", "Current location is null. Using defaults.")
                        Log.e("ERROR", "Exception: %s", task.exception)
                        gMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(SEATTLE, DEFAULT_ZOOM))
                           //.newLatLngZoom(LYNNWOOD, DEFAULT_ZOOM))
                        gMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.moveCamera(CameraUpdateFactory.zoomTo(13f))

        // Center map in Seattle, re-center when user location available
        gMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE))
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(LYNNWOOD))

        getCameraData()
        //getDeviceLocation()
    }

    fun getCameraData(){
        val api = ApiObject.getApiObject()
        CoroutineScope(Dispatchers.IO).launch{
            val response = api.getTrafficCamData().awaitResponse()
            if(response.isSuccessful){
                val data = response.body()?.get("Features")!!.asJsonArray
                val locationData = apiList(data)

                withContext(Dispatchers.Main){
                    getDeviceLocation()
                    updateMap(locationData)
                }
            }
        }
    }


    fun updateMap(data: ArrayList<CameraLocation>){

        for(item: CameraLocation in data){
            val location = LatLng(item.Coordinates[0], item.Coordinates[1])
            gMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(item.Cameras[0].Description)
            )
        }
    }

    fun apiList(jsonArray: JsonArray): ArrayList<CameraLocation> {
        val cameraList = ArrayList<CameraModel>()
        ArrayList<ArrayList<Double>>()
        val cameraArray = ArrayList<CameraLocation>()
        val n = jsonArray.size()-1

        for(i in 0..n){
            val item = jsonArray[i].asJsonObject
            val cameraData = item.getAsJsonArray("Cameras")[0].asJsonObject
            val locationData = item.getAsJsonArray("PointCoordinate").asJsonArray

            // add location data
            val pointContainer = ArrayList<Double>()
            pointContainer.add(locationData[0].asDouble)
            pointContainer.add(locationData[1].asDouble)

            // add camera data
            cameraList.add(
                CameraModel(
                    cameraData.get("Id").asString,
                    cameraData.get("Description").asString,
                    cameraData.get("ImageUrl").asString,
                    cameraData.get("Type").asString,)
            )

            // add to Camera Array
            val singleLocation = CameraLocation(pointContainer, cameraList)
            cameraArray.add(singleLocation)
        }
        return cameraArray
    }

}