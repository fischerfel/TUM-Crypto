KeySpec ks = new DESKeySpec(new byte[] {'s','e','c','r','e','t','!','!'});
SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
SecretKey sk = skf.generateSecret(ks);
Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
IvParameterSpec ips = new IvParameterSpec(new byte[] {0,0,0,0,0,0,0,0});
c.init(Cipher.ENCRYPT, sk, ips);
// or
c.init(Cipher.DECRYPT, sk, ips);
