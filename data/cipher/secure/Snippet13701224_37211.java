FileOutputStream fos = new FileOutputStream(outFilePath);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        //byte[] b= key.getBytes(Charset.forName("UTF-8"));
        byte[] b= key.getBytes("UTF-8");
        Log.i("b",""+b);
        int len= b.length;
        Log.i("len",""+len);
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

        byte[] results = new byte[cipher.getOutputSize(abc.length)];

        try
        {
            Log.i("output size:", ""+cipher.getOutputSize(abc.length));
            ***results = cipher.doFinal(abc);***
        }
        catch (Exception e) {
            // TODO: handle exception
            Log.e("EXCEPTION:", e.getMessage());
        }
        fos.write(results);
