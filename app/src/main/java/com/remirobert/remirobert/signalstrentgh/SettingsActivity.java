package com.remirobert.remirobert.signalstrentgh;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            switch (preference.getKey()) {
                case "time_interval": {
                    String stringValue = value.toString();
                    if (Long.parseLong(stringValue) <= 0)
                        return false;
                    preference.setSummary(stringValue + "s");
                    return true;
                }
                default:
                    return true;
            }
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #onPreferenceChangeListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SignalStrenght signalStrenght = (SignalStrenght) getApplication();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(signalStrenght.onSharedPreferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SignalStrenght signalStrenght = (SignalStrenght) getApplication();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(signalStrenght.onSharedPreferenceChangeListener);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("time_interval"));
            addModeToModeList(findPreference("add_mode"), findPreference("modes"));
            deleteModeFromModeList(findPreference("delete_mode"), findPreference("modes"));
        }

        private void deleteModeFromModeList(Preference delete_mode, final Preference modes) {
            delete_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String mode = value.toString();
                    ListPreference list = (ListPreference) modes;
                    CharSequence[] entries = list.getEntries();
                    CharSequence[] entryValues = list.getEntryValues();
                    CharSequence[] newEntries = deleteEntry(entries, mode);
                    CharSequence[] newEntryValues = deleteEntryValue(entries, entryValues, mode);
                    list.setEntries(newEntries);
                    list.setEntryValues(newEntryValues);
                    return true;
                }
            });
        }

        private CharSequence[] deleteEntryValue(CharSequence[] entries, CharSequence[] entryValues, String mode) {
            mode = mode.toLowerCase();
            boolean exist = false;
            for (CharSequence entry : entries) {
                if (entry.toString().toLowerCase().equals(mode)) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                return entryValues;
            int l = entries.length, k = 0;
            for (int i = 0; i < l; i++) {
                if (entries[i].toString().toLowerCase().equals(mode)) {
                    k = i;
                    break;
                }
            }
            CharSequence[] newEntryValues = new CharSequence[l - 1];
            for (int i = 0, j = 0; i < l; i++) {
                if (i != k) {
                    newEntryValues[j++] = entryValues[i];
                }
            }
            return newEntryValues;
        }


        private CharSequence[] deleteEntry(CharSequence[] entries, String mode) {
            mode = mode.toLowerCase();
            boolean exist = false;
            for (CharSequence entry : entries) {
                if (entry.toString().toLowerCase().equals(mode)) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                return entries;
            int l = entries.length;
            CharSequence[] newEntries = new CharSequence[l - 1];
            for (int i = 0, j = 0; i < l; i++) {
                if (!entries[i].toString().toLowerCase().equals(mode)) {
                    newEntries[j++] = entries[i];
                }
            }
            return newEntries;
        }

        private void addModeToModeList(final Preference add_mode, final Preference modes) {
            add_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newMode = newValue.toString();
                    if (newMode.equals(""))
                        return true;
                    ListPreference list = (ListPreference) modes;
                    CharSequence[] entries = list.getEntries();
                    CharSequence[] entryValues = list.getEntryValues();
                    CharSequence[] newEntries = addEntry(entries, newMode);
                    CharSequence[] newEntryValues = addEntryValue(entryValues);
                    list.setEntries(newEntries);
                    list.setEntryValues(newEntryValues);
                    return true;
                }
            });
        }

        private CharSequence[] addEntryValue(CharSequence[] entryValues) {
            int l = entryValues.length;
            CharSequence[] newEntryValues = new CharSequence[l + 1];
            System.arraycopy(entryValues, 0, newEntryValues, 0, l);
            int max = Integer.parseInt(entryValues[0].toString());
            for (int i = 1; i < l; i++) {
                int t = Integer.parseInt(entryValues[i].toString());
                if (t > max) {
                    max = t;
                }
            }
            newEntryValues[l] = Integer.toString(max + 1);
            return newEntryValues;
        }

        private CharSequence[] addEntry(CharSequence[] entries, String newMode) {
            int l = entries.length;
            CharSequence[] newEntries = new CharSequence[l + 1];
            System.arraycopy(entries, 0, newEntries, 0, l);
            newEntries[l] = newMode;
            return newEntries;
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
