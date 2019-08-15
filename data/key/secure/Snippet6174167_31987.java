        EncryptedContentInfo encryptionInfo = parseContentInfo(data);
        AlgorithmIdentifier algoID = encryptionInfo.getContentEncryptionAlgorithm();

        // get the real encrypted data
        byte[] encryptedData = encryptionInfo.getEncryptedContent().getOctets();

        // extract the initialization vector from the algorithm identifier object
        byte[] ivBytes = ((ASN1OctetString) algoID.getParameters()).getOctets();
        // create the key depending on the algorithm
        SecretKeySpec keySpec = new SecretKeySpec(rawKey, algoID.getObjectId().getId());
        // request cipher
        Cipher c = Cipher.getInstance(algoID.getObjectId().getId(), CRYPT_PROVIDER);

        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
        byte[] decrypted = c.doFinal(encryptedData);

        return decrypted;

        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        } catch (NoSuchProviderException e) {
            throw new CryptoException(e);
        } catch (NoSuchPaddingException e) {
            throw new CryptoException(e);
        } catch (InvalidKeyException e) {
            throw new CryptoException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new CryptoException(e);
        } catch (IllegalBlockSizeException e) {
            throw new CryptoException(e);
        } catch (BadPaddingException e) {
            throw new CryptoException(e);
        }
    }
}
