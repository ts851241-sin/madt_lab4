package com.example.myapplication;

import java.util.List;

public interface NoteStore {
    void putNote(String title, String content);
    String getNote(String title);
    void deleteNote(String title);
    List<String> listNoteTitles();
}
