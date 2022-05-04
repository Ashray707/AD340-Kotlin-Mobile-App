package AD340.HomeworkPractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MovieList : AppCompatActivity() {
    val movieData = MovieData().getMoviesList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        val actionBar = supportActionBar
        actionBar!!.title = "Movies"
        actionBar.setDisplayHomeAsUpEnabled(true);

        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = Adapter(movieData) { position: Int -> onMovieClick(position) }
    }

    fun onMovieClick(position: Int){
        val selectedMovie = movieData[position]
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra("movieDetail", selectedMovie)
        startActivity(intent)
    }
}





