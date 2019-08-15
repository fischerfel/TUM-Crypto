public class MyClassActivity extends Activity {

VideoView mVideoView; 
LocalSingleHttpServer mServer ;

@Override 
protected void onCreate(Bundle savedInstanceState) { 
  super.onCreate(savedInstanceState);

  SecretKey sk = encryptVideo("/mnt/sdcard/input.mp4", "/mnt/sdcard/enc.mp4");

  playENCVideo(sk,"/mnt/sdcard/enc.mp4");
}

public void playENCVideo(SecretKey skey, String path) {

   Cipher decipher = null;

   decipher = Cipher.getInstance("AES");

   decipher.init(Cipher.DECRYPT_MODE, skey);

   mServer = new LocalSingleHttpServer();

   mServer.setCipher(decipher);
   mServer.start();

   path = mServer.getURL(path);

  mVideoView = (VideoView) findViewById(R.id.vid_view);
  mVideoView.setVideoPath(path);
  mVideoView.setOnPreparedListener(this);
  mVideoView.setOnCompletionListener(this);
  mVideoView.setMediaController(new MediaController(this));
  mVideoView.start();
} //playENCVideo()

encryptVideo() {

}

decryptVideo() {

}
} //MyClassActivity
