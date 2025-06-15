package com.example.socialguard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.ImageView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up social media buttons
        setUpSocialMediaButtons(view)
    }

    private fun setUpSocialMediaButtons(view: View) {
        // Instagram button
        view.findViewById<ImageView>(R.id.button_open_instagram).setOnClickListener {
            startActivity(Intent(requireContext(), webviewinstagram::class.java))
        }

        // Facebook button
        view.findViewById<ImageView>(R.id.button_open_facebook).setOnClickListener {
            startActivity(Intent(requireContext(), webviewfacebook::class.java))
        }

        // LinkedIn button
        view.findViewById<ImageView>(R.id.button_open_linkedin).setOnClickListener {
            startActivity(Intent(requireContext(), webviewlinkedin::class.java))
        }

        // TikTok button
        view.findViewById<ImageView>(R.id.button_open_tiktok).setOnClickListener {
            startActivity(Intent(requireContext(), webviewtiktok::class.java))
        }

        // YouTube button
        view.findViewById<ImageView>(R.id.button_open_youtube).setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }

        // Twitter button
        view.findViewById<ImageView>(R.id.button_open_x).setOnClickListener {
            startActivity(Intent(requireContext(), webviewtwitter::class.java))
        }

        view.findViewById<ImageView>(R.id.profileimageView).setOnClickListener(){
            startActivity(Intent(requireContext(),ProfilesettingActivity::class.java))
        }
    }




override fun onResume() {
        super.onResume()
        // Ensure WebView keeps cookies between sessions
        val cookieManager = CookieManager.getInstance()
        cookieManager.flush()
    }
}

