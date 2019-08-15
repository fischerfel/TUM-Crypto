public class ToHash {

    public static void main(String[] args)  {

        byte[] data = "test".getBytes("UTF8");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        System.out.println(new BASE64Encoder().encode(hash));

    }
}
