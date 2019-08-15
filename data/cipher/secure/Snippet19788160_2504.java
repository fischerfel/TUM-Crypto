private void startVideo() {

    mVideoView.requestFocus();
    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {              
            finish();                    
        }           
    });

    try{
        SecretKeySpec skey = new SecretKeySpec("XXXXXxxxxxXXXXXX".getBytes(), "AES");     
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey,new IvParameterSpec(new byte[16]));   

        Licensing.allow(this);
        mServer = new LocalSingleHttpServer();        

        mServer.setCipher(cipher);
        mServer.start();           

        String serverPath = mServer.getURL("myvideopath");      
        mVideoView.setVideoPath(serverPath);
        mVideoView.setMediaController(mMediaController);
        mVideoView.start();  

    }catch(Exception e){
        e.printStackTrace();
    }

    mVideoView.setMediaController(null);
}
