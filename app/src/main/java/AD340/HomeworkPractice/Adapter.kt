package AD340.HomeworkPractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    val movieList: Array<Array<String>>,
    val clickListener: (Int) -> Unit
    ): RecyclerView.Adapter<Adapter.MovieViewHolder>(){

    class MovieViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.movie_title)
        val year: TextView = itemView.findViewById(R.id.movie_year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieValue = movieList[position]
        holder.title.text = movieValue[0]
        holder.year.text = movieValue[1]
        holder.itemView.setOnClickListener{clickListener(position)}
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}