String key = "my_own_key_which_should_be_quite_long";
byte[] keyData = key.getBytes();
SecretKeySpec myKey = new SecretKeySpec(keyData, "Blowfish");
Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(Cipher.ENCRYPT_MODE, myKey);
String input = "string to encrypt";
try {
    byte[] encrypted = cipher.doFinal(input.getBytes());
    System.out.println(new String(encrypted));
} catch (Exception ex) {
    ex.printStackTrace();
}
