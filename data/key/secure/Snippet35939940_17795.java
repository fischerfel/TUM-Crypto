SSECustomerKey customerKey = Constants.getCustomerKey(mContext);

public static SSECustomerKey getCustomerKey(Context context) {

        SSECustomerKey sseKey = null;
        String preferenceKey = PreferenceData.getStringPref(PreferenceData.KEY_ENCRYPTION, context);

        if ( preferenceKey != null) {
            // decode the base64 encoded string
            byte[] decodedKey = Base64.decodeBase64(preferenceKey.getBytes());
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            sseKey = new SSECustomerKey(originalKey);
            return sseKey;
        } else {
            try {
                SecretKey symKey = generateSecretKey();
                // get base64 encoded version of the key
                String encodedKey = new String(Base64.encodeBase64(symKey.getEncoded()));
                System.out.println("key_string encoded: " + encodedKey);
                sseKey = new SSECustomerKey(symKey);
                PreferenceData.setStringPref(PreferenceData.KEY_ENCRYPTION, context, encodedKey);
                return sseKey;
            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }
        }
        return sseKey;
    }

    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, new SecureRandom());
            return generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

PutObjectRequest uploadRequest = new PutObjectRequest(Constants.BUCKET_NAME, key, mediaFileToUpload);
                            uploadRequest.withCannedAcl(CannedAccessControlList.PublicRead);
                            uploadRequest.withSSECustomerKey(customerKey);//error
