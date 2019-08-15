public ArrayList<FootballClub> FootBallInputStream() throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
File file = new File("FootballClub.ser");
fileIn = new FileInputStream(file);

SecretKey key = KeyGenerator.getInstance("AES").generateKey();
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key);

CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
in = new ObjectInputStream(cipherIn);

SealedObject sealed = (SealedObject) in.readObject();

ArrayList<FootballClub> e = (ArrayList<FootballClub>) sealed.getObject(cipher);

in.close();

fileIn.close();

return e;

}
public void FootBallOutputStream(ArrayList<FootballClub> e) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {

File file = new File("FootballClub.ser");
fileOut = new FileOutputStream(file);


SecretKey key = KeyGenerator.getInstance("AES").generateKey();
Cipher cipher = (Cipher.getInstance("AES"));
cipher.init(Cipher.ENCRYPT_MODE, key);
SealedObject sealed = new SealedObject(e, cipher);

CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
out = new ObjectOutputStream(cipherOut);
out.writeObject(sealed);
out.close();
fileOut.close();
}
