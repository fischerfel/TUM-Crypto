public class Main {
    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        String uri = "https://sample.json";
        String password = "*********";

        String message = (timestamp + uri + password);
        System.out.println("message: " + message);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] securityToken = messageDigest.digest(message.getBytes());
            System.out.println("SHA-1 Hex: " + bytesToHex(securityToken));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
