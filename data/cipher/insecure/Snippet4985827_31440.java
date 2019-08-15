SecretKeyFactory sf = SecretKeyFactory.getInstance("DES");
byte[] in = "test".getBytes("UTF-8");

Cipher c1 = Cipher.getInstance("DES");
c1.init(Cipher.ENCRYPT_MODE, sf.generateSecret(new DESKeySpec(
   new byte[] {0x10,0x20,0x30,0x40,0x50,0x60,0x70,(byte) 0x80})));
byte[] r1 = c1.doFinal(in);

Cipher c2 = Cipher.getInstance("DES");
c2.init(Cipher.ENCRYPT_MODE, sf.generateSecret(new DESKeySpec(
    new byte[] {0x11,0x21,0x31,0x41,0x51,0x61,0x71,(byte) 0x81})));
byte[] r2 = c2.doFinal(in);

assertArrayEquals(r1, r2);  
