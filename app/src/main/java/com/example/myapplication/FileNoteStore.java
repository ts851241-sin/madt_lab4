package com.example.myapplication;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileNoteStore implements com.example.myapplication.NoteStore {

    private final File file;

    public FileNoteStore(Context context) {
        file = new File(context.getFilesDir(), "notes.json");
    }

    private JSONObject readAll() {
        if (!file.exists()) return new JSONObject();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private void writeAll(JSONObject obj) {
        try (FileWriter fw = new FileWriter(file, false)) {
            fw.write(obj.toString());
        } catch (Exception ignored) { }
    }

    @Override
    public void putNote(String title, String content) {
        JSONObject obj = readAll();
        try {
            obj.put(title, content);
        } catch (Exception ignored) { }
        writeAll(obj);
    }

    @Override
    public String getNote(String title) {
        JSONObject obj = readAll();
        return obj.optString(title, "");
    }

    @Override
    public void deleteNote(String title) {
        JSONObject obj = readAll();
        obj.remove(title);
        writeAll(obj);
    }

    @Override
    public List<String> listNoteTitles() {
        JSONObject obj = readAll();
        List<String> keys = new ArrayList<>();
        Iterator<String> it = obj.keys();
        while (it.hasNext()) keys.add(it.next());
        return keys;
    }
}
