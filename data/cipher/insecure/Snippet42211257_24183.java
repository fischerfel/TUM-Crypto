ObservableList<Access> access = FXCollections.observableArrayList();

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key128);

CipherInputStream cipherIn = new CipherInputStream(
        new BufferedInputStream(new FileInputStream("./resources/saves" + username)), cipher);
ObjectInputStream in = new ObjectInputStream(cipherIn);
SealedObject sealed;
while ((sealed = (SealedObject) in.readObject()) != null) {
    access.add((Access) sealed.getObject(cipher));
}
