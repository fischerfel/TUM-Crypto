// --- we need a key pair to test encryption/decryption
KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(1024); // speedy generation, but not secure anymore
KeyPair kp = kpg.generateKeyPair();
RSAPublicKey pubkey = (RSAPublicKey) kp.getPublic();
RSAPrivateKey privkey = (RSAPrivateKey) kp.getPrivate();

// --- encrypt given algorithm string
Cipher oaepFromAlgo = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
oaepFromAlgo.init(Cipher.ENCRYPT_MODE, pubkey);
byte[] ct = oaepFromAlgo.doFinal("owlstead".getBytes(StandardCharsets.UTF_8));

// --- decrypt given OAEPParameterSpec
Cipher oaepFromInit = Cipher.getInstance("RSA/ECB/OAEPPadding");
OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSpecified.DEFAULT);
oaepFromInit.init(Cipher.DECRYPT_MODE, privkey, oaepParams);
byte[] pt = oaepFromInit.doFinal(ct);
System.out.println(new String(pt, StandardCharsets.UTF_8));
