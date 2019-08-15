        cis.close();

        is.close();

        os.close();

    } catch (IOException e) {

        System.out.println("I/O Error:" + e.getMessage());

    }

}

public void run() {
    try {

        SecretKey key = KeyGenerator.getInstance("DES").generateKey();

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);


        dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        decrypt(input, new FileOutputStream("cleartext-reversed.txt"));

        FileWriter out = new FileWriter("test.txt");
        BufferedWriter bufWriter = new BufferedWriter(out);


        System.out.println("receive from : "
                + clientSocket.getInetAddress() + ":"
                + clientSocket.getPort());
        //Step 1 read length
        int nb = input.read();
        System.out.println("Read Length" + nb);

        String enctext = Character.toString(input.readChar());
        Integer.toString(nb);
        //Step 2 read byte

        String st = new String("see if it can write");
        bufWriter.append(enctext);
        bufWriter.close();


        //Step 1 send length
        output.writeInt(st.length());
        //Step 2 send length
        output.writeBytes(st); // UTF is a string encoding
        //  output.writeUTF(data);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    } catch (EOFException e) {
        System.out.println("EOF:" + e.getMessage());
    } catch (IOException e) {
        System.out.println("IO:" + e.getMessage());
    } finally {
        try {
            clientSocket.close();
        } catch (IOException e) {/*close failed*/

        }
    }
}
