createNewKeys(strAlias, getActivity());
encryptString(strtoken, strAlias, this);

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void createNewKeys(String strAlis, Context context) {
        String alias = strAlis.toString();
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
        }
        refreshKeys();
    }

    public static String encryptString(String token, String alias, Context context) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            String initialText = token;
            Log.e("MessageApp=", ""+initialText.toString());
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();//Error in this line

            byte[] values = outputStream.toByteArray();
            encryptedText = Base64.encodeToString(values, Base64.DEFAULT);
        } catch (Exception e) {
            Toast.makeText(context, "encryptString Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();

        }
                  return encryptedText;
    }


 public static void decryptString(String encryptedText, String alias) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            String cipherText = encryptedText.toString();
            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            decryptedText = new String(bytes, 0, bytes.length, "UTF-8");

        } catch (Exception e) {
        }
    }
