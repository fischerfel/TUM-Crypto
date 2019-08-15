public static String encryptPassword(String content) {


    String resultString = "";
       String appkey = "ftjf,ckdfkl";

       byte[] a = appkey.getBytes();
       byte[] datSource = content.getBytes();
       byte[] b = new byte[a.length + 4 + datSource.length];

       int i;
    for (i = 0; i < datSource.length; i++) {
        b[i] = datSource[i];
    }

    b[i++] = (byte) 172;
    b[i++] = (byte) 163;
    b[i++] = (byte) 161;
    b[i++] = (byte) 163;

    for (int k = 0; k < a.length; k++) {
        b[i] = a[k];
        i++;
    }

    try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(b);
        resultString = new HexBinaryAdapter().marshal(md5.digest());
    } catch (Exception e) {

    }

    return resultString.toLowerCase();
}


public static void main(String[] args) {
    System.out.print(encryptPassword("123456"));
}
