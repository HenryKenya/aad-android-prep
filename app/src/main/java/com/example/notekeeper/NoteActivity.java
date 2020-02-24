package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();
    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo note;
    private boolean isNewNote;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private Spinner spinnerCourses;
    private boolean isCancelling;
    private int notePostion;
    private NoteActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ViewModelProvider viewModelProvider =
                new ViewModelProvider(getViewModelStore(), ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        viewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if (viewModel.isNewlyCreated && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState);
        }

        viewModel.isNewlyCreated = false;

        spinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<NoteInfo> adapterCourses = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        textNoteTitle = findViewById(R.id.edit_note_title);
        textNoteText = findViewById(R.id.edit_note_text);

        displayNote(spinnerCourses, textNoteTitle, textNoteText);

        Log.d(TAG, "onCreate");
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(note.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(note.getTitle());
        textNoteText.setText(note.getText());
    }

    private void saveOriginalNoteValues() {
        if (isNewNote) {
            return;
        }
        viewModel.originalNoteCourseID = note.getCourse().getCourseId();
        viewModel.originalNoteTitle = note.getTitle();
        viewModel.originalNoteText = note.getText();
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        notePostion = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = notePostion == POSITION_NOT_SET;
        if (isNewNote) {
            createNewNote();
        }
        Log.i(TAG, "note position: " + notePostion);
        note = DataManager.getInstance().getNotes().get(notePostion);
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePostion = dm.createNewNote();
        //note = DataManager.getInstance().getNotes().get(notePostion);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share_email) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            isCancelling = true;
            finish();
        } else if (id == R.id.action_next) {
            moveNext();
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveNext() {
        saveNote(); // first save the note they are moving from
        ++notePostion;
        note = DataManager.getInstance().getNotes().get(notePostion);
        saveOriginalNoteValues(); // save original value of next note in case they opt to cancel
        displayNote(spinnerCourses, textNoteTitle, textNoteText);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String emailSubject = textNoteTitle.getText().toString();
        String emailBody = "Check out what I learned on Pluralsight \"" + course.getTitle() + "\"\n" + textNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);

        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCancelling) {
            Log.i(TAG, "cancelling note at position: " + notePostion);
            if (isNewNote) {
                DataManager.getInstance().removeNote(notePostion);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
        Log.d(TAG, "onPause ");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            viewModel.saveState(outState);
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(viewModel.originalNoteCourseID);
        note.setCourse(course);
        note.setTitle(viewModel.originalNoteTitle);
        note.setText(viewModel.originalNoteText);
    }

    private void saveNote() {
        note.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        note.setTitle(textNoteTitle.getText().toString());
        note.setText(textNoteText.getText().toString());
    }
}
