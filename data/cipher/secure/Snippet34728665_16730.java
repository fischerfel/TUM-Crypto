    Base64 base64Encoder = new Base64();

    // initialize cipher to encrypt
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    // **Step 1: encrypt data with public key**
    byte[] encBytes = cipher.doFinal(VALUE.getBytes("UTF-8"));
    byte[] encryptedData  = base64Encoder.encode(encBytes);
    String encryptedDataString = bytes2String(encryptedData);
    System.out.println("data encrypted: " + encryptedData);

    // **Step 2: sign the encrypted data with private key**
    Signature sig = Signature.getInstance("SHA1WithRSA");
    sig.initSign(privKey);
    sig.update(encryptedData);
    byte[] signData = sig.sign();

    // **Step 3: get hash for sign**
    byte[] signDataEncrypted = base64Encoder.encode(signData);
    String signDataString = bytes2String(signDataEncrypted);
    System.out.println("hash: "+signDataString);
