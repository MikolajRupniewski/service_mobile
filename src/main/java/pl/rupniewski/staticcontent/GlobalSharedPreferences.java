package pl.rupniewski.staticcontent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalSharedPreferences {
    private static GlobalSharedPreferences instance;
    private Context context;
    private SharedPreferences sharedPreferences;

    private GlobalSharedPreferences(){}
    public static GlobalSharedPreferences getInstance() {
        if (instance == null)
            instance = new GlobalSharedPreferences();
        return instance;
    }
    public void initialize (Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void writePreference(String key, String value){
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, value);
        e.apply();
    }
    public String readPreference(String key) {
        return sharedPreferences.getString(key, "");
    }
    public void deletePreference(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
