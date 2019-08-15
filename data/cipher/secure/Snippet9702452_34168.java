package uk.co.frontiertown;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.GetPasswordDataRequest;
import com.amazonaws.services.ec2.model.GetPasswordDataResult;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

public class GetEc2WindowsAdministratorPassword {

    private static final String ACCESS_KEY = "xxxxxxxxxxxxxxxxxxxx";
    private static final String SECRET_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String PRIVATE_KEY_MATERIAL = "-----BEGIN RSA PRIVATE KEY-----\n" +
        "MIIEowIBAAKCAQEAjdD54kJ88GxkeRc96EQPL4h8c/7V2Q2QY5VUiJ+EblEdcVnADRa12qkohT4I\n" +
        // several more lines of key data
        "srz+xXTvbjIJ6RL/FDqF8lvWEvb8uSC7GeCMHTznkicwUs0WiFax2AcK3xjgtgQXMgoP\n" +
        "-----END RSA PRIVATE KEY-----\n";

    public static void main(String[] args) throws GeneralSecurityException, InterruptedException {
        Security.addProvider(new BouncyCastleProvider());
        String password = getPassword(ACCESS_KEY, SECRET_KEY, "i-XXXXXXXX", PRIVATE_KEY_MATERIAL);
        System.out.println(password);
    }

    private static String getPassword(String accessKey, String secretKey, String instanceId, String privateKeyMaterial) throws GeneralSecurityException, InterruptedException {

        // Convert the private key in PEM format to DER format, which JCE can understand
        privateKeyMaterial = privateKeyMaterial.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
        privateKeyMaterial = privateKeyMaterial.replace("-----END RSA PRIVATE KEY-----", "");
        byte[] der = Base64.decode(privateKeyMaterial);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(der);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Get the encrypted password data from EC2
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonEC2Client client = new AmazonEC2Client(awsCredentials);
        GetPasswordDataRequest getPasswordDataRequest = new GetPasswordDataRequest().withInstanceId(instanceId);
        GetPasswordDataResult getPasswordDataResult = client.getPasswordData(getPasswordDataRequest);
        String passwordData = getPasswordDataResult.getPasswordData();
        while (passwordData == null || passwordData.isEmpty()) {
            System.out.println("No password data - probably not generated yet - waiting and retrying");
            Thread.sleep(10000);
            getPasswordDataResult = client.getPasswordData(getPasswordDataRequest);
            passwordData = getPasswordDataResult.getPasswordData();
        }

        // Decrypt the password
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherText = Base64.decode(passwordData);
        byte[] plainText = cipher.doFinal(cipherText);
        String password = new String(plainText, Charset.forName("ASCII"));

        return password;
    }
}
