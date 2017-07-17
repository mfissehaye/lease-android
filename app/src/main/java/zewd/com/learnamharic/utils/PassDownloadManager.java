package zewd.com.learnamharic.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import zewd.com.learnamharic.adapter.Constants;

public class PassDownloadManager {

    private android.app.DownloadManager dm;

    private String examId;

    private Context context;

    private BroadcastReceiver receiver;

    public PassDownloadManager(Context context, String examId, BroadcastReceiver receiver) {
        this.examId = examId;
        this.context = context;
        this.receiver = receiver;
    }

    public void download() {

        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        String fileName = examId + ".zip";

        String passDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "pass";

        if(!new File(passDownloads).exists()) {

            new File(passDownloads).mkdirs();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Constants.BASE_URL + fileName));

        request.setTitle(fileName);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "pass" + File.separator + fileName);

        final long enqueue = dm.enqueue(request);

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
