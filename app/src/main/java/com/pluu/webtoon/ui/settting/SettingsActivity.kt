package com.pluu.webtoon.ui.settting

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.text.TextUtils
import android.view.MenuItem
import com.pluu.webtoon.R
import com.pluu.webtoon.common.PrefConfig

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, GeneralPreferenceFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_settings)
            setHasOptionsMenu(true)
            bindPreferenceSummaryToValue(findPreference(PrefConfig.KEY_DEFAULT_WEBTOON))
        }

        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            if (item?.itemId == android.R.id.home) {
                activity.finish()
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (TextUtils.equals(LicenseActivity::class.java.canonicalName, preference?.fragment)) {
                startActivity(Intent(activity, LicenseActivity::class.java))
                return true
            }
            return super.onPreferenceTreeClick(preference)
        }

        private val changeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            when (preference) {
                is ListPreference -> {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    val index = preference.findIndexOfValue(newValue.toString())

                    // Set the summary to reflect the new value.
                    preference.summary = if (index >= 0) preference.entries[index] else null
                }
                else -> preference.summary = newValue.toString()
            }
            true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = changeListener
            changeListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }
    }
}