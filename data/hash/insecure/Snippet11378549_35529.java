public static String MD5(String message) {
    try {
        long start = System.currentTimeMillis();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(message.getBytes());
        byte[] hashBytes = md5.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        long end = System.currentTimeMillis();
        MD5TIME = end - start;
        System.out.println(end);
        System.out.println(start);
        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
