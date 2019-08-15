private static final byte[] RAW = "icanplayWillBeth".getBytes();
KeyGenerator kgen = KeyGenerator.getInstance("AES");
kgen.init(128);
SecretKeySpec skeySpec = new SecretKeySpec(RAW,"AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE,skeySpec);`
byte[] encrypted = cipher.doFinal(XXXXX);
