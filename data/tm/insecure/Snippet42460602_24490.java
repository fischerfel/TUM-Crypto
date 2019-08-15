Use to import self-signed SSL certificate to Volley on Android 4.1+
It will make volley to Trust all SSL certificates
I hope it will work.

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.player_artists_dialog, container, false);

    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    getDialog().setCancelable(true);

    model = this.getArguments().getParcelable("model");

    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    listView = (ListView) rootView.findViewById(R.id.listView);

    listView.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);

        //use trustAllCertificates here.
   trustAllCertificates();


    StringRequest request = new StringRequest(Request.Method.POST,
            ".. url ..",

            new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show(); // show null

                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError ex) {
                }
            }) {

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("a", String.valueOf(model.getTr()));
            params.put("b", String.valueOf(model.getCat()));
            return params;
        }

    };

    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
    queue.add(request);

    return rootView;
}
 private void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
