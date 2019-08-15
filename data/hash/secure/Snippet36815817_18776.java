public class Main {
    public static void main(String[] args) {
        String paramString = "teststring";
        calculateSignature(hash(paramString));
    }

    private static byte[] hash(String paramString)
    {
        MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
        localMessageDigest.update(paramString.getBytes("UTF-8"));
        byte[] paramByte = localMessageDigest.digest();
        System.out.println("Hash: " + DatatypeConverter.printBase64Binary(paramByte));
        return paramByte;
    }

    public static void calculateSignature(byte[] paramArrayOfByte)
    {
        String Algor = "HmacSHA256";
        Mac localMac = Mac.getInstance(Algor);
        byte [] key = "secretkey".getBytes();
        localMac.init(new SecretKeySpec(key, Algor));
        paramArrayOfByte = localMac.doFinal(paramArrayOfByte);
        System.out.println("Signature: " + DatatypeConverter.printBase64Binary(paramArrayOfByte));
    }
}
