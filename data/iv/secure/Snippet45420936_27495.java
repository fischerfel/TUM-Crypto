public class KeyStoreHelper {

    public static byte[] decryptAES(FileInputStream fis, byte[] key) throws IOException {

        CipherInputStream cin = null;
        try {

            byte[] keyBytes = getKeyBytes(key);
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] buffer = new byte[1024];

            ByteArrayInputStream cipherIn = new ByteArrayInputStream(buffer);
            ByteArrayOutputStream cipherOut = new ByteArrayOutputStream();
            cin = new CipherInputStream(cipherIn, aesCipher);

            int length;
            while ((length = fis.read(buffer)) != -1) {
                cin.read(buffer, 0, length);
                cipherOut.write(buffer);
            }

            return cipherOut.toByteArray();
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cin != null) {
                cin.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return new byte[1];
    }

    public static void encryptAES(FileOutputStream fos, byte[] plainText, byte[] key) throws IOException {

        CipherOutputStream cos = null;
        try {
            byte[] keyBytes = getKeyBytes(key);
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            cos = new CipherOutputStream(fos, aesCipher);
            cos.write(plainText);
            cos.flush();

        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cos != null) {
                cos.close();
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }
    }

    private static byte[] getKeyBytes(final byte[] key) throws Exception {
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
        return keyBytes;
    }
}
