/**
 * @param args
 */
public static void main(String[] args)
{
    try
    {
        boolean useBouncyCastleProvider = false;

        Provider provider = null;
        if (useBouncyCastleProvider)
        {
            provider = new BouncyCastleProvider();
            Security.addProvider(provider);
        }

        String plainText = "This is a plain text!!";

        // KeyPair
        KeyPairGenerator keyPairGenerator = null;
        if (null != provider)
            keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
        else
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Signature
        Signature signatureProvider = null;
        if (null != provider)
            signatureProvider = Signature.getInstance("SHA256WithRSA", provider);
        else
            signatureProvider = Signature.getInstance("SHA256WithRSA");
        signatureProvider.initSign(keyPair.getPrivate());

        signatureProvider.update(plainText.getBytes());
        byte[] signature = signatureProvider.sign();

        System.out.println("Signature Output : ");
        System.out.println("\t" + new String(Base64.encode(signature)));

        // Message Digest
        String hashingAlgorithm = "SHA-256";
        MessageDigest messageDigestProvider = null;
        if (null != provider)
            messageDigestProvider = MessageDigest.getInstance(hashingAlgorithm, provider);
        else
            messageDigestProvider = MessageDigest.getInstance(hashingAlgorithm);
        messageDigestProvider.update(plainText.getBytes());

        byte[] hash = messageDigestProvider.digest();

        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashingAlgorithm);

        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hash);
        byte[] hashToEncrypt = digestInfo.getEncoded();

        // Crypto
        // You could also use "RSA/ECB/PKCS1Padding" for both the BC and SUN Providers.
        Cipher encCipher = null;
        if (null != provider)
            encCipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", provider);
        else
            encCipher = Cipher.getInstance("RSA");
        encCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());

        byte[] encrypted = encCipher.doFinal(hashToEncrypt);

        System.out.println("Hash and Encryption Output : ");
        System.out.println("\t" + new String(Base64.encode(encrypted)));
    }
    catch (Throwable e)
    {
        e.printStackTrace();
    }
}
