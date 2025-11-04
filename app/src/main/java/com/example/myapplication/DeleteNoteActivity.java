package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.FileNoteStore;
import com.example.myapplication.NoteStore;
import com.example.myapplication.PrefsNoteStore;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {

    private ListView listToDelete;
    private Button btnConfirm, btnCancel;
    private NoteStore store;
    private ArrayAdapter<String> adapter;
    private final List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listToDelete = findViewById(R.id.listToDelete);
        listToDelete.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btnConfirm = findViewById(R.id.btnConfirmDelete);
        btnCancel = findViewById(R.id.btnCancelDelete);

        String mode = getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getString("storage_mode", "prefs");
        store = "prefs".equals(mode) ? new PrefsNoteStore(this) : new FileNoteStore(this);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, titles);
        listToDelete.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> confirmDelete());
        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        titles.clear();
        titles.addAll(store.listNoteTitles());
        adapter.notifyDataSetChanged();
    }

    private void confirmDelete() {
        int pos = listToDelete.getCheckedItemPosition();
        if (pos == ListView.INVALID_POSITION) {
            Toast.makeText(this, R.string.toast_select_note, Toast.LENGTH_SHORT).show();
            return;
        }
        String key = titles.get(pos);
        store.deleteNote(key);
        Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
