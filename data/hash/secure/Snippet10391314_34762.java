private static String crypt(String chain, String method) {
    MessageDigest md;
    try {
        md = MessageDigest.getInstance(method);
        md.update(chain.getBytes("UTF-8"));
        byte[] mb = md.digest();
        String out = "";
        for (int i = 0; i < mb.length; i++) {
            byte temp = mb[i];
            String s = Integer.toHexString(new Byte(temp));
            while (s.length() < 2) {
                s = "0" + s;
            }
            s = s.substring(s.length() - 2);
            out += s;
        }
        return out;

    } catch (Exception e) {
        System.out.println("ERROR: " + e.getMessage());
    }
    return null;
}
