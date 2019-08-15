package com.example.playvideo2;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.maxcom.http.LocalSingleHttpServer;
import fr.maxcom.libmedia.Licensing;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;


public class MainActivity extends Activity implements OnCompletionListener 
{
    LocalSingleHttpServer mServer;
    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        Licensing.allow(this);
        playENCVideo(Environment.getExternalStorageDirectory()
                + "/encVideo.mp4");
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

public void playENCVideo(String path) {

    try {
        Cipher decipher = null;

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecretKey skey = kgen.generateKey();

        decipher = Cipher.getInstance("AES");

        decipher.init(Cipher.DECRYPT_MODE, skey);

        mServer = new LocalSingleHttpServer();

        mServer.setCipher(decipher);
        mServer.start();

        path = mServer.getURL(path);

        mVideoView = (VideoView) findViewById(R.id.videoView1);
        mVideoView.setVideoPath(path);

        // mVideoView.setMediaController(new MediaController(this));
        // mVideoView.requestFocus();
        mVideoView.start();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

@Override
public void onCompletion(MediaPlayer mp) {
    // MediaPlayer.OnCompletionListener interface
    mServer.stop();

}

}
