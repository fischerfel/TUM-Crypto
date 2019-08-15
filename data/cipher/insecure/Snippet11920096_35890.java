static void Encrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        try {

            FileInputStream fis = new FileInputStream(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/logo.png");
            FileOutputStream fos = new FileOutputStream(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/Encrypted");


            SecretKeySpec aeskeySpec = new SecretKeySpec(
                    "12345678901234567890123456789012".getBytes(), "AES");

            tv.setText(aeskeySpec.getEncoded().toString());
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            int b;

            byte[] d = new byte[8];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);

            }

            cos.flush();
            cos.close();
            fis.close();

        }// try
        catch (Exception e) {
            // TODO: handle exception
            tv.setText("Error :" + e.getMessage()); } }// encrypt
