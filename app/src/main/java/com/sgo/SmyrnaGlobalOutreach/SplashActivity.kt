package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar

class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //typecasting of design elements
        progressBar = findViewById(R.id.progressBar)

        // Update progress bar using a background thread
        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus += 1

                // Update the progress bar on the UI thread
                handler.post {
                    progressBar.progress = progressStatus
                }

                try {
                    // Sleep for 50 milliseconds to show the progress gradually
                    Thread.sleep(50)


                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }).start()

    }
    }
