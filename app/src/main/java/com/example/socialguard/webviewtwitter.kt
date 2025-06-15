package com.example.socialguard

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class webviewtwitter : AppCompatActivity() {


        private lateinit var webView: WebView


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_webviewtwitter)

            webView = findViewById(R.id.webview)


            setupWebView()
            webView.loadUrl("https://twitter.com/")


        }

        private fun setupWebView() {
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    injectIdentifyButtonScript()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    injectIdentifyButtonScript()
                }
            }

            webView.addJavascriptInterface(JavaScriptInterface(), "Android")
        }

        private fun injectIdentifyButtonScript() {
            webView.evaluateJavascript(
                """
            (function() {
                function addIdentifyButton(postElement) {
                    if (!postElement.parentElement.querySelector('.identify-button')) {
                        let identifyButton = document.createElement('button');
                        identifyButton.innerText = 'Identify';
                        identifyButton.style.position = 'absolute';
                        identifyButton.style.backgroundColor = '#00ec5b'; 
                        identifyButton.style.color = 'white'; 
                        identifyButton.style.zIndex = 1000;
                        identifyButton.style.padding = '10px 15px'; 
                        identifyButton.style.margin = '10px';
                        identifyButton.style.borderRadius = '5px'; 
                        identifyButton.classList.add('identify-button');
                        
                        identifyButton.onclick = function() {
                            Android.identifyMedia();
                        };

                        postElement.parentElement.style.position = 'relative';
                        postElement.parentElement.appendChild(identifyButton);
                    }
                }

                function observePosts(selectors) {
                    selectors.forEach(selector => {
                        document.querySelectorAll(selector).forEach(post => {
                            addIdentifyButton(post);
                        });
                    });
                }

                const platformSelectors = {
                    'instagram.com': ['article img', 'article video'],
                    'facebook.com': ['img[src*="scontent"]', 'video'],
                    'linkedin.com': ['.feed-shared-image__image', 'video'],
                    'twitter.com': ['article img', 'article video'],
                };

                const currentHost = window.location.host;
                const selectors = platformSelectors[currentHost] || Object.values(platformSelectors).flat();

                observePosts(selectors);

                const observer = new MutationObserver(mutationsList => {
                    mutationsList.forEach(mutation => {
                        if (mutation.type === 'childList' && mutation.addedNodes.length) {
                            observePosts(selectors);
                        }
                    });
                });

                observer.observe(document.body, { childList: true, subtree: true });
            })();
            """.trimIndent(),
                null
            )
        }

        inner class JavaScriptInterface {
            @JavascriptInterface
            fun identifyMedia() {
                runOnUiThread {
                    AlertDialog.Builder(this@webviewtwitter)
                        .setTitle("Processing")
                        .setMessage("Identifying media from Twitter...")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
    }


