    public static void main(String[] args) {
        // String str = new String(md5x16("test"));
        byte[] input = md5x16("testtesttest");
        String t = new String(input);
        System.out.println(t);
    }

    public static byte[] md5x16(String text) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(text.getBytes());
            byte[] md5Bytes = digester.digest();
            return md5Bytes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
