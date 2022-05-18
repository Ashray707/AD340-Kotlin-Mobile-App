package AD340.HomeworkPractice

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class MainActivity : AppCompatActivity() {

    lateinit var inputUsername: String
    lateinit var inputEmail: String
    lateinit var inputPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initalize Firebase
        FirebaseApp.initializeApp(this)

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

        // Read values from shared preference
        val savedPref = getSharedPreferenceValues()
        findViewById<EditText>(R.id.email_input).setText(savedPref[0])
        findViewById<EditText>(R.id.username_input).setText(savedPref[1])
        findViewById<EditText>(R.id.password_input).setText(savedPref[2])


        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{

            inputEmail = findViewById<EditText>(R.id.email_input).text.toString()
            inputPassword = findViewById<EditText>(R.id.password_input).text.toString()
            inputUsername = findViewById<EditText>(R.id.username_input).text.toString()

            // validate user inputs
            if(areInputsValid()){
                signIn()
            }
        }

    }

    fun isValidEmail(email: String): Boolean{
        if (email.isEmpty()){
            return false
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun areInputsValid(): Boolean {
        val email = isValidEmail(inputEmail)
        val username = !(inputUsername.isEmpty())
        val password = !(inputPassword.isEmpty())
        return (email && username && password)
    }

    fun saveToSharedPreferences(email: String, username: String, password: String){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString("email", email)
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    fun getSharedPreferenceValues(): List<String>{
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val sharedPrefData = ArrayList<String>()

        sharedPrefData.add(sharedPref.getString("email", "").toString())
        sharedPrefData.add(sharedPref.getString("username", "").toString())
        sharedPrefData.add(sharedPref.getString("password", "").toString())
        return sharedPrefData
    }

    fun signIn() {
        Log.d("FIREBASE", "signIn")
        val mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(
            inputEmail,
            inputPassword
        ).addOnCompleteListener(
                this
            ) { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    saveToSharedPreferences(
                        inputEmail,
                        inputUsername,
                        inputPassword
                    )
                    // update profile
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(inputUsername)
                        .build()
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FIREBASE", "User profile updated.")
                                // Go to FirebaseActivity
                                startActivity(Intent(this, FirebaseActivity::class.java))
                            }
                        }
                } else {
                    Log.d("FIREBASE", "sign-in failed")
                    Toast.makeText(this, "Sign In Failed",
                        Toast.LENGTH_SHORT).show();
                }
            }
    }

}