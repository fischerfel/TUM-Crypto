   ObjectInputStream ois = new ObjectInputStream(new FileInputStream("keyfile"));
    DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey key = skf.generateSecret(ks);

    Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, key);
    CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("ciphertext"), c);
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cos));
    pw.println("Stand and unfold yourself");
    pw.close();
