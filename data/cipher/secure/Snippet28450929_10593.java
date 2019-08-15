    // Generate a one-time key
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    SecretKey oneTimeKey = keyGenerator.generateKey();
    System.out.println("Encrypted bytes length: " + oneTimeKey.getEncoded().length); // prints "Plain bytes length: 16"

    // Retrieve public key from certificate
    FileInputStream fileInputStream = new FileInputStream("D:\\test.cer");
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    X509Certificate certificate = (X509Certificate) factory.generateCertificate(fileInputStream);
    PublicKey publicKey = certificate.getPublicKey();

    // Encrypt one-time key using the public key.
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
    cipherOutputStream.write(oneTimeKey.getEncoded(), 0, oneTimeKey.getEncoded().length);

    // Retrieve the encrypted bytes.
    System.out.println("Encrypted bytes length: " + byteArrayOutputStream.toByteArray().length); // prints "Encrypted bytes length: 0"
    cipherOutputStream.close();
