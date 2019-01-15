package de.djuelg.vostore.helper;

import android.content.Context;
import android.content.SharedPreferences;
import de.djuelg.vostore.R;
import java.util.Map;




public class Preferencias {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Preferencias() {
    }

    public Preferencias(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.key_app), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Preferencias(Context context, String nameCache, int typeMode) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(nameCache, typeMode);
        editor = sharedPreferences.edit();
    }

    public boolean savePreferences(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public String getValue(String value) {
        return sharedPreferences.getString(value, "");
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public boolean remove(String value) {
        return sharedPreferences.edit().remove(value).commit();
    }

    public void clearAll() {
        editor.clear().commit();
    }
}
