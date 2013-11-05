package com.eucsoft.foodex.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.eucsoft.foodex.Constants;
import com.eucsoft.foodex.callback.TaskCallback;
import com.eucsoft.foodex.db.model.Food;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CreateFoodAndUploadTask extends AsyncTask<Bitmap, Integer, Long> implements BaseTask {

    public static final int TASK_ID = 100;

    private TaskCallback taskCallback;
    private HashMap<String, Object> data;
    private Context ctx;

    public CreateFoodAndUploadTask(TaskCallback taskCallback, Context ctx) {
        this.taskCallback = taskCallback;
        this.ctx = ctx;
    }

    @Override
    protected Long doInBackground(Bitmap... params) {

        Bitmap originalBmp = params[0];

        int size = Math.min(originalBmp.getWidth(), originalBmp.getHeight());
        Bitmap croppedBmp = Bitmap.createBitmap(originalBmp, 0, 0, size, size);

        File file = getOutputMediaFile();
        String imagePath = file.getAbsolutePath();
        try {
            FileOutputStream out = new FileOutputStream(file);
            croppedBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            //TODO: Handle exception
        }

        //scan the image so show up in album
        MediaScannerConnection.scanFile(ctx,
                new String[]{imagePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });


        Food food = new Food();

        return RESULT_OK;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        taskCallback.onTaskResult(TASK_ID, aLong, data);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.ALBUM_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}
