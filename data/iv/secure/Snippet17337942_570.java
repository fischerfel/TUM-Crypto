public void setPassword(String password) throws UnsupportedEncodingException {
    this.password = password.getBytes("UTF-16LE");
    debug("Using password: ", this.password);
}

public void encrypt(int version, String fromPath, String toPath) throws IOException, GeneralSecurityException {
    InputStream in = null;
    OutputStream out = null;
    try {
        in = new BufferedInputStream(new FileInputStream(fromPath));
        debug("Opened for reading: " + fromPath);
        out = new BufferedOutputStream(new FileOutputStream(toPath));
        debug("Opened for writing: " + toPath);

        encrypt(version, in, out);
    } finally {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}

public void encrypt(int version, InputStream in, OutputStream out) throws IOException, GeneralSecurityException {
    try {
        byte[] text = null;

        ivSpec1 = new IvParameterSpec(generateIv1());
        aesKey1 = new SecretKeySpec(generateAESKey1(ivSpec1.getIV(), password), CRYPT_ALG);
        ivSpec2 = new IvParameterSpec(generateIV2());
        aesKey2 = new SecretKeySpec(generateAESKey2(), CRYPT_ALG);
        debug("IV1: ", ivSpec1.getIV());
        debug("AES1: ", aesKey1.getEncoded());
        debug("IV2: ", ivSpec2.getIV());
        debug("AES2: ", aesKey2.getEncoded());

        out.write("AES".getBytes("UTF-8")); // Heading.
        out.write(version); // Version.
        out.write(0); // Reserved.
        if (version == 2) { // No extensions.
            out.write(0);
            out.write(0);
        }
        out.write(ivSpec1.getIV()); // Initialization Vector.

        text = new byte[BLOCK_SIZE + KEY_SIZE];
        cipher.init(Cipher.ENCRYPT_MODE, aesKey1, ivSpec1);
        cipher.update(ivSpec2.getIV(), 0, BLOCK_SIZE, text);
        cipher.doFinal(aesKey2.getEncoded(), 0, KEY_SIZE, text, BLOCK_SIZE);
        out.write(text); // Crypted IV and key.
        debug("IV2 + AES2 ciphertext: ", text);

        hmac.init(new SecretKeySpec(aesKey1.getEncoded(), HMAC_ALG));
        text = hmac.doFinal(text);
        out.write(text); // HMAC from previous cyphertext.
        debug("HMAC1: ", text);

        cipher.init(Cipher.ENCRYPT_MODE, aesKey2, ivSpec2);
        hmac.init(new SecretKeySpec(aesKey2.getEncoded(), HMAC_ALG));
        text = new byte[BLOCK_SIZE];
        int len, last = 0;
        while ((len = in.read(text)) > 0) {
            cipher.update(text, 0, BLOCK_SIZE, text);
            hmac.update(text);
            out.write(text); // Crypted file data block.
            last = len;
        }
        last &= 0x0f;
        out.write(last); // Last block size mod 16.
        debug("Last block size mod 16: " + last);

        text = hmac.doFinal();
        out.write(text); // HMAC from previous cyphertext.
        debug("HMAC2: ", text);
    } catch (InvalidKeyException e) {
        throw new GeneralSecurityException(JCE_EXCEPTION_MESSAGE, e);
    }
}

public void decrypt(String fromPath, String toPath) throws IOException, GeneralSecurityException {
    InputStream in = null;
    OutputStream out = null;
    try {
        in = new BufferedInputStream(new FileInputStream(fromPath));
        debug("Opened for reading: " + fromPath);
        out = new BufferedOutputStream(new FileOutputStream(toPath));
        debug("Opened for writing: " + toPath);

        decrypt(new File(fromPath).length(), in, out);
    } finally {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}

public void decrypt(long inSize, InputStream in, OutputStream out) throws IOException, GeneralSecurityException {
    try {
        byte[] text = null, backup = null;
        long total = 3 + 1 + 1 + BLOCK_SIZE + BLOCK_SIZE + KEY_SIZE + SHA_SIZE + 1 + SHA_SIZE;
        int version;

        text = new byte[3];
        readBytes(in, text); // Heading.
        if (!new String(text, "UTF-8").equals("AES")) {
            throw new IOException("Invalid file header");
        }

        version = in.read(); // Version.
        if (version < 1 || version > 2) {
            throw new IOException("Unsupported version number: " + version);
        }
        debug("Version: " + version);

        in.read(); // Reserved.

        if (version == 2) { // Extensions.
            text = new byte[2];
            int len;
            do {
                readBytes(in, text);
                len = ((0xff & (int) text[0]) << 8) | (0xff & (int) text[1]);
                if (in.skip(len) != len) {
                    throw new IOException("Unexpected end of extension");
                }
                total += 2 + len;
                debug("Skipped extension sized: " + len);
            } while (len != 0);
        }

        text = new byte[BLOCK_SIZE];
        readBytes(in, text); // Initialization Vector.
        ivSpec1 = new IvParameterSpec(text);
        aesKey1 = new SecretKeySpec(generateAESKey1(ivSpec1.getIV(), password), CRYPT_ALG);
        debug("IV1: ", ivSpec1.getIV());
        debug("AES1: ", aesKey1.getEncoded());

        cipher.init(Cipher.DECRYPT_MODE, aesKey1, ivSpec1);
        backup = new byte[BLOCK_SIZE + KEY_SIZE];
        readBytes(in, backup); // IV and key to decrypt file contents.
        debug("IV2 + AES2 ciphertext: ", backup);
        text = cipher.doFinal(backup);
        ivSpec2 = new IvParameterSpec(text, 0, BLOCK_SIZE);
        aesKey2 = new SecretKeySpec(text, BLOCK_SIZE, KEY_SIZE, CRYPT_ALG);
        debug("IV2: ", ivSpec2.getIV());
        debug("AES2: ", aesKey2.getEncoded());

        hmac.init(new SecretKeySpec(aesKey1.getEncoded(), HMAC_ALG));
        backup = hmac.doFinal(backup);
        text = new byte[SHA_SIZE];
        readBytes(in, text); // HMAC and authenticity test.
        if (!Arrays.equals(backup, text)) {
            throw new IOException("Message has been altered or password incorrect");
        }
        debug("HMAC1: ", text);

        total = inSize - total; // Payload size.
        if (total % BLOCK_SIZE != 0) {
            throw new IOException("Input file is corrupt");
        }
        if (total == 0) { // Hack: empty files won't enter block-processing
                          // for-loop below.
            in.read(); // Skip last block size mod 16.
        }
        debug("Payload size: " + total);

        cipher.init(Cipher.DECRYPT_MODE, aesKey2, ivSpec2);
        hmac.init(new SecretKeySpec(aesKey2.getEncoded(), HMAC_ALG));
        backup = new byte[BLOCK_SIZE];
        text = new byte[BLOCK_SIZE];
        for (int block = (int) (total / BLOCK_SIZE); block > 0; block--) {
            int len = BLOCK_SIZE;
            if (in.read(backup, 0, len) != len) { // Cyphertext block.
                throw new IOException("Unexpected end of file contents");
            }
            cipher.update(backup, 0, len, text);
            hmac.update(backup, 0, len);
            if (block == 1) {
                int last = in.read(); // Last block size mod 16.
                debug("Last block size mod 16: " + last);
                len = (last > 0 ? last : BLOCK_SIZE);
            }
            out.write(text, 0, len);
        }
        out.write(cipher.doFinal());

        backup = hmac.doFinal();
        text = new byte[SHA_SIZE];
        readBytes(in, text); // HMAC and authenticity test.
        if (!Arrays.equals(backup, text)) {
            throw new IOException("Message has been altered or password incorrect");
        }
        debug("HMAC2: ", text);
    } catch (InvalidKeyException e) {
        throw new GeneralSecurityException(JCE_EXCEPTION_MESSAGE, e);
    }
}
