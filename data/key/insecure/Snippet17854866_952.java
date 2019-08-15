KeyStore ks = KeyStore.getInstance("JCEKS");
ks.load(null, null);
SecretKey skInput = new SecretKeySpec("input".getBytes(), "DESede");
SecretKeyEntry skeInput = new KeyStore.SecretKeyEntry(skInput);
ks.setEntry("input_key", skeInput, new KeyStore.PasswordProtection("banana".toCharArray()));
FileOutputStream fos = new FileOutputStream("my.keystore");
pambks.store(fos, "password".toCharArray());
fos.flush();
fos.close();
