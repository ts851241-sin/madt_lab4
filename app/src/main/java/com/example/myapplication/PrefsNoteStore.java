package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrefsNoteStore implements com.example.myapplication.NoteStore {

    private final SharedPreferences sp;

    public PrefsNoteStore(Context context) {
        sp = context.getSharedPreferences("notes", Context.MODE_PRIVATE);
    }

    private JSONObject getAll() {
        String json = sp.getString("data", "{}");
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    private void saveAll(JSONObject obj) {
        sp.edit().putString("data", obj.toString()).apply();
    }

    @Override
    public void putNote(String title, String content) {
        JSONObject obj = getAll();
        try {
            obj.put(title, content);
        } catch (JSONException ignored) { }
        saveAll(obj);
    }

    @Override
    public String getNote(String title) {
        JSONObject obj = getAll();
        return obj.optString(title, "");
    }

    @Override
    public void deleteNote(String title) {
        JSONObject obj = getAll();
        obj.remove(title);
        saveAll(obj);
    }

    @Override
    public List<String> listNoteTitles() {
        JSONObject obj = getAll();
        List<String> keys = new ArrayList<>();
        Iterator<String> it = obj.keys();
        while (it.hasNext()) keys.add(it.next());
        return keys;
    }
}
