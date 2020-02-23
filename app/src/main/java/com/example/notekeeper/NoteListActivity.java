package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    // private ArrayAdapter<NoteInfo> adapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
//        final ListView notesList = findViewById(R.id.list_notes); // make it accessible in anonymous class
//
//        List<NoteInfo> notes = DataManager.getInstance().getNotes();
//
//        adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
//
//        notesList.setAdapter(adapterNotes);
//
//        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                //NoteInfo note = (NoteInfo) notesList.getItemAtPosition(position);
//                intent.putExtra(NoteActivity.NOTE_POSITION, position);
//                startActivity(intent);
//            }
//        });
        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);
        LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerNotes.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        NoteRecyclerAdapter noteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);

        recyclerNotes.setAdapter(noteRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //adapterNotes.notifyDataSetChanged();
    }
}
