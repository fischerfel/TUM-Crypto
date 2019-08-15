String iv = "0000000000000000"; //Ignore security concerns
IvParameterSpec ivspec;
ivspec = new IvParameterSpec(iv.getBytes());

String plaintext = "Top Secret Data";
byte[] encrypted = null;

Cipher cipher;
cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, seckey, ivspec);
encrypted = cipher.doFinal(padString(plaintext).getBytes());

ciphertext = Base64.encodeToString(encrypted, Base64.DEFAULT);

Log.i("Encrypted Base64 Data", ciphertext);
