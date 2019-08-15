   ...     

   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
   cipher.init(Cipher.DECRYPT_MODE, secKey, ivSpec);

    File file = new File("D_" + filename); // D_ for decrypted
    FileOutputStream fos = null;
    if (!file.exists()) {
        file.createNewFile();
        fos = new FileOutputStream(file);
    }

    try (FileInputStream fis = new FileInputStream(filename)) {
        do {
            byte[] buf = new byte[4096];
            int n = fis.read(buf);
            if (n < 0) {
                System.out.println("Read error");
                fis.close();
                return false;
            }

            byte[] cipherBuf = cipher.doFinal(buf);  // ERROR HERE
            System.out.println("IS THIS WORKING WTF: " + new String(cipherBuf));
            fos.write(cipherBuf, 0, n);

        } while (fis.available() > 0);
        fis.close();
        fos.close();
        return true;
