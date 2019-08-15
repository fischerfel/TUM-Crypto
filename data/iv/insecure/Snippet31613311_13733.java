public class TwofishInStreams {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher twofish = Cipher.getInstance("twofish/cbc/pkcs5padding");
        SecretKey twoFishKey = new SecretKeySpec(new byte[16], "twofish");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        twofish.init(Cipher.ENCRYPT_MODE, twoFishKey, iv);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (CipherOutputStream cos = new CipherOutputStream(baos, twofish)) {
            cos.write("owlstead".getBytes(StandardCharsets.UTF_8));
        }
        System.out.println(Hex.toHexString(baos.toByteArray()));
    }
}
