public class Decrypt {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("sample.log"));
        String newStr = null;
        String value = "";
        while ((newStr = br.readLine()) != null) {
            String next = null;
            if (newStr.contains("FLTR")) {
                next = newStr.substring(97, 135); // this gets string **[B@4783165b**
                String collect = CallToDecrypt(next, value);
                system.out.println(collect);
            }
        }
        pt.close();
        br.close();
    }

    private static String CallToDecrypt(String next, String value) {
        String key = "ThisIsASecretKey";
        byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        byte[] original = cipher.doFinal();
        return new String(original, Charset.forName("US-ASCII"));
    }
}
