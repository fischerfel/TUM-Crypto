public class HashCheck {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        byte[] file1Contents = Files.readAllBytes(Paths.get("C:/path/to/file1.csv"));
        byte[] file2Contents = Files.readAllBytes(Paths.get("C:/path/to/file2.csv"));

        String hashtext1 = computeHash(file1Contents);
        String hashtext2 = computeHash(file2Contents);
        System.out.println(hashtext1);
        System.out.println(hashtext2);
        System.out.println(hashtext1.equals(hashtext2));
    }

    public static String computeHash(String input) throws NoSuchAlgorithmException {
        return computeHash(input.getBytes());
    }

    public static String computeHash(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest hasher = java.security.MessageDigest.getInstance("MD5"); //MD5 or SHA1
        hasher.reset();
        hasher.update(input);
        byte[] digest = hasher.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16); // The hashes are base-16 numbers
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < hasher.getDigestLength() ){
          hashtext = "0"+hashtext;   
        }
        return hashtext;
    }

}
