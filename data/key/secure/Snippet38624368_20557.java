final String ALGORITHM = "HmacSHA256";
        Mac mac = Mac.getInstance(ALGORITHM);
        SecretKeySpec secret = new SecretKeySpec(authorizationKey.getBytes(), ALGORITHM);

        mac.init(secret);
        byte[] digest = mac.doFinal(body.getBytes());

        hash = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        return signature.equals(hash);
