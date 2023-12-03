package com.sgo.SmyrnaGlobalOutreach

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast

class Rehab : AppCompatActivity() {
    // var
    private lateinit var btnDownloadForm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rehab)
        // typecast
        btnDownloadForm = findViewById(R.id.btnDownloadForm)

        // set button click event
        btnDownloadForm.setOnClickListener {
            val pdfUrl =
                "https://drive.google.com/u/0/uc?id=11ztVyz8z3oFcCGkk_oc0tXDzOB0j-7MX&export=download"
            val pdfTitle = "REHAB_CONSENT_AND_INDEMNITY_FORMS.pdf"
            downloadPdf(pdfUrl, pdfTitle)
        }
    }

    private fun downloadPdf(pdfUrl: String, pdfTitle: String) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle(pdfTitle)
            .setMimeType("application/pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pdfTitle)

        val downloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Rehab Application Downloaded", Toast.LENGTH_SHORT).show()
    }
}