...
    // Prozess/Thread für Internetverbindung/Daten abrufen
private class MyThread extends Thread {
    @Override
    public void run() {

        if (stop) {
            if (DEBUG) {
                Log.d(TAG, "Stop ist true");
            }
            // nicht weitermachen, da erst der User unter Optionen
            // die
            // Einstellungen ändern muss
        } else {
            int TIMEOUT_VALUE = 3000;
            for (int z = 0; z < datum.length; z++) {
                Resultat = null;
                Endres = null;
                if (DEBUG) {
                    Log.d(TAG, "Endres " + Endres + " " + Resultat);
                    Log.d(TAG, "z: " + z);
                }

                statusZaehler = statusZaehler + 100 / statusZ;
                if (DEBUG) {
                    Log.d(TAG, o + " thread start statusZaehler "
                            + statusZaehler);
                }
                o++;

                if (DEBUG) {
                    Log.d(TAG, "run1");
                }

                // try {
                if (DEBUG) {
                    Log.d(TAG, "run2");
                }
                // ### Datumseingabe bzw. Variable
                pruefdatum = datum[z];

                if (DEBUG) {
                    Log.d(TAG, "Prüfdatum: " + pruefdatum);
                }

                // ### URL Abfragen & Auslesen
                // URL url = null;

                if (!NetworkInfo(getBaseContext())) {
                    showDialog(DIALOG_ALERT);
                    progressThread.setState(ProgressThread.STATE_DONE);
                    stop1 = true;
                    break;
                } else {

                    // Datenabruf

                    // try {

                    if (DEBUG) {
                        Log.d(TAG, "Daten aus dem Internet abrufen "
                                + pruefdatum);
                    }

                    javax.net.ssl.SSLContext sslContext = null;
                    javax.net.ssl.SSLSocketFactory socketFactory = null;
                    try {

                        sslContext = SSLContext.getInstance("TLS"); // (1)
                        sslContext
                                .init(null,
                                        new TrustManager[] { de.mdisco.ssl.TrustManagerFactory
                                                .get() },
                                        new SecureRandom()); // (2)
                        socketFactory = sslContext.getSocketFactory();
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Error in ssl connection " + e.toString());
                    }

                    HttpsURLConnection
                            .setDefaultSSLSocketFactory(socketFactory); // (3)
                    HttpsURLConnection
                            .setDefaultHostnameVerifier(new de.mdisco.ssl.TestX509HostnameVerifier()); // (4)

                    HttpsURLConnection httpsUrlConnection = null;

                    try {

                        httpsUrlConnection = (HttpsURLConnection) new URL(
                                "https://....."+ pruefdatum + "&asi=")
                                .openConnection();

                        httpsUrlConnection.setConnectTimeout(TIMEOUT_VALUE);
                        httpsUrlConnection.setReadTimeout(TIMEOUT_VALUE);

                        httpsUrlConnection.connect(); // (5)

                    } catch (ConnectTimeoutException e) {
                        Log.e("Timeout Exception: ", e.toString());
                        Log.d(TAG, "fehler: " + e.getMessage()
                                + " More than " + TIMEOUT_VALUE
                                + " elapsed.");
                        myProgressDialog.dismiss();
                        Connfehler();
                        stop1 = true;
                        break;
                    } catch (SocketTimeoutException ste) {
                        Log.e("Timeout Exception: ", ste.toString());
                        Log.d(TAG, "fehler: " + ste.getMessage()
                                + " More than " + TIMEOUT_VALUE
                                + " elapsed.");
                        myProgressDialog.dismiss();
                        Connfehler();
                        stop1 = true;
                        break;
                    } catch (MalformedURLException me) {
                        Log.d(TAG,
                                "fehler in Internetverbindung: "
                                        + me.getMessage());
                        myProgressDialog.dismiss();
                        Connfehler();
                        stop1 = true;
                        break; // finish();

                    } catch (IllegalStateException cause) {
                        Log.e(TAG,
                                "Error in https connection Illegal State "
                                        + cause.toString());

                    } catch (Exception e) {
                        Log.e(TAG,
                                "Error in https connection " + e.toString());
                    }
                    // HTML der Webseite auslesen:

                    String lesezeile = null;
                    try {
                        BufferedReader buffReader = new BufferedReader(
                                new InputStreamReader(httpsUrlConnection
                                        .getInputStream()),
                                8 * 1024);

                        while ((lesezeile = buffReader.readLine()) != null) {
                            if (Resultat == null) {

                                Resultat = lesezeile;

                            } else {

                                Resultat = Resultat + lesezeile;

                            }

                        }

                        if (DEBUG) {
                            Log.d(TAG, "Edatum " + pruefdatum);
                            Log.d(TAG, Resultat + "");
                        }
                        buffReader.close();
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Error buffered Reader result "
                                        + e.toString());
                    }
                    httpsUrlConnection.disconnect();
                    Endres = Resultat;
                    statusZaehler = statusZaehler + 100 / statusZ;
                    if (DEBUG) {
                        Log.d(TAG, o + "thread Übergabe statusZaehler "
                                + statusZaehler);
                    }
                    o++;
                    parse();

                    /*
                     * } catch (MalformedURLException me) { Log.d(TAG,
                     * "fehler in Internetverbindung: " + me.getMessage());
                     * myProgressDialog.dismiss(); Connfehler(); stop1 =
                     * true; break; // finish(); }
                     */

                    if (DEBUG) {
                        Log.d(TAG, "Ende StartInternet");

                        Log.d(TAG, "parse+offline ende");
                    }

                }
                /*
                 * } catch (ConnectTimeoutException e) {
                 * Log.e("Timeout Exception: ", e.toString()); Log.d(TAG,
                 * "fehler: " + e.getMessage() + " More than " +
                 * TIMEOUT_VALUE + " elapsed."); myProgressDialog.dismiss();
                 * Connfehler(); stop1 = true; break; } catch
                 * (SocketTimeoutException ste) {
                 * Log.e("Timeout Exception: ", ste.toString()); Log.d(TAG,
                 * "fehler: " + ste.getMessage() + " More than " +
                 * TIMEOUT_VALUE + " elapsed."); myProgressDialog.dismiss();
                 * Connfehler(); stop1 = true; break; } catch (Exception e)
                 * { Log.e(TAG, "Error in http connection " + e.toString());
                 * 
                 * }
                 */
            }

        }
...
