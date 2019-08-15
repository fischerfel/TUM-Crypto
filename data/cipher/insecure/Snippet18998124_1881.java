static void encryptLast64KB(String inputPath, String outputPath)
throws IOException, NoSuchAlgorithmException,
NoSuchPaddingException, InvalidKeyException {


    File myFile = new File(inputPath);
    FileInputStream fis = new FileInputStream(myFile);

    FileOutputStream fos = new FileOutputStream(outputPath);
    BufferedOutputStream bus = new BufferedOutputStream(fos);

    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
            "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);

    int b = 0;
    byte[] d = new byte[65536];

    int offset = 0;

    byte[] encVal = null;

    while ((b = fis.read(d)) != -1) {

        offset = offset + b;
        Log.d(TAG, "Offset: "+offset);
        Log.d(TAG, "b: "+b);
        if((offset)>=myFile.length())
        {
            Log.d(TAG, "last 64 Kbytes");

            try {
                encVal = cipher.doFinal(d);
                Log.d(TAG, "encVal: "+encVal);
                bus.write(encVal);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
        {
            Log.d(TAG, "rest of the bytes");
            bus.write(d);
        }


        bus.flush();
        bus.close();
        fis.close();
    }


}
