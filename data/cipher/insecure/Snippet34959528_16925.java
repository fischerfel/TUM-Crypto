try {
        decrypt(list.image_path);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
        e.printStackTrace();
    }

    Picasso.with(context).load(new File(list.image_path)).error(R.drawable.logo).placeholder(R.drawable.logo)
            .into(holder.lock_image);

 public void decrypt(String image) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }; //Choose a key wisely
    FileInputStream fis = new FileInputStream(image);

    FileOutputStream fos = new FileOutputStream(image);
    SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);

    fos.flush();
    fos.close();
    cis.close();
}
