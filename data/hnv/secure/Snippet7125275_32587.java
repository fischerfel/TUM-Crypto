    private String sendPostRequest(String requeststring) {

    DataInputStream dis = null;
    StringBuffer messagebuffer = new StringBuffer();

    HttpURLConnection urlConnection = null;

    //Conexion por HTTPS
    HttpsURLConnection urlHttpsConnection = null;

    try {
        URL url = new URL(this.getServerURL());

        //urlConnection = (HttpURLConnection) url.openConnection();         

        //Si necesito usar HTTPS
        if (url.getProtocol().toLowerCase().equals("https")) {

            trustAllHosts();

            //Creo la Conexion
            urlHttpsConnection = (HttpsURLConnection) url.openConnection();

            //Seteo la verificacion para que NO verifique nada!!
            urlHttpsConnection.setHostnameVerifier(DO_NOT_VERIFY);

            //Asigno a la otra variable para usar simpre la mism
            urlConnection = urlHttpsConnection;

        } else {

            urlConnection = (HttpURLConnection) url.openConnection();
        }

//Do the same like up
