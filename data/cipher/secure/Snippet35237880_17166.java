private byte[] getKeyBytes(final byte[] key) throws Exception {
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
        return keyBytes;
    }

    public Cipher getCipherEncrypt(final byte[] key) throws Exception {
        byte[] keyBytes = getKeyBytes(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher;
    }
public void encrypt() throws Exception {
        Cipher cipher = getCipherEncrypt(key);
        FileOutputStream fos = null;
        CipherOutputStream cos = null;
        FileInputStream fis = null;
        try {
           fis = new FileInputStream(inputFile);
         fos = new FileOutputStream(outputFile);
            cos = new CipherOutputStream(fos, cipher);
            byte[] data = new byte[1024];
            int read = fis.read(data);
            while (read != -1) {
                cos.write(data, 0, read);
                read = fis.read(data);
                System.out.println(new String(data, "UTF-8").trim());
            }
            cos.flush();
        } finally {
            cos.close();
            fos.close();
            fis.close();
        }
    }
