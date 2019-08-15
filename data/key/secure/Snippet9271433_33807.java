    try {
                 in = new FileInputStream("/sdcard/Pic 1.txt");
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            File f=new File("/sdcard/Pic 1.txt");

            buf = new byte[(int) f.length()+2];


            try {
                in.read(buf);
            } catch (IOException e) {

                e.printStackTrace();
            }

            try {

                decryptedData = decrypt(key, buf);

                decryptedimage = BitmapFactory.decodeByteArray(
                        decryptedData, 0, decryptedData.length);

                loadedimage.setImageBitmap(decryptedimage);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    });

}

public void Encription(Bitmap bm) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    b = baos.toByteArray();

    try {
        keyStart = ".....".getBytes();
        kgen = KeyGenerator.getInstance("AES");
        sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        skey = kgen.generateKey();
        key = skey.getEncoded();

        encryptedData = encrypt(key, b);

        encryptedimage = BitmapFactory.decodeByteArray(encryptedData, 0,
                encryptedData.length);


         out = new FileOutputStream("/sdcard/"+"Pic "+i+".txt");
         out.write(encryptedData, 0, encryptedData.length);

            out.close();
    } catch (Exception e) {

    }
    i++;
}

private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}
private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    long start=System.currentTimeMillis()/1000l;
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}

02-19 08:27:06.972: W/System.err(12742): javax.crypto.IllegalBlockSizeException: last block incomplete in decryption
