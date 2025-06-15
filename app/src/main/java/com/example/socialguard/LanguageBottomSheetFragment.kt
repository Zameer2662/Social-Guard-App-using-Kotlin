package com.example.socialguard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // List of languages
        val languages = listOf(
            "English", "Spanish", "French", "German", "Chinese",
            "Hindi", "Japanese", "Arabic", "Portuguese", "Russian"
        )

        // Get reference to the ListView
        val languageListView = view.findViewById<ListView>(R.id.languageList)

        // Set up ArrayAdapter
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            languages
        )

        languageListView.adapter = adapter

        // Handle item click
        languageListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            Toast.makeText(requireContext(), "Selected: $selectedLanguage", Toast.LENGTH_SHORT).show()

            // Dismiss the bottom sheet
            dismiss()
        }
    }
}
