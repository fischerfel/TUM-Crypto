        public static String hash(String str) {
            try {
                    MessageDigest mg = MessageDigest.getInstance("SHA-1");
                    byte[] result = mg.digest(str.getBytes());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < result.length; i++) {
                            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                    System.err.println("SHA-1 not found.");
                    return "";
            }
    }
