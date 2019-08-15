KeyGenerator keygenerator;
try {
    keygenerator = KeyGenerator.getInstance("Blowfish");
    SecretKey secretkey = keygenerator.generateKey();

    String Key = settings.getString("key", "");
    byte[] KeyData = Key.getBytes();
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, KS);

    byte[] encrypted = cipher.doFinal(message.getBytes());
    message = new String(encrypted, "UTF-8");

    System.out.println("encrypt ok");
} catch (Exception e){
    System.out.println("encrypt error");
}
