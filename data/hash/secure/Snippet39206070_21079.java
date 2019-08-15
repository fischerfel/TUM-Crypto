class HashGenerator {
public HashGenerator() {
}

public byte[] generateSha256Hash(byte[] message) {
    String var2 = "SHA-256";
    String var3 = "BC";
    byte[] var4 = null;

    try {
        MessageDigest var7 = MessageDigest.getInstance(var2, var3);
        var7.reset();
        var4 = var7.digest(message);
    } catch (Exception var6) {
        var6.printStackTrace();
    }

    return var4;
}
}
