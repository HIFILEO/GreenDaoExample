/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.services;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.leonardis.greendaoexample.R;
import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;
import com.example.leonardis.greendaoexample.model.helpers.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A great way to export sqlite databases
 */
public class CopyDatabaseService extends IntentService {
    private Handler handler = new Handler();
    private static final String TAG = CopyDatabaseService.class.getSimpleName();

    public CopyDatabaseService(String name) {
        super(name);
    }

    public CopyDatabaseService(){
        super("CopyDatabaseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Close the database so we can make a copy
        GreenDaoExampleApplication.closeDatabase();

        //Basic File I/O
        String tmpDatabase = "/data/data/" + this.getApplicationContext().getPackageName() + "/databases/" + DatabaseHelper.DATABASE_NAME;

        boolean success = false;
        FileInputStream in = null;
        FileOutputStream out = null;
        try {

            //Create directory
            File charadesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator +
                    this.getResources().getString(R.string.app_name));
            if (!charadesDirectory.exists()) {
                Log.i(TAG, "creating directory: " + charadesDirectory);
                charadesDirectory.mkdirs();
            }

            in = new FileInputStream(tmpDatabase);
            out = new FileOutputStream(Environment.getExternalStorageDirectory() +
                    File.separator +
                    this.getResources().getString(R.string.app_name) +
                    File.separator +
                    DatabaseHelper.DATABASE_NAME);
            copyAndClose(in, out);

            success = true;
        }catch (FileNotFoundException e){
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        //Toast that file was copied
        handler.post(new FinishedRunnable(success));

    }

    /**
     * A simple runnable class meant to run on MAIN UI to toast the database was copied
     */
    private class FinishedRunnable implements Runnable {

        boolean success;

        /**
         * Default Constructor
         * @param success - true if copy service worked.
         */
        FinishedRunnable(boolean success) {
            this.success = success;
        }

        @Override
        public void run() {
            if (this.success) {
                Toast.makeText(getApplicationContext(), "Database Saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Database Save Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Copy
     * @param in
     * @param out
     * @throws java.io.IOException
     */
    private void copyAndClose(InputStream in, FileOutputStream out) throws IOException {

        try {
            copy(in,out);
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Copy on stream to another.
     * @param in
     * @param out
     * @throws java.io.IOException
     */
    private void copy(InputStream in, OutputStream out) throws IOException {
        final int BUFFER_SIZE = 8 * 1024;
        byte[] b = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}

