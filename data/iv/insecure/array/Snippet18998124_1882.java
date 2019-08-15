 static byte[] decryptLast64KBytes(String inputPath) throws IOException,
NoSuchAlgorithmException, NoSuchPaddingException,
InvalidKeyException {

        FileInputStream fis = new FileInputStream(inputPath);

        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[] { '3', 'd', '0', 'c', 'd', '7', 'A', '9', '7', 'e', '2', '0', 'b', 'x', 'g', 'y' };
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        try {
            cipher.init(Cipher.DECRYPT_MODE, sks, ivParameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        int b;
        byte[] d = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int count =0;

        int offset = 0;
        while((b = fis.read(d)) != -1) {
            offset = offset + b;
            Log.d(TAG, "Offset: "+offset);
            Log.d(TAG, "b: "+b);
            if((offset)>=fis.available())
            {
                Log.d(TAG, "last 64 Kbytes");
                while((b = cis.read(d, offset, offset+b))!=-1)
                {
                    bos.write(d);
                    offset = offset + b;
                }

            }
            else
            {
                Log.d(TAG, "rest of the bytes");
                bos.write(d);
            }

        }

        byte[] completeBytes = bos.toByteArray();
        cis.close();
        return completeBytes;

}
