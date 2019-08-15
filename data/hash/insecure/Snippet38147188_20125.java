public static String md5Encrypt(String encryptStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(encryptStr.getBytes("utf8"));
            byte[] bs = md5.digest();
            System.out.println("length1 :" + bs.length);
            String str = new String(bs);
            System.out.println("length2 :" + str.getBytes("utf8").length);
            return str;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
