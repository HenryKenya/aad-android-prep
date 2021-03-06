package com.example.notekeeper;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;

public class NoteUploaderJobService extends JobService {
    public static final String EXTRA_DATA_URI = "com.example.notekeeper.extras.DATA_URI";
    private NoteUploader noteUploader;

    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        @SuppressLint("StaticFieldLeak") AsyncTask<JobParameters, Void, Void> task = new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters... backgroundParams) {
                JobParameters jobParameters = backgroundParams[0];
                String stringURI = jobParameters.getExtras().getString(EXTRA_DATA_URI);
                Uri dataURI = Uri.parse(stringURI);
                noteUploader.doUpload(dataURI);
                if (!noteUploader.isCanceled())
                    jobFinished(jobParameters, false); // passing true indicates that task needs to be re-run
                return null;
            }
        };
        noteUploader = new NoteUploader(this);
        task.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        noteUploader.cancel();
        return true;
    }
}
