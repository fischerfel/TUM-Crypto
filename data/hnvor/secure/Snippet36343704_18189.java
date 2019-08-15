URLConnection c = new URL("https://example.com/").openConnection();
((HttpsURLConnection)c).setHostnameVerifier( new HostnameVerifier() {
        public boolean verify( String s, SSLSession sess ) {
            return false; // or true, won't matter for this
        }
});
InputStream i = c.getInputStream(); // Exception thrown here
...
