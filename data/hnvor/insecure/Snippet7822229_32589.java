URL req = new URL(getUrl);

HttpsURLConnection con = (HttpsURLConnection) req.openConnection();

con.setHostnameVerifier(new HostnameVerifier()
{

    public boolean verify(String hostname, SSLSession session)
    {
        return true; //My decision
    }
});
