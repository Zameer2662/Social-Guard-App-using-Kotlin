@file:Suppress("DEPRECATION")

package com.example.socialguard

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var fetchCommentsButton: Button
    private val apiKey =
        "AIzaSyDN2jrk_4YqOfiHxSJoqBIuFo6lZdepiCE" // Replace with your YouTube API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webview)
        fetchCommentsButton = findViewById(R.id.fetchCommentsButton)

        setupWebView()
        webView.loadUrl("https://www.youtube.com/")


        // Button click to fetch comments
        fetchCommentsButton.setOnClickListener {
            val currentUrl = webView.url
            if (currentUrl != null) {
                val videoId = extractVideoId(currentUrl)
                if (videoId != null) {
                    fetchAllComments(videoId)
                } else {
                    showAlert("Error", "No video ID found in the URL.")
                }
            } else {
                showAlert("Error", "Failed to get the current URL.")
            }
        }
    }

    private fun setupWebView() {
        // Enable JavaScript and configure WebView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Load from cache if available

        // Ensure cookies are handled correctly
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
        cookieManager.flush()

        // Set the WebView client to track page loading events
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView,
                url: String,
                favicon: android.graphics.Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

            }
        }

        // Add JavaScript interface for future functionality (e.g., downloading)
        webView.addJavascriptInterface(JavaScriptInterface(), "Android")
    }



    // JavaScript Interface class for identifying media
    // JavaScript Interface class for identifying media
    inner class JavaScriptInterface {
        @JavascriptInterface
        fun identifyMedia() {
            runOnUiThread {
                AlertDialog.Builder(this@WebViewActivity)
                    .setTitle("Processing")
                    .setMessage("ML Algorithm is running for Detecting Comments! Please wait for API Call")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun extractVideoId(url: String): String? {
        val regex = "v=([^&]+)".toRegex()
        return regex.find(url)?.groups?.get(1)?.value
    }

    // Fetch all comments from the YouTube Data API
    private fun fetchAllComments(videoId: String) {
        val comments = mutableListOf<String>()
        var nextPageToken: String? = null
        val apiUrl =
            "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=$videoId&key=$apiKey&maxResults=100"

        AsyncTask.execute {
            try {
                do {
                    val url =
                        URL(if (nextPageToken == null) apiUrl else "$apiUrl&pageToken=$nextPageToken")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connect()

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val response = connection.inputStream.bufferedReader().use { it.readText() }
                        val jsonResponse = JSONObject(response)

                        val items = jsonResponse.optJSONArray("items")
                        for (i in 0 until (items?.length() ?: 0)) {
                            val snippet = items?.getJSONObject(i)
                                ?.getJSONObject("snippet")
                                ?.getJSONObject("topLevelComment")
                                ?.getJSONObject("snippet")
                            snippet?.let {
                                val comment = it.optString("textDisplay", "")
                                comments.add(comment)
                            }
                        }

                        // Check for next page token
                        nextPageToken = jsonResponse.optString("nextPageToken", null)
                    } else {
                        runOnUiThread {
                            showAlert(
                                "Error",
                                "Failed to fetch comments. Response Code: $responseCode"
                            )
                        }
                        return@execute
                    }
                } while (nextPageToken != null)

                runOnUiThread {
                    showCommentsDialog(comments)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    showAlert("Error", "An error occurred: ${e.message}")
                }
            }
        }
    }

    // Show all comments in a custom dialog
    private fun showCommentsDialog(comments: List<String>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_comments, null)
        val commentsContainer = dialogView.findViewById<LinearLayout>(R.id.commentsContainer)

        // Add comments to the container
        for (comment in comments) {
            val commentView = TextView(this).apply {
                text = comment
                setPadding(8, 8, 8, 8)
                textSize = 16f
            }
            commentsContainer.addView(commentView)
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent the dialog from being closed accidentally
            .create()

        // Detect Hate Speech Button Handler
        dialogView.findViewById<Button>(R.id.detectHateButton).setOnClickListener {
            detectHateSpeech(comments, dialog)
        }

        // Close button handler
        dialogView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun detectHateSpeech(comments: List<String>, currentDialog: AlertDialog) {
        // Perspective API Endpoint and Key
        val PERSPECTIVE_API_URL = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze"
        val PERSPECTIVE_API_KEY = "AIzaSyBojWzpp7b52KlkcZyFRDrX0CuIlhFWJuo"

        // Use OkHttp for making network calls
        val client = OkHttpClient()

        // Define batch size (e.g., process 10 comments per batch)
        val batchSize = 10

        AsyncTask.execute {
            try {
                val hateComments = mutableListOf<String>()

                // Process comments in batches
                comments.chunked(batchSize).forEach { batch ->
                    batch.forEach { comment ->
                        // Prepare JSON request body
                        val requestBody = """
                        {
                          "comment": { "text": "$comment" },
                          "languages": ["en"],
                          "requestedAttributes": { "TOXICITY": {} }
                        }
                    """.trimIndent()

                        // Build the HTTP request
                        val request = Request.Builder()
                            .url("$PERSPECTIVE_API_URL?key=$PERSPECTIVE_API_KEY")
                            .post(RequestBody.create("application/json".toMediaType(), requestBody))
                            .build()

                        // Execute the request
                        val response = client.newCall(request).execute()

                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            val toxicityScore = parseToxicityScore(responseBody)

                            // Check if the toxicity score exceeds a threshold (e.g., 0.8 for hate speech)
                            if (toxicityScore > 0.2) {
                                hateComments.add(comment)
                            }
                        } else {
                            Log.e("PerspectiveAPI", "API Error: ${response.code}")
                        }
                    }
                }

                // Update UI with hate speech results
                runOnUiThread {
                    if (hateComments.isNotEmpty()) {
                        currentDialog.dismiss()
                        showAlert("Hate Speech Detected", "Detected Hate Comments: \n\n" + hateComments.joinToString("\n"))
                    } else {
                        showAlert("No Hate Speech", "No hate comments found.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PerspectiveAPI", "Exception occurred: ${e.message}")
                runOnUiThread {
                    showAlert("Error", "An error occurred during the detection process.")
                }
            }
        }
    }

    // Function to parse toxicity score from Perspective API response
    private fun parseToxicityScore(json: String?): Double {
        val gson = Gson()
        val responseMap = gson.fromJson(json, Map::class.java)
        val attributes = responseMap["attributeScores"] as Map<*, *>?
        val toxicity = attributes?.get("TOXICITY") as Map<*, *>?
        val summaryScore = toxicity?.get("summaryScore") as Map<*, *>?
        return summaryScore?.get("value").toString().toDouble()
    }






    // Helper function to show alerts
    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
