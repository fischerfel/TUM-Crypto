public String encrypt(SecretKey key, String stringIn){

    String outString = "";      

    if (stringIn.isEmpty() || stringIn.toUpperCase().equals("NULL")){
        return "";
    }

    try {   

        if (key == null)
            key = this.key;


        InputStream in = new ByteArrayInputStream(stringIn.getBytes("UTF-8"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Create and initialize the encryption engine
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Create a special output stream to do the work for us
        CipherOutputStream cos = new CipherOutputStream(out, cipher);

        // Read from the input and write to the encrypting output stream
        byte[] buffer = new byte[2048];

        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            cos.write(buffer, 0, bytesRead);
        }

        cos.close();

        // For extra security, don't leave any plaintext hanging around memory.
        java.util.Arrays.fill(buffer, (byte) 0);

        outString = out.toString();

    } catch (UnsupportedEncodingException e) {

        e.printStackTrace();

    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();

    } catch (NoSuchPaddingException e) {

        e.printStackTrace();

    } catch (InvalidKeyException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    } finally {

        return outString;
    }

}
