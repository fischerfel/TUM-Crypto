public static void main( String[] args )
{
    System.out.println("MessageDisgest: " +  MD5("12345") );

    MessageDigest md5Digest = DigestUtils.getMd5Digest();
    System.out.println("MD5Hex with digest: " + DigestUtils.md5Hex(md5Digest.digest("12345".getBytes())));

    System.out.println("MD5Hex: " + DigestUtils.md5Hex("12345"));
}

public final static String MD5(String s) {
    char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

    try {
        byte[] btInput = s.getBytes();
        // 獲得MD5摘要算法的 MessageDigest 對象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字節更新摘要
        mdInst.update(btInput);
        // 獲得密文
        byte[] md = mdInst.digest();
        // 把密文轉換成十六進制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];

            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        System.out.println();
        return new String(str);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
