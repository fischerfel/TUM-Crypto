private void connect(HttpsURLConnection uc) throws IOException {
    uc.setHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String host, SSLSession sess) {
            return true;
        }
    });

    uc.setRequestProperty("Content-Type", "application/json");
    uc.setReadTimeout(15 * 1000); // 15 seconds
    uc.connect();
}
