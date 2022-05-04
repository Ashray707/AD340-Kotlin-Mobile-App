package AD340.HomeworkPractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.http.Url

class TrafficAdapter(
    val apiCam: List <CameraModel>
): RecyclerView.Adapter<TrafficAdapter.TrafficViewHolder>(){

    // val SDOT = "https://www.seattle.gov/trafficcams/images/"
    // val WSDOT = "https://images.wsdot.wa.gov/nw/"

    class TrafficViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.cam_title)
        val img: ImageView = itemView.findViewById(R.id.cam_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrafficViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.traffic_item, parent, false)
        return TrafficViewHolder(view)
    }

    override fun getItemCount(): Int {
        return apiCam.size;
    }

    override fun onBindViewHolder(holder: TrafficViewHolder, position: Int) {
        holder.title.text = apiCam[position].Description

        // load sdot not wsdot needs fixing
        val cam = apiCam[position]
        val imgurl = cam.imageUrl()
        Picasso.get().load(imgurl).into(holder.img)

        // Loading image using Picasso
        // val ImgUrl = SDOT + apiCam[position].ImageUrl
        // Picasso.get().load(ImgUrl).into(holder.img)

        // camera pic doesn't load when using WSDOT
        // val ImgUrl2 = WSDOT + apiCam[position].ImageUrl
        // Picasso.get().load(ImgUrl2).into(holder.img)
    }
}