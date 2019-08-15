    public static SignedRequestResponse parse_signed_request(String input, String secret, int max_age) {
        String[] split = input.split("[.]", 2);
        SignedRequestResponse response = new SignedRequestResponse();
        response.setStatus(Status.Failure);

        String encoded_sig = split[0];
        String encoded_envelope = split[1];
        JSONParser parser = new JSONParser();
        Map envelope;
        try {
            envelope = (Map) parser.parse(new String(base64_url_decode(encoded_envelope)));
        } catch (Exception ex) {
            response.addError("ParsingError");
            return response;
        }

        response.setEnvelope(envelope);

        String algorithm = (String) envelope.get("algorithm");

        if (!algorithm.equals("HMAC-SHA256")) {
            response.addError("UnsupportedAlgorithm");
            return response;    // instant fail
        }

        byte[] key = secret.getBytes();
        SecretKey hmacKey = new SecretKeySpec(key, "HMACSHA256");
        Mac mac = null;
        try {
            mac = Mac.getInstance("HMACSHA256");
            mac.init(hmacKey);
        } catch (Exception e) {
            response.addError("DecryptionError");
            return response; // instant fail
        }

        byte[] digest = mac.doFinal(encoded_envelope.getBytes());
        try {

            if (!Arrays.equals(base64_url_decode(encoded_sig), digest)) {
                response.addError("InvalidSignature");
                return response; // instant fail
            }
        } catch (Exception ex) {
            response.addError("InvalidSignature");
            return response; // instant fail
        }

        if (((Long) envelope.get("issued_at")) < System.currentTimeMillis() / 1000 - max_age) {
            response.addError("Expired");
            response.setStatus(Status.PartialSuccess);
            return response;    // partial fail
        }

        response.setStatus(Status.Success);
        return response;
    }
}
