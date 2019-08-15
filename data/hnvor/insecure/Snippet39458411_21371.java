okHttpClient.setHostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
});

OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.hostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });
OkHttpClient client = builder.build();
