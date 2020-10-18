package com.pluu.webtoon.setting.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.pluu.webtoon.data.pref.PrefConfig
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.setting.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.commit {
            replace(android.R.id.content,
                GeneralPreferenceFragment()
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    @AndroidEntryPoint
    class GeneralPreferenceFragment : PreferenceFragmentCompat() {

        @Inject
        lateinit var pref: SharedPreferences

        @Inject
        lateinit var navigator: SettingNavigator

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_settings)
            setHasOptionsMenu(true)
            bindPreferenceSummaryToValue(findPreference(PrefConfig.KEY_DEFAULT_WEBTOON))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == android.R.id.home) {
                activity?.finish()
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        @ExperimentalCoroutinesApi
        @ObsoleteCoroutinesApi
        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (LicenseActivity::class.java.canonicalName == preference?.fragment) {
                navigator.openLicense(requireContext())
                return true
            }
            return super.onPreferenceTreeClick(preference)
        }

        private val changeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            when (preference) {
                is ListPreference -> {
                    // For list preferences, look up the correct display url in
                    // the preference's 'entries' list.
                    val index = preference.findIndexOfValue(newValue.toString())

                    // Set the summary to reflect the new url.
                    preference.summary = if (index >= 0) preference.entries[index] else null
                }
                else -> preference.summary = newValue.toString()
            }
            true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference?) {
            preference ?: return
            preference.onPreferenceChangeListener = changeListener
            changeListener.onPreferenceChange(
                preference, pref.getString(preference.key, "")
            )
        }
    }
}
