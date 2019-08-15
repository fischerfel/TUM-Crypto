 public String getMD5Hex(String inputString) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(inputString.getBytes());

    byte[] digest = md.digest();

    return digest.toString();
}

public Boolean aliceChapAuth(Socket socket, byte[] sharedKey) {

    Boolean check = false;

    try {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        //generate challenge
        BigInteger b = new BigInteger(256, new Random());

        //send Challenge
        out.writeObject(b);
        out.flush();

        //receive hash
        String hash = (String)in.readObject();

        //compare foreign and local hash
        String s = sharedKey.toString();

        toastDisplay(getMD5Hex(b+s)+" "+hash);

        if(hash == getMD5Hex(b+s)) {

            check = true;
        }
        //send response


    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return check;
}
