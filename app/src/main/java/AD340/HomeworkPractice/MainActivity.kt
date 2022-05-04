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

        val clickMovies = findViewById<Button>(R.id.btn_movies);
        clickMovies.setOnClickListener {
            val intent = Intent(this, MovieList::class.java)
            startActivity(intent)
        }

        val clickCamera = findViewById<Button>(R.id.btn_camera);
        clickCamera.setOnClickListener {
            val intent = Intent(this, TrafficCamActivity::class.java)
            startActivity(intent)
        }

        val clickLogin = findViewById<Button>(R.id.btn_login);
        clickLogin.setOnClickListener {
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show()
        }

        val clickSignUp = findViewById<Button>(R.id.btn_signup);
        clickSignUp.setOnClickListener {
            Toast.makeText(this,"Sign Up",Toast.LENGTH_SHORT).show()
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