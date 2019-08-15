private void HttpLogin() {
    // Define Progress Dialog
    ProgressDialog progressDialog = ProgressDialog.show(this, "Verifying", "Loading..", true);

    // Variable
    EditText username = (EditText) findViewById(R.id.txtUsername);
    EditText password = (EditText) findViewById(R.id.txtPassword);
    Spinner company = (Spinner)findViewById(R.id.ddlCompany);
    StringBuilder sb = new StringBuilder();
    String title = "";
    CompanyInfo companyInfo = (CompanyInfo)company.getSelectedItem();
    final String user = username.getText() + "";
    final String pass = password.getText() + "";
    final String domain = companyInfo.getCompanyDomain();
    String authorization = domain + "\\" + user + ":" + pass;

    TrustManager trustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() { return null; }
    };

    // Check
    if (user != "" && pass != "" && domain != "") {

        // Error Handling
        try {
            // Reset Variable
            sb = new StringBuilder();
            title = "";

            // Certificate
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { trustManager }, new SecureRandom());

            // Add Socket Factory
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Accept All Host Verifier
            HostnameVerifier allHostValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) { return true; }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostValid);

            // Set Urls
            URL url = new URL("https://YourUrlAddess/");

            // Open Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(Constant._REQUEST_METHOD_GET);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP));
            conn.connect();

            // Check
            if (conn.getResponseCode() == 401) {
                title = "Login Failed";
                sb.append("Login username or password invalid.");
                conn.disconnect();
            }
            else if(conn.getResponseCode() == 200)
            {
                title = "Authorized";
                sb.append("You have login to SharePoint");
                conn.disconnect();

                Intent intent = new Intent(this, eLeave_HomeActivity.class);
                intent.putExtra(_Username, user);
                startActivity(intent);
            }
            else
            {
                title = "Unknown Error";
                sb.append(conn.getResponseCode() + ": " + conn.getResponseMessage());
            }


        } catch (Exception ex) { sb.append(ex.toString()); }
    }
    else
    {
        title = "Login Failed";
        sb.append("Username or Password or company cannot be empty");
    }
    progressDialog.dismiss();
    AlertMessage(title, sb.toString());
}
