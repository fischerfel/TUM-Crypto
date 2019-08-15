private ResourceOwnerPasswordResourceDetails getAuthDetails(String userName, String userPwd)
{

    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
    {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }
    }};

    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }

    ResourceOwnerPasswordResourceDetails authDetails = new ResourceOwnerPasswordResourceDetails();
    authDetails.setAccessTokenUri(LoggerAndReader.getInstance().getoAuth2tokenRequestUrl());
    authDetails.setClientId(LoggerAndReader.getInstance().getoAuth2ClientId());
    authDetails.setClientSecret(LoggerAndReader.getInstance().getoAuth2SecretToken());
    authDetails.setGrantType(LoggerAndReader.getInstance().getOauth2granttype());
    if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userPwd)) {
        authDetails.setUsername(userName);
        authDetails.setPassword(userPwd);
    } else {
        authDetails.setUsername(LoggerAndReader.getInstance().getOauth2UserName());
        authDetails.setPassword(LoggerAndReader.getInstance().getOauth2password());
    }
    // authDetails.setScope(Arrays.asList(new String[] {"cn mail sn givenname uid employeeNumber"}));
    return authDetails;
}
