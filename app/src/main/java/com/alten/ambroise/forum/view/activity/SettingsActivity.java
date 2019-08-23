package com.alten.ambroise.forum.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.alten.ambroise.forum.R;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.title_activity_settings));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final MultiSelectListPreference mMultiPref = findPreference("carbon_copied");
            mMultiPref.setSummaryProvider(preference -> {
                MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
                StringBuilder builder = new StringBuilder();
                final Set<String> values = multiSelectListPreference.getValues();
                if (values.size() == 0) {
                    return getString(R.string.not_defined);
                }
                for (final CharSequence entry : values) {
                    if (builder.length() != 0) {
                        builder.append(System.lineSeparator());
                    }
                    builder.append(entry);
                }
                return builder.toString();
            });
        }
    }
}