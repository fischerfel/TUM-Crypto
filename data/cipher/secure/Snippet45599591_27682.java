byte[] decoded = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

PublicKey pubkey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");
rsa.init(Cipher.ENCRYPT_MODE, pubkey);
String hasil = Base64.encodeToString(rsa.doFinal(text.getBytes("UTF-8")),
                                     Base64.NO_WRAP);
return hasil;
