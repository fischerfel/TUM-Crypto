    package com.bodom.ghosty;

    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.NoSuchPaddingException;
    import java.io.*;
    import java.math.BigInteger;
    import java.nio.ByteBuffer;
    import java.nio.channels.FileChannel;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.security.*;
    import java.security.spec.InvalidKeySpecException;
    import java.security.spec.RSAPrivateKeySpec;
    import java.security.spec.RSAPublicKeySpec;
    import java.util.Scanner;

    public class EncryptionUtil {
        private final PrivateKey privateKey;
        private final PublicKey publicKey;

        /**
         * Build an EncryptionUtil object
         *
         * @param keyPair The KeyPair used for Ghosty
         */
        public EncryptionUtil (KeyPair keyPair) {
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        }


        /**
         * Generate a pair of RSA keys
         *
         * @return A keypair of RSA keys
         * @throws NoSuchAlgorithmException
         */
        private static KeyPair keyGenerate() throws NoSuchAlgorithmException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.genKeyPair();
        }


        /**
         * Crypt data
         *
         * @param data      Data to encrypt
         * @return          The crypted data
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         * @throws InvalidKeyException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        private static byte[] rsaEncryption(byte[] data, EncryptionUtil encryptionUtil) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionUtil.publicKey);
            return cipher.doFinal(data);
        }


        /**
         * Read the bytes of a file
         *
         * @param file is the file to read
         * @return     the bytes of the file
         * @throws IOException
         */
        private static byte[] readBytesInFile (Path file) throws IOException {
            byte[] result = new byte[(int)Files.size(file)];
            try {
                try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file.getFileName().toString()))) {
                    int bytesRead = 0;
                    while (bytesRead < result.length) {
                        int bytesLeft = result.length - bytesRead;
                        int bytesGet = inputStream.read(result, bytesRead, bytesLeft);
                        if (bytesGet > 0) {
                            bytesRead += bytesGet;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            return result;
        }


        /**
         * Encrypt a file
         *
         * @param file      Path of the file to crypt
         * @return          A byte array which contains the crypted lines of the file
         * @throws java.io.IOException
         * @throws javax.crypto.IllegalBlockSizeException
         * @throws java.security.InvalidKeyException
         * @throws javax.crypto.BadPaddingException
         * @throws java.security.NoSuchAlgorithmException
         * @throws javax.crypto.NoSuchPaddingException
         */
        public static byte[] encryption (Path file, EncryptionUtil encryptionUtil) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
            byte[] datatocrypt = readBytesInFile(file);
            int offset = 0;
            byte[] cryptedfile = null;

            while (offset < datatocrypt.length) {
                byte[] outputBytes;
                byte[] tmp;

                if(datatocrypt.length - offset < 200 ) {
                    outputBytes = new byte[datatocrypt.length - offset];
                    System.arraycopy(datatocrypt, offset, outputBytes, 0, datatocrypt.length - offset);

                    byte[] crypt = rsaEncryption(outputBytes, encryptionUtil);

                    tmp = cryptedfile;
                    if (tmp != null) {
                        cryptedfile = new byte[tmp.length + crypt.length];
                    }
                    else cryptedfile = new byte[crypt.length];

                    if (tmp != null) {
                        System.arraycopy(tmp, 0, cryptedfile, 0, tmp.length);
                        System.arraycopy(crypt, 0, cryptedfile, tmp.length, crypt.length);
                    }
                    else {
                        System.arraycopy(crypt, 0, cryptedfile, 0, crypt.length);
                    }
                    break;
                }

                outputBytes = new byte[200];
                System.arraycopy(datatocrypt, offset, outputBytes, 0, 200);

                byte[] crypt = rsaEncryption(outputBytes, encryptionUtil);

                tmp = cryptedfile;
                if (tmp != null) {
                    cryptedfile = new byte[tmp.length + crypt.length];
                }
                else cryptedfile = new byte[crypt.length];

                if (tmp != null) {
                    System.arraycopy(tmp, 0, cryptedfile, 0, tmp.length);
                    System.arraycopy(crypt, 0, cryptedfile, tmp.length, crypt.length);
                }
                else {
                    System.arraycopy(crypt, 0, cryptedfile, 0, crypt.length);
                }

                offset += 200 ;
            }
            return cryptedfile;
        }


        /**
         * Decrypt data
         *
         * @param crypteddata Crypted data to decrypt
         * @return            The decrypted data
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         * @throws InvalidKeyException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        private static byte[] rsaDecryption(byte[] crypteddata, EncryptionUtil encryptionUtil) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, encryptionUtil.privateKey);
            return cipher.doFinal(crypteddata);
        }


        /**
         * Decrypt a file
         *
         * @param file       Path of the file to decrypt
         * @return           A byte array which contains the decrypted lines of the file
         * @throws IOException
         * @throws IllegalBlockSizeException
         * @throws InvalidKeyException
         * @throws BadPaddingException
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         */
        public static byte[] decryption(Path file, EncryptionUtil encryptionUtil) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
            byte[] crypteddata = readBytesInFile(file);
            int offset = 0;
            byte[] decryptedfile = null;

            while (offset < crypteddata.length) {
                byte[] outputBytes;
                byte[] tmp;

                if(crypteddata.length - offset < 200 ) {
                    outputBytes = new byte[crypteddata.length - offset];
                    System.arraycopy(crypteddata, offset, outputBytes, 0, crypteddata.length - offset);

                    byte[] decrypt = rsaDecryption(outputBytes, encryptionUtil);

                    tmp = decryptedfile;
                    if (tmp != null) {
                        decryptedfile = new byte[tmp.length + decrypt.length];
                    }
                    else decryptedfile = new byte[decrypt.length];

                    if (tmp != null) {
                        System.arraycopy(tmp, 0, decryptedfile, 0, tmp.length);
                        System.arraycopy(decrypt, 0, decryptedfile, tmp.length, decrypt.length);
                    }
                    else {
                        System.arraycopy(decrypt, 0, decryptedfile, 0, decrypt.length);
                    }
                    break;
                }

                outputBytes = new byte[200];
                System.arraycopy(crypteddata, offset, outputBytes, 0, 200);

                byte[] decrypt = rsaDecryption(outputBytes, encryptionUtil);

                tmp = decryptedfile;
                if (tmp != null) {
                    decryptedfile = new byte[decrypt.length + tmp.length];
                }
                else decryptedfile = new byte[decrypt.length];

                if (tmp != null) {
                    System.arraycopy(tmp, 0, decryptedfile, 0, tmp.length);
                    System.arraycopy(decrypt, 0, decryptedfile, tmp.length, decrypt.length);
                }
                else {
                    System.arraycopy(decrypt, 0, decryptedfile, 0, decrypt.length);
                }
                offset +=200 ;
            }
            return decryptedfile;
        }

        /**
         * Save a key in a file
         *
         * @param modulus  Modulus of the key to save
         * @param exponent Exponent of the key to save
         * @param filename File used to save the keys
         * @throws IOException
         * @throws NoSuchAlgorithmException
         */
        private static void saveKeyToFile (BigInteger modulus, BigInteger exponent, String filename) throws IOException, NoSuchAlgorithmException {
            Path path = Paths.get(filename);
            if(!Files.exists(path)) {
                Files.createFile(path);
            }

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
                objectOutputStream.writeObject(modulus);
                objectOutputStream.writeObject(exponent);
            }
        }

        /**
         * Save a KeyPair in two files
         *
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         * @throws FileNotFoundException
         * @throws IOException
         */
        public static void saveKeyPair(EncryptionUtil encryptionUtil, String directorypath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(encryptionUtil.publicKey, RSAPublicKeySpec.class);
            saveKeyToFile(rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent(), directorypath + "/public.key");

            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(encryptionUtil.privateKey, RSAPrivateKeySpec.class);
            saveKeyToFile(rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent(), directorypath + "/private.key");
        }

        /**
         * Get a PublicKey from a file
         *
         * @param filename File where the PublicKey is saved
         * @return         The PublicKey get in the file
         * @throws IOException
         * @throws ClassNotFoundException
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         */
        private static PublicKey getPublicKeyFromFile (String filename) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
                BigInteger modulus = (BigInteger) objectInputStream.readObject();
                BigInteger exponent = (BigInteger) objectInputStream.readObject();

                RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(rsaPublicKeySpec);
            }
        }

        /**
         * Get a PrivateKey from a file
         *
         * @param filename File where the PrivateKey is saved
         * @return         The PrivateKey get in the file
         * @throws IOException
         * @throws ClassNotFoundException
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         */
        private static PrivateKey getPrivateKeyFromFile (String filename) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
                BigInteger modulus = (BigInteger) objectInputStream.readObject();
                BigInteger exponent = (BigInteger) objectInputStream.readObject();

                RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePrivate(rsaPrivateKeySpec);
            }
        }

        /**
         * Get the RSA keypair from the files
         *
         * @return The Keypair which contains the public and the private key
         * @throws IOException
         * @throws ClassNotFoundException
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         */
        public static KeyPair getKeysFromFiles (String directorypath) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException{
            PublicKey publicKey = getPublicKeyFromFile(directorypath + "/public.key");
            PrivateKey privateKey = getPrivateKeyFromFile(directorypath + "/private.key");
            return new KeyPair(publicKey, privateKey);
        }


        public static void main(String[] args) {
            EncryptionUtil encryptionUtil = null;

            Scanner scanner = new Scanner(System.in);
            System.out.println("Path of the keys :");
            String path = scanner.nextLine();

            try {
                encryptionUtil = new EncryptionUtil(EncryptionUtil.keyGenerate());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Path directorypath = Paths.get(path);
            try {
                Files.createDirectories(directorypath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                saveKeyPair(encryptionUtil, path);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException| IOException e) {
                System.out.println("Error during the storage of the keys : " + e);
            }

            // Crypt part
            byte[] uncrypt;
            try {
                if (encryptionUtil != null) {
                    if (encryptionUtil.publicKey != null && encryptionUtil.privateKey != null) {
                        //Cryptage
                        byte[] crypt = encryption(Paths.get("filetocrypt"), encryptionUtil);

                        FileOutputStream fileOutputStream = new FileOutputStream("cryptedfile");
                        FileChannel channel = fileOutputStream.getChannel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(crypt.length * 2);
                        byteBuffer.put(crypt);
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                        channel.close();

                        //Decryptage
                        uncrypt = decryption(Paths.get("cryptedfile"), encryptionUtil);

                        String v = new String(uncrypt);
                        System.out.println("END " + v);
                    }
                }
            } catch (InvalidKeyException | NoSuchAlgorithmException
                    | NoSuchPaddingException | IllegalBlockSizeException
                    | BadPaddingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
