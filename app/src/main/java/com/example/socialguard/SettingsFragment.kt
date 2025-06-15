package com.example.socialguard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connected platform intent
        val connect_platform = view.findViewById<LinearLayout>(R.id.connectplatformLayout)
        connect_platform.setOnClickListener {
            val intent = Intent(activity, connectedplatformactivity::class.java)
            startActivity(intent)
        }

        // Hate comment intent
        val hatecomment_setting = view.findViewById<LinearLayout>(R.id.hatecommentLayout)
        hatecomment_setting.setOnClickListener {
            val intent = Intent(activity, senstivityactivity::class.java)
            startActivity(intent)
        }

        // Notification preferences intent
        val notification_preferences = view.findViewById<LinearLayout>(R.id.notificationlayout)
        notification_preferences.setOnClickListener {
            val intent = Intent(activity, NotificationpresferencesActivity::class.java)
            startActivity(intent)
        }

        // Feedback intent
        val feedback_setting = view.findViewById<LinearLayout>(R.id.feebacksettingLayout)
        feedback_setting.setOnClickListener {
            val intent = Intent(activity, feedbackActivity::class.java)
            startActivity(intent)
        }

        // Reporting intent
        val reporting_setting = view.findViewById<LinearLayout>(R.id.reportingLayout)
        reporting_setting.setOnClickListener {
            val intent = Intent(activity, ReportingActivity::class.java)
            startActivity(intent)
        }

        // Moderation settings intent
        val moderation_setting = view.findViewById<LinearLayout>(R.id.moderationsettinglayout)
        moderation_setting.setOnClickListener {
            val intent = Intent(activity, ModerationActivity::class.java)
            startActivity(intent)
        }

        // Language settings - Open bottom sheet
        val languageSetting = view.findViewById<LinearLayout>(R.id.languagesettingLayout)
        languageSetting.setOnClickListener {
            val bottomSheet = LanguageBottomSheetFragment()
            bottomSheet.show(childFragmentManager, "LanguageBottomSheet") // Use childFragmentManager here
        }

        // app updates settings - Open bottom sheet
        val appupdates = view.findViewById<LinearLayout>(R.id.appupdateslayout)
        appupdates.setOnClickListener {
            val intent = Intent(activity, AppupdatesActivity::class.java)
            startActivity(intent)
        }

        val profilelayout = view.findViewById<LinearLayout>(R.id.profilelayout)
        profilelayout.setOnClickListener {
            val intent = Intent(activity, ProfilesettingActivity::class.java)
            startActivity(intent)
        }

    }
}
