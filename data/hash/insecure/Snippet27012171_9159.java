import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
...
public String getIdHash(long id){
    String hash = null;
    long intId = id ^ Long.MAX_VALUE;
    String md5 = String.format("%X-ANY-TEXT", intId);
    try {
        MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] arr = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i)
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,3));

        hash = sb.toString();
    } catch (NoSuchAlgorithmException e) {
        Log.e("MD5", e.getMessage());
    }

    return hash.toUpperCase();
}
