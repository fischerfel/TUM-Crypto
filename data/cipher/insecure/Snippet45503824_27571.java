public class CipherClient
{
    public static void main(String[] args) throws Exception 
    {
        // -Generate a DES key.
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        Key key = generator.generateKey();

        // -Store it in a file.
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.xx"));
        out.writeObject(key);
        out.close();

        String message = "The quick brown fox jumps over the lazy dog.";
        System.out.println("Message converted from Bytes = " + new String(message.getBytes()));
        System.out.println("Length = " + message.getBytes().length);

        String host = "localhost";
        int port = 7999;
        Socket s = new Socket(host, port);

        // -Use the key to encrypt the message above and send it over socket s to the server.   
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), cipher);
        cipherOut.write(message.getBytes());
        cipherOut.close();
        s.close();
    }
}
