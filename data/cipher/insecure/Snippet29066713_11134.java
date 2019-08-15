        mServer = new LocalSingleHttpServer();
        c = Cipher.getInstance("AES");

        SecretKey skey = new SecretKeySpec("password".getBytes(),
                "AES");
        c.init(Cipher.DECRYPT_MODE, skey);
        mServer.setCipher(c);
        mServer.start();
        String path = mServer.getURL("asset://video.mp4");
        mVideoView.setMediaController(new MediaController(this)); 
        mVideoView.setOnCompletionListener(this);
        mVideoView.setVideoPath(path);
        mVideoView.start();
