public class Test {

    static SecureRandom random = new SecureRandom();
    static byte[] salt = new byte[16];

    static {
        random.nextBytes(salt);
    }

    public static void main(String[] args) {
        String inputOne = "abc";
        String a = new String(toHash(inputOne));
        System.out.println("hash with salt of String a:" + a);
        String inputTwo = "abc";
        String b = new String(toHash(inputTwo));
        System.out.println("hash with salt of String b:" + b);

        if(Arrays.equals(toHash(inputOne), toHash(inputTwo))) {
            System.out.println("Same");
        }
        else {
            System.out.println("Not Same");
        }
    }

    public static byte[] toHash(String password) {

        byte[] hash = null;

        SecretKeyFactory f = null;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            hash = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }
}
