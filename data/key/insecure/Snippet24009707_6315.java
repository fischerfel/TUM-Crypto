public class CBCTest {
    public static void main(String[] args) throws Exception {
        Cipher cc = Cipher.getInstance("DES/CBC/NoPadding");
        Key k = new SecretKeySpec(new byte[] {1,1,1,1,1,1,1,1}, "DES");
        cc.init(Cipher.ENCRYPT_MODE, k);
        byte[] data = new byte[]{1,2,3,4,5,6,7,8};
        cc.doFinal(data);
        System.out.println("Encrypted: " + Arrays.toString(cc.doFinal(data)));
    }
}
