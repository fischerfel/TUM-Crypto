public void playENCVideo(String path) {
    try {
        Cipher decipher = null;

        KeyGenerator kgen = KeyGenerator.getInstance("AES");


        byte[] key = AppUtiles.generateKey("qwertyuiopasdfgh");
        SecretKeySpec skey = new SecretKeySpec(key, "AES");

        decipher = Cipher.getInstance("AES");

        decipher.init(Cipher.DECRYPT_MODE, skey);

        mServer = new LocalSingleHttpServer();

        mServer.setCipher(decipher);
        mServer.start();

        path = mServer.getURL(path);

        vvPlayer.setVideoPath(path);
        vvPlayer.start();
    } catch (InvalidKeyException e) {
        Log.d(TAG, "InvalidKeyException  ");
    } catch (NoSuchAlgorithmException e) {
        Log.d(TAG, "NoSuchAlgorithmException  ");

    } catch (NoSuchPaddingException e) {
        Log.d(TAG, "NoSuchPaddingException  ");

    } catch (IOException e) {
        Log.d(TAG, "IOEXCEPTION   ");

    }
    catch (Exception e) {
            Log.d(TAG,"Genral exceptin");
    }
}
