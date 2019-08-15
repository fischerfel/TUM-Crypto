import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 
import java.util.Vector;

public class Test {

private static Vector<String> vecA, vecB;

public static void main(String args[]) {
    vecA = new Vector<String>();
    vecB = new Vector<String>();

    vecA.add("hallo");
    vecA.add("blödes Beispiel");
    vecA.add("Einer geht noch");
    vecB.add("hallo");
    vecB.add("blödes Beispiel");
    vecB.add("Einer geht noch");

    System.out.println("HashCode() VecA: " + vecA.hashCode());
    System.out.println("HashCode() VecB: " + vecB.hashCode());

    System.out.println("md5 VecA: " + md5(vecA));
    System.out.println("md5 VecB: " + md5(vecB));

    vecA.add("ungleich");

    System.out.println("HashCode() VecA: " + vecA.hashCode());
    System.out.println("HashCode() VecB: " + vecB.hashCode());

    System.out.println("md5 VecA: " + md5(vecA));
    System.out.println("md5 VecB: " + md5(vecB));

}

private static String md5(Vector<String> v){
    try {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(vecA.toString().getBytes());
        byte messageDigest[] = algorithm.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException nsae) {}
    return null;
}
}
