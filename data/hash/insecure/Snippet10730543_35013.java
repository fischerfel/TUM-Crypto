public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
    MessageDigest md = MessageDigest.getInstance("sha-1");
    FileInputStream in =  new FileInputStream("./ic_launcher.png");
    int bytes = 0;
    while ((bytes = in.read()) != -1) {
        md.update((byte)bytes);
    }
    in.close();
    byte[] thedigest = md.digest();
    System.out.println(Base64Encoder.encode(thedigest));
}
