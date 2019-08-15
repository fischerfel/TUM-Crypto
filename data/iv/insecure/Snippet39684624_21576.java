byte[] iv = new byte[16]; // 16 is the block size of AES
if (fis.read(iv) != 16) {
    throw new Exception("Incomplete IV"); // TODO: rename to a different exception
}

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

CipherInputStream cis = new CipherInputStream (fis, cipher);
