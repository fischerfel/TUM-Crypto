class myClass {

    public static void main(String[] args) {
        String password = "asdf";
        try {
            System.out.println(password);
            password = (passwordencrypt(password));
            System.out.println(password);

        } catch (NoSuchAlgorithmException ex) {
             System.out.println("oops");
        }
        password = base16to64(password);

        System.out.println(password);

    }

    public static String passwordencrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b1 : b) {
            sb.append(Integer.toHexString(b1 & 0xff).toString());
        }
        return sb.toString();
    }
    public static String base16to64(String hex){
    return Base64.getEncoder().encodeToString(new BigInteger(hex, 16).toByteArray());
    }

}
