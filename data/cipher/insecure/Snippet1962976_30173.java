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
 pw.close();
 oos.writeObject(c.getIV());
 oos.close();
