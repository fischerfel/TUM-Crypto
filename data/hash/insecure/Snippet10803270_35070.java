import java.security.MessageDigest;

class x {
    public static void main(String[] args) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        String mystring ="zWh9YZX3";
        byte[] bytes = mystring.getBytes();
        byte[] hash = sha1.digest(bytes);
        System.out.write(hash, 0, hash.length);
    }
}
