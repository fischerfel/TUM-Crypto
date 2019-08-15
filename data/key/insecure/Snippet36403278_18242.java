private void decriptFile() throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        //byte[] docBytes = serialize(myDoc);
        byte[] b = new byte[0];
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.output27);

            b = new byte[in_s.available()];
            in_s.read(b);
            //txtHelp.setText(new String(b));
        } catch (Exception e) {
            // e.printStackTrace();
            //txtHelp.setText("Error: can't show help.");
        }

        //byte[] dataBytes = FileUtils.readFileToByteArray(File file);
        byte[] key = new byte[0];
        try {
           // key = ("HR$2pIjHR$2pIj12").getBytes("UTF-8");
            key = ("HR$2pIjHR$2pIj12").getBytes("UTF-8");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            IvParameterSpec iv = new IvParameterSpec(key);
            c.init(Cipher.DECRYPT_MODE, k, iv);

            // IllegalBlockSizeException Occurred

            //File folder = new File(Environment.getExternalStorageDirectory(),
                    //"test");
            File folder = new File("/sdcard",
                    "test");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] decryptedDocBytes = c.doFinal(b);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(folder.getPath()+"/test.epub"));
            bos.write(decryptedDocBytes);
            bos.flush();
            bos.close();
            //DocumentsContract.Document decryptedDoc = (DocumentsContract.Document)deserialize(decryptedDocBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //IvParameterSpec iv = new IvParameterSpec(key);



        //And my serialize/deserialize methods:
    }
