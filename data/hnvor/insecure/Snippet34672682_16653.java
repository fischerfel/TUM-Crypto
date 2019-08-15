OkHttpClient client = new OkHttpClient();
client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName
                            + "' is different to SSLSession host '"
                            + session.getPeerHost() + "'.");
                }
                return true;
            }
        });
