public static void main(String[] args) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("SHA-256");

    byte[] hash = md.digest("password".getBytes());

    StringBuffer sb = new StringBuffer();
    for(byte b : hash) {
        sb.append(Integer.toHexString(b & 0xff));
    }

    System.out.println(sb.toString());
}
