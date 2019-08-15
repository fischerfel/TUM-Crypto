public static byte[] encrypt(String publicKey, String data) {
        if (TextUtils.isEmpty(publicKey) || TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            // Decode the modified public key into a byte[]
            byte[] publicKeyByteArray = Base64.decode(publicKey.getBytes("UTF-8"),Base64.NO_WRAP);

            Cipher mCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyByteArray);
            Key key = keyFactory.generatePublic(x509KeySpec);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return mCipher.doFinal(data.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            Log.e("RSAKEY", e.getMessage());
        }
        catch (NoSuchPaddingException e) {
            Log.e("RSAKEY", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("RSAKEY", e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e("RSAKEY", e.getMessage());
        } catch (InvalidKeySpecException e) {
            Log.e("RSAKEY", e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Log.e("RSAKEY", e.getMessage());
        } catch (BadPaddingException e) {
            Log.e("RSAKEY", e.getMessage());
        }
        return null;
    }
