    ByteBuffer bb = ByteBuffer.allocate(16);
    bb.put("1234567891230000".getBytes());
    byte[] ivString = bb.array();

    // INITIALISATION
    String keyString = "1234567812345678";
    IvParameterSpec iv = new IvParameterSpec(ivString);
    SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "AES");

    // FOR ENCRYPTION
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivString));
    Inputstream encrypted_is = new CipherInputStream(in, cipher);

    // FOR DECRYPTION
    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivString));
            Inputstream decrypted_is = new CipherInputStream(in, cipher);
