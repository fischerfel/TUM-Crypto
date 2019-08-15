final String strPassPhrase = "EB7CB21AA6FB33D3B1FF14BBE7DB4962"; //min 24 chars

    SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
    SecretKey key = factory.generateSecret(new DESedeKeySpec(strPassPhrase.getBytes()));
    Cipher cipher = Cipher.getInstance("DESede");

    cipher.init(Cipher.DECRYPT_MODE, key);

    String encrypted = "3764b8140ae470bda73f7ebed3c33b0895f70c3497c85f39043345128a4bc3b3";
    String decrypted = new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted)));
    System.out.println("Text Decryted : " + decrypted);
