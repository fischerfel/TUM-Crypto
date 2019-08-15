import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.commonfloor.vtour.R;
import com.commonfloor.vtour.model.BaseResponse;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;

public class UploadService extends IntentService {

    public static final String UPLOAD_STATE_CHANGED_ACTION = "com.readystatesoftware.simpl3r.example.UPLOAD_STATE_CHANGED_ACTION";
    public static final String UPLOAD_CANCELLED_ACTION = "com.readystatesoftware.simpl3r.example.UPLOAD_CANCELLED_ACTION";
    public static final String S3KEY_EXTRA = "s3key";
    public static final String PERCENT_EXTRA = "percent";
    public static final String MSG_EXTRA = "msg";
    private final String ZIP = ".zip";
    static final public String COPA_RESULT = "test";
    private final String basePath = Environment.getExternalStorageDirectory() + "/projects/";
    private LocalBroadcastManager broadcaster = null;
    private static final int NOTIFY_ID_UPLOAD = 1337;
    private AmazonS3Client s3Client;
    private Uploader uploader;
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    public UploadService() {
        super("simpl3r-example-upload");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter f = new IntentFilter();
        f.addAction(UPLOAD_CANCELLED_ACTION);
        //registerReceiver(uploadCancelReceiver, f);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        s3Client = new AmazonS3Client(
                new BasicAWSCredentials("key", "password"));

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Log.i("test", "Service started");

        final ZipManager mZipManager = new ZipManager();
        SharedPreferencesUtils.setIsServiceRunning(getApplicationContext(), true);

        final HashSet<String> data = SharedPreferencesUtils.getPendingTask(getApplicationContext());
       // Log.i("test", data + "pending Item List");
        if (data != null) {
            for (final Iterator<String> i = data.iterator(); i.hasNext(); ) {
                final String folderName = i.next();
                final String filePath = basePath + folderName + "/" + folderName + ZIP;
                mZipManager.zipFiles(basePath + folderName, filePath, new ZipManager.ZipListener() {
                    @Override
                    public void onSuccess() {
                        //Log.v("test", "Zip created successfully");

                        sendResult(folderName, "UPLOADING");
                        if (upload(filePath)) {
                            notifyUpload("s3", folderName + ZIP, folderName, SharedPreferencesUtils.getToken(UploadService.this));
                            i.remove();
                            mZipManager.deleteZipFile(filePath);
                            sendResult(folderName, "UPLOADED");
                            SharedPreferencesUtils.setPendingTask(getApplicationContext(), data);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        //Log.v("test", "Error in Zip Creation " + message);
                    }
                });
            }
            if (data.size() > 0) {
                SharedPreferencesUtils.setPendingTask(getApplicationContext(), data);
            } else {
                SharedPreferencesUtils.setPendingTask(getApplicationContext(), null);
            }
        }

    }


    public void sendResult(String folderName, String status) {
        Intent intent = new Intent(COPA_RESULT);
        if (folderName != null) {
            intent.putExtra("foldername", folderName);
            intent.putExtra("Status", status);
        }
        sendBroadcast(intent);
    }

    private boolean upload(String path) {
        File fileToUpload = new File(path);
        if (!fileToUpload.exists()) {
            //Log.i("test", "file does not exist");
        }
        final String s3ObjectKey = fileToUpload.getName();
        String s3BucketName = "vtimgcapture";

        final String msg = s3ObjectKey + " uploading ";

        // create a new uploader for this file
        uploader = new Uploader(this, s3Client, s3BucketName, s3ObjectKey, fileToUpload);

        // listen for progress updates and broadcast/notify them appropriately
        uploader.setProgressListener(new Uploader.UploadProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent,
                                        long bytesUploaded, int percentUploaded) {
                //Log.i("test", " " + percentUploaded);
                if (percentUploaded != 100) {
                    Notification notification = buildNotification(msg + percentUploaded + " %", percentUploaded);
                    nm.notify(NOTIFY_ID_UPLOAD, notification);
                    broadcastState(s3ObjectKey, percentUploaded, msg);
                } else {
                    nm.cancel(NOTIFY_ID_UPLOAD);
                }
            }
        });

        Notification notification = buildNotification(msg + 0 + " %", 0);
        nm.notify(NOTIFY_ID_UPLOAD, notification);
        broadcastState(s3ObjectKey, 0, msg);

        try {
            String s3Location = uploader.start(); // initiate the upload
            //Log.i("test", "File successfully uploaded to " + s3Location);
            broadcastState(s3ObjectKey, -1, "File successfully uploaded to " + s3Location);
            return true;
        } catch (UploadIterruptedException uie) {
            //Log.i("test", "User Interrupted the upload");
            broadcastState(s3ObjectKey, -1, "User interrupted");
            return false;
        } catch (Exception e) {
            //Log.i("test", "Error in upload " + e.getMessage());
            e.printStackTrace();
            broadcastState(s3ObjectKey, -1, "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void onDestroy() {
        //Log.i("test", "Stopping Service onDestroy");
        SharedPreferencesUtils.setIsServiceRunning(getApplicationContext(), false);
        nm.cancel(NOTIFY_ID_UPLOAD);
//        unregisterReceiver(uploadCancelReceiver);
        super.onDestroy();
    }

    private void broadcastState(String s3key, int percent, String msg) {
        Intent intent = new Intent(UPLOAD_STATE_CHANGED_ACTION);
        Bundle b = new Bundle();
        b.putString(S3KEY_EXTRA, s3key);
        b.putInt(PERCENT_EXTRA, percent);
        b.putString(MSG_EXTRA, msg);
        intent.putExtras(b);
        sendBroadcast(intent);
    }

    private Notification buildNotification(String msg, int progress) {
        builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setOngoing(true);
        builder.setTicker(msg);
        builder.setContentText(msg);
        builder.setProgress(100, progress, false);
        return builder.build();
    }

//    private BroadcastReceiver uploadCancelReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (uploader != null) {
//                uploader.interrupt();
//            }
//        }
//    };

    private String md5(String s) {
        try {
            // create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void notifyUpload(String source, String zip_url, String vt_id, String accessToken) {
        NetworkUtils.notifyUpload(source, zip_url, vt_id, accessToken, new NetworkUtils.NetworkListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.status) {
                   // Log.i("test", "Notification Successful");
                } else {
                    //Log.i("test", "Notification UnSuccessful");
                }
            }

            @Override
            public void onError(String error) {
               // Log.i("test", "Notification Error " + error);
            }
        });
    }

}
