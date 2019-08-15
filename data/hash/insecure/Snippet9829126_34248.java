    public void doMD5() throws IOException, NoSuchAlgorithmException {

    File file = new File(jTxtMD51.getText());

    FileInputStream iStream = null;

    try {iStream = new FileInputStream(file);}
    catch (FileNotFoundException e) {      
        String MD5Output = "There has been an error: " + e.toString();   
    }

    byte[] dataBytes = new byte[1024];

    MessageDigest md = MessageDigest.getInstance("MD5");

    int numRead = iStream.read(dataBytes);
        md.update(dataBytes, 0, numRead);

        iStream.close();

        byte[] MD5checksum = md.digest();

    md.update(dataBytes);

    BigInteger bigInt = new BigInteger(1, md.digest());
    String MD5Hash = bigInt.toString(16);

    jTextOutput.append("MD5 is : " + MD5Hash);

}
