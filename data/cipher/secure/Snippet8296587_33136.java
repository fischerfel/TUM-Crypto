File bufferFile = new File(path);
FileInputStream fis   = new FileInputStream(bufferFile);

Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
CipherInputStream cis = new CipherInputStream(fis, cipher);
Bitmap ops = BitmapFactory.decodeStream(cis);
logo.setImageBitmap(ops);
