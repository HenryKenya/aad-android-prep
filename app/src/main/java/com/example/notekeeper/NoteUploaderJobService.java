package com.example.notekeeper;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class NoteUploaderJobService extends JobService {
    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
