package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.FileNoteStore;
import com.example.myapplication.NoteStore;
import com.example.myapplication.PrefsNoteStore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_ADD = 1001;
    private TextView txtStorage;
    private ListView listNotes;
    private Button btnAdd, btnDelete, btnToggle;
    private ArrayAdapter<String> adapter;
    private final List<String> titles = new ArrayList<>();

    private NoteStore store;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStorage = findViewById(R.id.txtStorage);
        listNotes = findViewById(R.id.listNotes);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnToggle = findViewById(R.id.btnToggle);

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String mode = prefs.getString("storage_mode", "prefs");
        store = "prefs".equals(mode) ? new PrefsNoteStore(this) : new FileNoteStore(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listNotes.setAdapter(adapter);
        listNotes.setOnItemClickListener((parent, view, position, id) -> {
            String key = titles.get(position);
            String content = store.getNote(key);
            Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
        });

        btnAdd.setOnClickListener(v -> openAdd());
        btnDelete.setOnClickListener(v -> openDelete());
        btnToggle.setOnClickListener(v -> toggleStorage());

        updateStorageLabel();
    }

    private void openAdd() {
        startActivityForResult(new Intent(this, AddNoteActivity.class), REQ_ADD);
    }

    private void openDelete() {
        startActivity(new Intent(this, DeleteNoteActivity.class));
    }

    private void toggleStorage() {
        String current = (store instanceof PrefsNoteStore) ? "prefs" : "files";
        String next = current.equals("prefs") ? "files" : "prefs";
        prefs.edit().putString("storage_mode", next).apply();
        store = "prefs".equals(next) ? new PrefsNoteStore(this) : new FileNoteStore(this);
        updateStorageLabel();
        refresh();
    }

    private void updateStorageLabel() {
        String label = (store instanceof PrefsNoteStore)
                ? getString(R.string.storage_prefs)
                : getString(R.string.storage_files);
        txtStorage.setText(getString(R.string.storage_now, label));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        titles.clear();
        titles.addAll(store.listNoteTitles());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            openAdd();
            return true;
        } else if (id == R.id.action_delete) {
            openDelete();
            return true;
        } else if (id == R.id.action_toggle_storage) {
            toggleStorage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
