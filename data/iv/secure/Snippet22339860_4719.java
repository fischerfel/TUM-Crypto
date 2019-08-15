public static void main(String[] args) throws Exception {
    // got this example from http://www.java2s.com/Tutorial/Java/0490__Security/UsingCipherInputStream.htm
    write();
    read();
}

public static void write() throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance("DES");
    kg.init(new SecureRandom());
    SecretKey key = kg.generateKey();
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    Class spec = Class.forName("javax.crypto.spec.DESKeySpec");
    DESKeySpec ks = (DESKeySpec) skf.getKeySpec(key, spec);
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("keyfile"));
    oos.writeObject(ks.getKey());

    Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, key);
    CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("ciphertext"), c);
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cos));
    pw.println("Stand and unfold yourself");
    pw.flush();
    pw.close();
    oos.writeObject(c.getIV());
    oos.close();
}

public static void read() throws Exception {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("keyfile"));
    DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey key = skf.generateSecret(ks);

    Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec((byte[]) ois.readObject()));
    CipherInputStream cis = new CipherInputStream(new FileInputStream("ciphertext"), c);
    BufferedReader br = new BufferedReader(new InputStreamReader(cis));
    System.out.println(br.readLine());
}
