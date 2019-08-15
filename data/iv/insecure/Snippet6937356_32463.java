    try {

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    AssetManager is = this.getAssets();
    InputStream fis = is.open("card2_encrypted.jpg");

    CipherInputStream cis = new CipherInputStream(fis, cipher);
    FileOutputStream fos  = new FileOutputStream(
               new File(Environment.getExternalStorageDirectory(), "card2_decrypted.jpg"));




    byte[] b = new byte[8];
    int i;

    while ((i = cis.read(b)) != -1) {
      fos.write(b, 0, i);
    }
    fos.flush(); fos.close();
    cis.close(); fis.close();   

    }
    catch(Exception e){
        e.fillInStackTrace();
        Log.v("Error","Error "+e);
    }
    }
