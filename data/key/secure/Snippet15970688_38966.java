randomKey = randomKey.substring(0, 16);
        keyBytes = randomKey.getBytes();
        key = new SecretKeySpec(keyBytes, "AES");
        paramSpec = new IvParameterSpec(iv);
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        in = new FileInputStream(srcFile);
        out = new FileOutputStream(encryptedFile);
        out = new CipherOutputStream(out, ecipher);
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        in.close();
        out.flush();
        out.close();
