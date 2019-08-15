    try {
        conn = (HttpURLConnection)url.openConnection();
        if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection)conn).setSSLSocketFactory(SimpleSSLSocketFactory.getSocketFactory());
                // EDIT: added this line, the HV has to be set on connection, not on the factory.
                ((HttpsURLConnection)conn).setHostnameVerifier(new NaiveHostnameVerifier());
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type","application/x-www-form-urlencoded");
        conn.connect();

        StringBuffer sbContent = new StringBuffer();
        // (snip)
        DataOutputStream stream = new DataOutputStream(conn.getOutputStream ());
        stream.writeBytes(sbContent.toString());
        stream.flush();
        stream.close();
    } catch (ClassCastException e) {
        log.error("The URL does not seem to point to a HTTP connection");
        return null;
    } catch (IOException e) {
        log.error("Error accessing the requested URL", e);
        return null;
    }
