class Utils {
 public static String md5Hash(String input) {
        String result = "";
        try {
            System.out.println("Input=" + input);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(input.getBytes());
            result = md.digest().toString();
        } catch (Exception ee) {
            System.err.println("Error computing MD5 Hash");
        }
        return result;
    }
};
