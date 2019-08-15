            //ONE WAY OF SIGNING
            byte[] signedInfoSha1Digest = sha1Digest(bSignedInfo);
            //byte[] bytesCS = new byte[]{55,-59,-1,71,-62,26,57,126,76,7,120,53,-38,-51,8,38,127,-29,5,25};
            String vSignedInfoSha1DigestString64 = Base64.encodeToString(signedInfoSha1Digest, Base64.DEFAULT);
            byte[] signedInfoDerSha1Digest = mergeArrays(DER_SHA1_DIGEST_IDENTIFIER, signedInfoSha1Digest);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] signatureBytes = cipher.doFinal(signedInfoDerSha1Digest);
            String base64RsaSignature1 = base64encode(signatureBytes, true);
            //String vFirma = bytesToHex(signatureBytes);

            //ANOTHER WAY OF SIGNING
            Signature instance2 = Signature.getInstance("SHA1withRSA");
            instance2.initSign(privateKey);
            instance2.update(bSignedInfo);
            byte[] bFirma3 = instance2.sign();
            String base64RsaSignature2 = base64encode(bFirma3, true);
            Log.i("Log","nada");

            //VALIDATE THE RESULT
            Signature instanceValida = Signature.getInstance("SHA1withRSA");
            instanceValida.initVerify(Certificado);
            instanceValida.update(bSignedInfo);
            if(instanceValida.verify(bFirma3)==true)
                Log.i("Validacion","La firma es valida");
            else
                Log.i("Validacion","La firma NO ES valida");

            return bFirma3;
        } 
        catch (Throwable e) {
            Log.e(LOG_TAG, "Error generating signature for XML", e);
            throw e;
        }
    }
