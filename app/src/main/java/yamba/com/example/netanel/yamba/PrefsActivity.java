package yamba.com.example.netanel.yamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import yamba.com.example.netanel.yamba.R;

public class PrefsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

    }
}
