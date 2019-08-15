import java.security.*;

public class Main {
    public static void main(String[] args) throws Exception
    {
        byte[] data = new byte[] {1,2,3};

        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(data);
        byte[] sha1Hash = digest.digest();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        KeyPair keyPair = kpg.genKeyPair();

        Signature signingInstance = Signature.getInstance("SHA1withRSA");
        signingInstance.initSign(keyPair.getPrivate());
        signingInstance.update(data);
        byte[] signature = signingInstance.sign();

        Signature dataVerifyingInstance = Signature.getInstance("SHA1withRSA");
        dataVerifyingInstance.initVerify(keyPair.getPublic());
        dataVerifyingInstance.update(data);
        boolean dataVerified = dataVerifyingInstance.verify(signature);

        Signature hashVerifyingInstance = Signature.getInstance("NONEwithRSA");
        hashVerifyingInstance.initVerify(keyPair.getPublic());
        hashVerifyingInstance.update(sha1Hash);
        boolean hashVerified = hashVerifyingInstance.verify(signature);

        System.out.println("Verification based on data: " + dataVerified);
        System.out.println("Verification based on hash: " + hashVerified);
    }
}
