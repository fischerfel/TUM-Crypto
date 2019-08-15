@Component
public class BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence arg0) {
        try {
            return getMD5Hex((String) arg0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean matches(CharSequence arg0, String arg1) {
        return arg0.equals(encode(arg1));
    }

    public static String getMD5Hex(final String inputString) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputString.getBytes());

        byte[] digest = md.digest();

        return convertByteToHex(digest);
    }

    private static String convertByteToHex(byte[] byteData) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}
