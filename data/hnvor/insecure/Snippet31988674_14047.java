        httpURLConnection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                // return false;
                return true;
            }
        });
