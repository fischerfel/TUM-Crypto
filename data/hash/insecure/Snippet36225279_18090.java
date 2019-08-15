import android.content.Context;
import android.os.AsyncTask;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// you can use FileDescriptor as 
// extends AsyncTask<String, Void, FileDescriptor>

public class AudioStreamWorkerTask extends AsyncTask<String, Void, FileInputStream> {

    private OnCacheCallback callback = null;
    private Context context = null;

    public AudioStreamWorkerTask(Context context, OnCacheCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected FileInputStream doInBackground(String... params) {
        String data = params[0];
        // Application class where i did open DiskLruCache
        DiskLruCache cache = MyApplication.getDiskCache(context);
        if (cache == null)
            return null;
        String key = hashKeyForDisk(data);
        final int DISK_CACHE_INDEX = 0;
        long currentMaxSize = cache.getMaxSize();
        float percentageSize = Math.round((cache.size() * 100.0f) / currentMaxSize);
        if (percentageSize >= 90) // cache size reaches 90%
            cache.setMaxSize(currentMaxSize + (10 * 1024 * 1024)); // increase size to 10MB
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot == null) {
                Log.i(getTag(), "Snapshot is not available downloading...");
                DiskLruCache.Editor editor = cache.edit(key);
                if (editor != null) {
                    if (downloadUrlToStream(data, editor.newOutputStream(DISK_CACHE_INDEX)))
                        editor.commit();
                    else
                        editor.abort();
                }
                snapshot = cache.get(key);
            } else
                Log.i(getTag(), "Snapshot found sending");
            if (snapshot != null)
                return (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(getTag(), "File stream is null");
        return null;
    }

    @Override
    protected void onPostExecute(FileInputStream fileInputStream) {
        super.onPostExecute(fileInputStream);
        if (callback != null) {
            if (fileInputStream != null)
                callback.onSuccess(fileInputStream);
            else
                callback.onError();
        }
        callback = null;
        context = null;
    }

    public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream stream = urlConnection.getInputStream();
            // you can use BufferedInputStream and BufferOuInputStream
            IOUtils.copy(stream, outputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(stream);
            Log.i(getTag(), "Stream closed all done");
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                IOUtils.close(urlConnection);
        }
        return false;
    }

    private String getTag() {
        return getClass().getSimpleName();
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
