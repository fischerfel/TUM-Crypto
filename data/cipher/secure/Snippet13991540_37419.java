    File file = new File("C:\\temp-ldi\\pubkey.txt");
    FileWriter writer = new FileWriter(file);
    file.createNewFile();
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, new SecureRandom());
    KeyPair keyPayr = generator.generateKeyPair();
    writer.write(new BASE64Encoder().encode(keyPayr.getPublic().getEncoded()));
    writer.flush();
    writer.close();
    file = new File("C:\\temp-ldi\\privkey.txt");
    writer = new FileWriter(file);
    file.createNewFile();
    writer.write(new BASE64Encoder().encode(keyPayr.getPrivate().getEncoded()));
    writer.flush();
    writer.close();


    File privfile = new File("C:\\temp-ldi\\privkey.txt");
    File pubfile = new File("C:\\temp-ldi\\pubkey.txt");
    FileReader reader = new FileReader(pubfile);

    BufferedReader br = new BufferedReader(reader);
    StringBuilder sb = new StringBuilder();
    String s;
    while ((s = br.readLine()) != null) {
        sb.append(s);
    }
    br.close();
    reader.close();
    PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(sb.toString())));
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    String encryptedText = new BASE64Encoder().encode(cipher.doFinal("PLAIN TEXT".getBytes("UTF-8")));
    System.out.println("encrypted: " + encryptedText);
    reader = new FileReader(privfile);
    br = new BufferedReader(reader);
    sb = new StringBuilder();
    while ((s = br.readLine()) != null) {
        sb.append(s);
    }
    br.close();
    reader.close();
    PrivateKey privateKey =  KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(sb.toString())));
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    System.out.println( new String(cipher.doFinal (new BASE64Decoder().decodeBuffer(encryptedText))));
