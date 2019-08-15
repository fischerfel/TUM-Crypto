public static String encode(String strs,String key_plain, String algorithm) {
byte[] key=key_plain.getBytes();

c=Cipher.getInstance(algorithm);
SecretKeySpec k = new SecretKeySpec(key, algorithm);       
c.init(Cipher.ENCRYPT_MODE, k);
byte[] enc_out=c.doFinal(strs.getBytes());
String output=Arrays.toString(enc_out).replace(" ","").replace("{","").replace("}","");
return output;
}
