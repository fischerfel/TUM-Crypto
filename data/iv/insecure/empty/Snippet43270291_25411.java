public static void main(String[] args) {
    ServerSocket welcomeSocket = null;

    // Creates a connectable socket on port 6789
    try {
        welcomeSocket = new ServerSocket(6789);
    } catch (IOException e) {
        e.printStackTrace();
    }
    while(true){
        try{
            double k2, B, A;
            double n = 13;
            double g = 61;
            long y = 7;
            B = (Math.pow(g, y))%n;

            System.out.println("Accepting connections");
            // Accept an incoming connection on the socket server
            Socket connectionSocket = welcomeSocket.accept();
            // Creates a read and write stream for that client
            DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Sends the double to the client
            outToClient.writeDouble(B);
            System.out.println("Sent " + B);
            // Reads the number sent by the Client
            A = inFromClient.readDouble();
            System.out.println("Received " + A);

            // Modifies the number
            k2 = (Math.pow(A, y))%n;
            System.out.println("DES key seed = " + k2);
            byte[] deskeydata = toByteArray(k2);

            // Turns the bytes of the modified number into a DES key spec
            DESKeySpec deskeyspec = new DESKeySpec(deskeydata);

            // Makes a secret key (DES)
            SecretKeyFactory keyF = SecretKeyFactory.getInstance("DES");
            SecretKey keystuff = keyF.generateSecret(deskeyspec);
            System.out.println(keystuff.toString());

            // Gets an incoming string from the client and turns it into binary
            byte[] incomingBytes = new byte[128];
            try{
                inFromClient.readFully(incomingBytes);
            } catch(EOFException eof){
                System.out.println("Finished reading");
            }
            System.out.println(new String(incomingBytes));
            Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // Decrypts the string using the shared secret key
            c.init(Cipher.DECRYPT_MODE, keystuff, new IvParameterSpec(new byte[8]));
            byte[] ct2 = c.doFinal(incomingBytes);

            // Decode it from base 64
            //byte[] decodedBytes = Base64.getDecoder().decode(ct2);

            // Prints the received string
            System.out.println("Received: " + new String(ct2));

            inFromClient.close();
            outToClient.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
