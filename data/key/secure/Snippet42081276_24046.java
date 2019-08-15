public String calculateSecretHash(String userPoolclientId, String userPoolclientSecret, String userName) {
        if (userPoolclientSecret == null) {
            return null;
        }

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolclientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolclientId.getBytes(StandardCharsets.UTF_8));
            return Encoding.encodeBase64(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating ");
        }
    }
