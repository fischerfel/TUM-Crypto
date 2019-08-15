public class Sha {

    private static String byteArrayToHexString(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(Integer.toString((array[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        final int MAX_PASS_ATTEMPTS = 40000;

        final String PASS_PREFIX = "previousblock14currentblock";

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        for (int i = 0; i < MAX_PASS_ATTEMPTS; i++) {
            String password = PASS_PREFIX + i;

            md.reset();
            md.update(password.getBytes());
            byte[] hash = md.digest();

            //System.out.println(byteArrayToHexString(hash));

            if (hash[0] == 0 && hash[1] == 0) {
                System.out.println("Password: " + password);
                System.out.println("Hash: " + byteArrayToHexString(hash));
                return;
            }
        }       

        System.out.println("No luck after " + MAX_PASS_ATTEMPTS + " tries.");
    }
}
