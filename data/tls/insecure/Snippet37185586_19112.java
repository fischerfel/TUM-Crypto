public static void trustSSLCertificate(final Activity mActivity, final DownloadPortalTask task){
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        chain[0].checkValidity();
                    } catch (final Exception e) {

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                String message = "There a problem with the security certificate for this web site.";
                                message += "\nDo you want to continue anyway?";
                                alertDialog.setTitle("SSL Certificate Error");
                                alertDialog.setMessage(message);
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        acceptSSL = true;
                                        return;

                                    }
                                });

                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        acceptSSL = true;
                                        task.onInterruptedDownload();
                                    }
                                });
                                alertDialog.show();

                            }

                        });

                        while( !acceptSSL){
                            try{
                                Thread.sleep(1000);
                            } catch( InterruptedException er) { }
                        }

                    }
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }