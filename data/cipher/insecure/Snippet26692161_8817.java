public class JavaApplication8 {

/**
 * @param args the command line arguments
 */
public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, ClassNotFoundException {
    // TODO code application logic here

    cifrarFicheiro();
    decifrarFicheiro();

}
public static void cifrarFicheiro() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    File t1 = new File("t1.txt");
    FileInputStream Fin= new FileInputStream(t1);
    byte[] texto= new byte[(int)t1.length()];
    Fin.read(texto);
    Fin.close();

    SecretKey key = KeyGenerator.getInstance("DES").generateKey();
    Cipher cifra = Cipher.getInstance("DES/ECB/PKCS5Padding");
    cifra.init(Cipher.ENCRYPT_MODE, key);
    byte[] chave = key.getEncoded();
    byte[] texto_cifrado = cifra.doFinal(texto);

    FileOutputStream fout = new FileOutputStream("t1_t.txt");
    ObjectOutputStream obj = new ObjectOutputStream(fout);
    fout.write(texto_cifrado);
    obj.writeObject(chave);
    obj.close();
    fout.close();

}
public static void decifrarFicheiro() throws FileNotFoundException, IOException, ClassNotFoundException{
    FileInputStream fin = new FileInputStream("t1_t.txt");
    ObjectInputStream obj = new ObjectInputStream(fin);

    SecretKey chave = (javax.crypto.SecretKey)obj.readObject();
    byte []keyCifrada = chave.getEncoded();
    obj.close();

    FileOutputStream fout = new FileOutputStream("t1_chave.txt");
    ObjectOutputStream obj1 = new ObjectOutputStream(fout);
    obj1.writeObject(keyCifrada);
    byte [] text = new byte[fin.available()];
    fin.read(text);

}

}
