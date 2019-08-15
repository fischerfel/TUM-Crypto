 public byte[] encrypt(String message) throws Exception {

        getResources().getIdentifier("key",
                "raw", getPackageName());
        byte[] bytes = new byte[1024];
        try {
            BufferedInputStream buf = new BufferedInputStream(getResources().openRawResource(
                    getResources().getIdentifier("key",
                            "raw", getPackageName())));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final SecretKey key = new SecretKeySpec(bytes, "DESede/ECB/PKCS5Padding");
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);

        return cipherText;
    }
