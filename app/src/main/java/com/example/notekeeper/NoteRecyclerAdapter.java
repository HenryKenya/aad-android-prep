package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context context;
    //private final List<NoteInfo> notes;
    private Cursor mCursor;
    private final LayoutInflater inflater;
    private int coursePos;
    private int noteTitlePos;
    private int idPos;

    public NoteRecyclerAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.mCursor = cursor;
        inflater = LayoutInflater.from(this.context);
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor == null)
            return;
        coursePos = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        noteTitlePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        idPos = mCursor.getColumnIndex(NoteInfoEntry._ID);
    }

    public void changeCursor(Cursor cursor) {
        if (mCursor != null)
            mCursor.close();
        this.mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // move mCursor to correct position
        String course = mCursor.getString(coursePos);
        String noteTitle = mCursor.getString(noteTitlePos);
        int id = mCursor.getInt(idPos);

        //NoteInfo note = notes.get(position);
        holder.textCourse.setText(course);
        holder.textTitle.setText(noteTitle);
        holder.id = id;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTitle;
        public int id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
