import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.io.File;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class Idea extends Application {
    public static Retrofit mRetrofit;
    public static IdeaService Iservice;


    public static LoginResponceModel loinResponce;
    public static SettingsModel settingModel;

    public static LocationModel location = new LocationModel();

    private static SQLiteDatabase dbase;
    private static String FILE_PATH;

    public static SQLiteDatabase getDataBase() {
        return dbase;
    }

    public static String getFilePath() {
        return FILE_PATH;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "App", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AppConstance.DbConstans.tblLogin);
            Log.i("DB", "Created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public static void deleteAllTables() {
        getDataBase().execSQL("DELETE FROM login");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(AppConstance.APP_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
            Iservice = mRetrofit.create(IdeaService.class);
            MultiDex.install(this);
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbase = dbHelper.getWritableDatabase();

            AppDataService appDataService = new AppDataService();
            loinResponce = appDataService.getLoginDetails();
            settingModel = appDataService.getSettings();

            FILE_PATH = getAppFilePath();
            startService(new Intent(Idea.this, LocationTracker.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String combineFilePath(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    public String getAppFilePath() {

        String dsPath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            dsPath = combineFilePath(Environment
                            .getExternalStorageDirectory().getAbsolutePath(),
                    "android/data/Idea/");
        else
            dsPath = this.getDir(
                    this.getPackageName(), 0).getAbsolutePath();

        new File(dsPath).mkdirs();
        return dsPath;
    }

    private OkHttpClient getOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
