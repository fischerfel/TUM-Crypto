Security.addProvider(new BouncyCastleProvider());

byte[] mKeyData = new byte[16];
byte[] mIv = new byte[8];

SecretKeySpec KS = new SecretKeySpec(mKeyData, "Blowfish");

Cipher cipher = Cipher.getInstance("Blowfish/CBC/ZeroBytePadding");
cipher.init(Cipher.ENCRYPT_MODE, KS, new IvParameterSpec(mIv));
