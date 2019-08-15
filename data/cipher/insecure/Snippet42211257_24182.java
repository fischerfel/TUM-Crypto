ObservableList<Access> userData;
userData = FXCollections.observableArrayList();
...
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key128);
File file = new File("./resources/saves" + username);
file.createNewFile();
CipherOutputStream cipherOut = new CipherOutputStream(
        new BufferedOutputStream(new FileOutputStream(file, true)), cipher);
ObjectOutputStream out = new ObjectOutputStream(cipherOut);

userData.forEach((item) -> {
    try {
        out.writeObject(new SealedObject(item, cipher));
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
});
out.flush();
out.close();
