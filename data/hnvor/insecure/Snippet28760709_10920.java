this.client.setHostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        //TODO: Make this more restrictive
        return true;
    }
});
