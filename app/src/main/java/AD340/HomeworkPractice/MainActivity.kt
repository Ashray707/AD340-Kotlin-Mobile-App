package AD340.HomeworkPractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Movies List and Details Button
        val clickMovies = findViewById<Button>(R.id.btn_movies);
        clickMovies.setOnClickListener {
            val intent = Intent(this, MovieList::class.java)
            startActivity(intent)
        }

        // Traffic Cam Button
        val clickCamera = findViewById<Button>(R.id.btn_traffic_cam);
        clickCamera.setOnClickListener {
            val intent = Intent(this, TrafficCamActivity::class.java)
            startActivity(intent)
        }

        // Camera Map Button
        val clickSignUp = findViewById<Button>(R.id.btn_cam_map);
        clickSignUp.setOnClickListener {
            val intent = Intent(this , CameraMap::class.java)
            startActivity(intent)
        }

        val clickLogin = findViewById<Button>(R.id.btn_login);
        clickLogin.setOnClickListener {
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show()
        }

        val clickHello = findViewById<Button>(R.id.btn_hello);
        clickHello.setOnClickListener {
            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show()
        }
        val clickWorld = findViewById<Button>(R.id.btn_world);
        clickWorld.setOnClickListener {
            Toast.makeText(this,"World",Toast.LENGTH_SHORT).show()
        }
    }
}