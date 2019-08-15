    mVideoView.requestFocus();
    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            finish();
        }
    });

    try{
        byte k[] = "SomeKey".getBytes();
        SecretKeySpec skey = new SecretKeySpec(k,"DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skey);

        Licensing.allow(this);
        mServer = new LocalSingleHttpServer();

        mServer.setCipher(cipher);
        mServer.start();

        String serverPath = mServer.getURL(path);
        mVideoView.setVideoPath(serverPath);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.start();

    }catch(Exception e){
        Toast.makeText(this,e.getMessage()+" sometext",Toast.LENGTH_LONG).show();
        Log.e("e.getMessage()",e.getMessage());
        e.printStackTrace();
    }

    mVideoView.setMediaController(null);
