SecretKey key = loadKey(); // Deserialize your SecretKey object
try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            FileInputStream fis = new FileInputStream("Your file here");
            BufferedInputStream bis = new BufferedInputStream(fis);
            CipherInputStream cis = new CipherInputStream(bis, cipher);
