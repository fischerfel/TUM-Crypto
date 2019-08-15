String s="a1d0534e4baf9e670bde8670caee8b87"
String decKey = "R=U!LH$O2B#";
Cipher m_decrypt = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
m_decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decKey.getBytes(),"Blowfish"));
byte[] decrypted = m_decrypt.doFinal(Hex.decodeHex(s.toCharArray()));
