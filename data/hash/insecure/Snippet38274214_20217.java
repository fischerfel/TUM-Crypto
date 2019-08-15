private byte[] getSignedParams(ChannelBuffer params)
        throws NoSuchAlgorithmException, DigestException, 
        SignatureException, InvalidKeyException {
    byte[] signedParams = null;
    ChannelBuffer signAlg = ChannelBuffers.buffer(2);
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    switch (session.cipherSuite.sign) {
        case rsa:
            signAlg.writeByte(2); // 2 for SHA1
            sha.update(clientRandom);
            sha.update(serverRandom);
            sha.update(params.toByteBuffer());
            md5.update(clientRandom);
            md5.update(serverRandom);
            md5.update(params.toByteBuffer());
            signedParams = concat(md5.digest(), sha.digest());
        break;
    }
    signAlg.writeByte(session.cipherSuite.sign.value); // for RSA he byte is one
    ChannelBuffer signLength = ChannelBuffers.buffer(2);
    signLength.writeShort(signedParams.length);
    return concat(signAlg.array(),concat(signLength.array(),signedParams));
}
