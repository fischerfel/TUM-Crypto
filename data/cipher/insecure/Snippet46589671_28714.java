 try {
    KeyGenerator  kg = KeyGenerator.getInstance("DESede");
    SecretKey key = kg.generateKey();
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream("saves/data.ttg"), cipher));
    oos.writeObject("" + CurrentMoney);
    fos = new FileOutputStream("saves/key.ttg");
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
    DESedeKeySpec keyspec = (DESedeKeySpec) skf.getKeySpec(key, DESedeKeySpec.class);
    fos.write(keyspec.getKey());
    fos.close();
    oos.close();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
