public static String getMD5Hash(String val) throws Exception {
        byte[] bytes = val.getBytes();

        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] digest = m.digest(bytes);
        String hash = new BigInteger(1, digest).toString(16);
        System.out.println(hash.length());
        return hash;
    }

    public static void main(String[] asd) throws Exception{
        for(int i=0;i<10;i++){
            System.out.println(getMD5Hash(i+Math.pow(10, i)+""));//for testing
            System.out.println(getMD5Hash(i+""));//for testing
        }
    }
