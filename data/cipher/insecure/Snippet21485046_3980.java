//ENCRYPTED  as  jAOtTv22BfkTkVrhTN/RHQ==   
public String encrypt(String username,String code) throws Exception {
     try {
    byte[] keyData = (username).getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] hasil1 = cipher.doFinal(username.getBytes());
byte[] hasil2 = cipher.doFinal(code.getBytes());
return new BASE64Encoder().encode(hasil1);
    } catch (Exception e) { System.out.println(e);
    return null; }
}

//DECRYPT  --doug@gmail.com
public String decrypt(String email,String code) throws Exception {
    try {
    byte[] keyData = (code).getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] hasil = cipher.doFinal(new BASE64Decoder().decodeBuffer(email));
    return new String(hasil);
    } catch (Exception e) {  System.out.println("exaception ="+e);
    return null; }
}
