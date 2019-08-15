public static void main(String[] args) {

    // Creates a socket to the local host on port 6789
    Socket clientSocket = null;
    try {
        clientSocket = new Socket("localhost", 6789);
    } catch (IOException e1) {
        e1.printStackTrace();
    }
    try{
        double k1, B, A;
        double n = 13;
        double g = 61;
        long x = 3;

        // Sends an unencrypted number to the server
        A = (Math.pow(g, x))%n;
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());  

        // Transforms A into a byte array and sends it over
        outToServer.writeDouble(A);
        outToServer.flush();
        System.out.println("Sending " + A);

        // Reads the incoming data from the server
        B = inFromServer.readDouble();
        System.out.println("Recieved " + B);

        // Modifies the data to create the number for des key
        k1 = (Math.pow(B, x))%n;
        System.out.println("DES key seed = " + k1);
        byte[] deskeydata = toByteArray(k1);

        // Turns the bytes of the modified number into a DES key spec
        DESKeySpec deskeyspec = new DESKeySpec(deskeydata);

        // Makes a secret key (DES)
        SecretKeyFactory keyF = SecretKeyFactory.getInstance("DES");
        SecretKey keystuff = keyF.generateSecret(deskeyspec);
        System.out.println(keystuff.toString());

        // Takes in input from the user and turns it into binary
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a message:");

        String sentence = inFromUser.readLine();
        byte[] str2 = sentence.getBytes();
        byte[] encodedMessage = Base64.getEncoder().encode(str2);

        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // Encrypts the user's input with the secret key
        c.init(Cipher.ENCRYPT_MODE, keystuff, new IvParameterSpec(new byte[8]));
        byte[] ct2 = c.doFinal(encodedMessage);
        System.out.println("Initted the cipher and moving forward with " + new String(ct2));

        // Writes the encrypted message to the user
        outToServer.write(ct2);
        outToServer.flush();


        inFromServer.close();
        outToServer.close();
    } catch(Exception e){
        e.printStackTrace();
    }

}
