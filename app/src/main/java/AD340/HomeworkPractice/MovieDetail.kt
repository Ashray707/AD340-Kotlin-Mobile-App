package AD340.HomeworkPractice

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle

class MovieDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val actionBar = supportActionBar
        actionBar!!.title = "Movie Details"
        actionBar.setDisplayHomeAsUpEnabled(true);

        val movieData = intent.extras?.getStringArray("movieDetail")
        findViewById<TextView>(R.id.movie_title).text = movieData?.get(0)
        findViewById<TextView>(R.id.movie_year).text = movieData?.get(1)
        findViewById<TextView>(R.id.movie_director).text = movieData?.get(2)
        findViewById<TextView>(R.id.movie_description).text = movieData?.get(4)
    }
}