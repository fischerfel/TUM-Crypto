public static void main(String[] args) throws NoSuchAlgorithmException {
        String hexString = "90aa";
        String bkey = hexToString(hexString);
        String someString = "qwe";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String input = someString + bkey;
        String checksum = new BigInteger(1, messageDigest.digest(input
                .getBytes())).toString(16);
        System.out.println(checksum);
    }
    public static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
