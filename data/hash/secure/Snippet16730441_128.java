public void registerClient(String username, String password,
            ResponseEvent evnt) throws Exception {

        cSeqHeader = headerFactory.createCSeqHeader(1, Request.REGISTER);
//      request = messageFactory.createRequest(requestURI, Request.REGISTER,
//              callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders,
//              maxForwards);

        request = this.messageFactory.createRequest("REGISTER sip:"
                + toHost + " SIP/2.0\r\n\r\n");
        request.addHeader(callIdHeader);
        request.addHeader(cSeqHeader);
        request.addHeader(fromHeader);
        request.addHeader(toHeader);
        request.addHeader(maxForwards);
        request.addHeader(viaHeader);
        request.addHeader(contactHeader);

        request.addHeader(contactHeader);
        if (evnt != null) {
            request.addHeader(createAuthHeader(username, password, evnt, Request.REGISTER));
        }

        if (transaction == null) {
            transaction = sipProvider.getNewClientTransaction(request);
        }

        transaction.sendRequest();
    }

    private AuthenticationHeader createAuthHeader(String username,
            String password, ResponseEvent response, String requestMethod)
            throws ParseException, NoSuchAlgorithmException {

        AuthenticationHeader header = (AuthenticationHeader) headerFactory
                .createAuthorizationHeader("Digest");

        // get authentication type and nounce from wwwAuthheader we receive from
        // response object
        WWWAuthenticateHeader wwwAuthHeader = (WWWAuthenticateHeader) response
                .getResponse().getHeader(WWWAuthenticateHeader.NAME);

        String nonce = wwwAuthHeader.getNonce();
        String qop = wwwAuthHeader.getQop();
        String realm = wwwAuthHeader.getRealm();
        String opaque = wwwAuthHeader.getOpaque();


        // prepare and md5 username password and realm.
        MessageDigest messageDigest = MessageDigest.getInstance(wwwAuthHeader
                .getAlgorithm());
        ;
        String message = String.format("%1$s:%2$s:%3$s", username, realm,
                password);
        String ha1 = toHexString(messageDigest.digest(message.getBytes()));

        // prepare second md5 value for request method and request URI used
        String message2 = String.format("%1$s:%2$s", requestMethod, requestURI);
        String ha2 = toHexString(messageDigest.digest(message2.getBytes()));

        String responseValue;

        // check what type of digest we need and apply it auth header by
        // checking qop
        if (qop != null && qop.equals(AUTH)) {
            // Create the final MD5 string using ha1 + “:” + nonce + “:” +
            // nonceCount + “:” + cNonce + “:” + qop + “:” + ha2
            // responseValue = String.format("%1$s:%2$s:%3$s:%4$s:%5$s:",
            // ha1,nonce,)

        } else {
            // Create the final MD5 string using ha1 + “:” + nonce + “:” + ha2
            responseValue = String.format("%1$s:%2$s:%3$s", ha1, nonce, ha2);
            String responseConverted = toHexString(messageDigest.digest(responseValue
                    .getBytes()));
            System.out.println(responseConverted);
            System.out.println(wwwAuthHeader.getAlgorithm());
            System.out.println(username);
            System.out.println(nonce);
            System.out.println(realm);
            System.out.println(responseConverted);

            header.setAlgorithm(wwwAuthHeader.getAlgorithm());
            header.setUsername(username);
            header.setNonce(nonce);
            header.setRealm(realm);
            // header.setQop(qop);
            header.setURI(request.getRequestURI());
            header.setResponse(responseConverted);
            if(opaque != null) {
                header.setOpaque(opaque);
            }
        }

        return header;

    }

    private static final char[] toHex = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * From Nist/JAIN examples: convert an array of bytes to an hexadecimal
     * string
     * 
     * @return a string (length = 2 * b.length)
     * @param b
     *            bytes array to convert to a hexadecimal string
     */
    static String toHexString(byte b[]) {
        int pos = 0;
        char[] c = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            c[pos++] = toHex[(b[i] >> 4) & 0x0F];
            c[pos++] = toHex[b[i] & 0x0f];
        }
        return new String(c);
    }
