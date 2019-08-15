    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {

    System.out.println("Please enter file path: \n");

    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    String dir = stdin.readLine();
    File file = new File(dir);

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

        dataBytes = md.digest();

    md.update(dataBytes);
    System.out.println("MD5: " + new BigInteger(1, md.digest()).toString(16));

}
