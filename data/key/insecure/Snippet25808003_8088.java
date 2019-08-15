String key="MySecretKeyABCDE";
SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
byte[] ivbytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
IvParameterSpec iv = new IvParameterSpec(ivbytes);//need IV in CBC mode

Cipher m_enc_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
m_enc_cipher.init(Cipher.ENCRYPT_MODE, skey, iv);

String content ="Hello";
byte[] contentBArr = content.getBytes();
byte[] block = new byte[4080];
System.arraycopy(contentBArr,0,block,0,contentBArr.length);
byte[] res = m_enc_cipher.doFinal(block,0,block.length);
