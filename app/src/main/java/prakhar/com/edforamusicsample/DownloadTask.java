package prakhar.com.edforamusicsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lendingkart on 3/19/2017.
 */
public abstract class DownloadTask extends AsyncTask<String, Void, Void> {
    private NotificationHelper mNotificationHelper;

    private static final String PEFERENCE_FILE = "preference";
    private static final String ISDOWNLOADED = "isdownloaded";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context context;

    public DownloadTask(Context context) {
        this.context = context;
        mNotificationHelper = new NotificationHelper(context);
    }

    protected void onPreExecute() {
        //Create the notification in the statusbar
        mNotificationHelper.createNotification();
    }

    @Override
    protected Void doInBackground(String urllink) {
        //This is where we would do the actual download stuff
        //for now I'm just going to loop for 10 seconds
        // publishing progress every second

        int count;

        try {


            URL url = new URL(url);
            URLConnection connexion = url.openConnection();
            connexion.connect();

            int lenghtOfFile = connexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            //OutputStream output = new FileOutputStream("/sdcard/foldername/temp.zip");
            OutputStream output = new FileOutputStream("/sdcard/foldername/himages.zip");
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                //publishProgress(""+(int)((total*100)/lenghtOfFile));
                Log.d("%Percentage%", "" + (int) ((total * 100) / lenghtOfFile));
                onProgressUpdate((int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/foldername/" + "_images.zip");
            File path = new File(Environment.getExternalStorageDirectory()
                    + "/foldername");
            try {
                //ZipUtil.unzip(file, path);
                settings = this.context.getSharedPreferences(PEFERENCE_FILE, 0);
                editor = settings.edit();
                editor.putBoolean(ISDOWNLOADED, true);
                editor.commit();

            } catch (Exception e) {
                Log.d("ZIP UTILL", e.toString());
            }

        } catch (Exception e) {
        }


        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
        mNotificationHelper.progressUpdate(progress[0]);
    }

    protected void onPostExecute(Void result) {
        //The task is complete, tell the status bar about it

        mNotificationHelper.completed();
    }
}