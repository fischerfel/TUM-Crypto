public void saveProfiles() {
    try {
        if (key == null) {
            createKey();
            iv = generateIv();
            ivSpec = new IvParameterSpec(iv);
        }
        cipher.init(Cipher.ENCRYPT_MODE, saltedKey, ivSpec);
        FileOutputStream fos = openFileOutput("connProfiles.bin", Context.MODE_PRIVATE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(connectionProfiles);
        oos.flush();
        oos.close();
        FileOutputStream keyOutputStream = openFileOutput("key.bin", Context.MODE_PRIVATE);
        keyOutputStream.write(key.getEncoded());
        keyOutputStream.flush();
        keyOutputStream.close();
        FileOutputStream ivOutputStream = openFileOutput("iv.bin", Context.MODE_PRIVATE);
        ivOutputStream.write(iv);
        ivOutputStream.flush();
        ivOutputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
