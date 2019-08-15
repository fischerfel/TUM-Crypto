public static Vector<PrivateKey> privateKeyArr = new Vector<PrivateKey>();
    private static PrivateKey privateKey;

    public static void load_win_store() {
        try {
            KeyStore store = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
            store.load(null, null);
            Enumeration enumer = store.aliases();

            while (enumer.hasMoreElements()) {
                String alias = enumer.nextElement().toString();
                X509Certificate x509 = (X509Certificate) store.getCertificate(alias);
                PrivateKey privateKey = (PrivateKey) store.getKey(alias, null);
                privateKeyArr.add(privateKey);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        privateKey = privateKeyArr.get(0);
    }

public static void main(String[] args) {
        try {
            String plaintext = "abc";
            load_win_store();

            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(privateKey);
            instance.update((plaintext).getBytes());
            byte[] signature = instance.sign();

            MessageDigest _sha1 = MessageDigest.getInstance("SHA1");
            byte[] _digest = _sha1.digest(plaintext.getBytes());
            DERObjectIdentifier sha1oid_ = new DERObjectIdentifier("1.3.14.3.2.26");

            AlgorithmIdentifier sha1aid_ = new AlgorithmIdentifier(sha1oid_, null);
            DigestInfo di = new DigestInfo(sha1aid_, _digest);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherText = cipher.doFinal(di.getDEREncoded());

            System.out.println("Cipher text: " + byteArray2Hex(cipherText));
            System.out.println("Signature: " + byteArray2Hex(signature));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
