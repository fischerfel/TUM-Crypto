package com.androidman.pc02.mywebviewapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.commonsware.cwac.security.trust.TrustManagerBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Downloader extends IntentService {
  public static final String ACTION_COMPLETE=
      "com.commonsware.android.downloader.action.COMPLETE";

  public Downloader() {
    super("Downloader");
  }

  @Override
  public void onHandleIntent(Intent i) {
      try {
          File root=
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

          root.mkdirs();

          File output=new File(root, i.getData().getLastPathSegment());

          if (output.exists()) {
              output.delete();
          }

          //---
          HttpsURLConnection conn=null;
          Context mcontext= MySuperAppApplication.getContext();
          TrustManagerBuilder tmm=new TrustManagerBuilder(mcontext)
                  .allowCA("fdesmie.cer")
                  .or()
                  .useDefault();
          SSLContext ssl=SSLContext.getInstance("TLS");

          ssl.init(null, tmm.buildArray(), null);

          //conn=(HttpsURLConnection)new URL(mUri).openConnection();
          URL url=new URL(i.getData().toString());
          conn =
                  (HttpsURLConnection)url.openConnection();

          conn.setSSLSocketFactory(ssl.getSocketFactory());


          //HttpURLConnection c=(HttpURLConnection)url.openConnection();

          FileOutputStream fos=new FileOutputStream(output.getPath());
          BufferedOutputStream out=new BufferedOutputStream(fos);

          try {
              InputStream in=conn.getInputStream();
              byte[] buffer=new byte[8192];
              int len=0;

              while ((len=in.read(buffer)) >= 0) {
                  out.write(buffer, 0, len);
              }

              out.flush();
          }
          finally {
              fos.getFD().sync();
              out.close();
              conn.disconnect();
          }
          Log.d(getClass().getName(), "Exception in download") ;

          LocalBroadcastManager.getInstance(this)
                  .sendBroadcast(new Intent(ACTION_COMPLETE));
      }
      catch (IOException e2) {
          Log.e(getClass().getName(), "Exception in download", e2);
      } catch (CertificateException e) {
          e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
      } catch (KeyStoreException e) {
          e.printStackTrace();
      } catch (KeyManagementException e) {
          e.printStackTrace();
      }
  }
}
