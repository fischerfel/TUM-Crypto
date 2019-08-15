        public class TCPSockets extends CordovaPlugin {
        private CallbackContext callbackContext;

        @Override
        public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {        
    //      PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);   
            this.callbackContext = callbackContext; 

    //      Log.d("TCPSockets", "Plugin Called");
            try {
                if (action.equals("sendMessage")) {

                    if (args != null) 
                    {
                        final int port  = args.getInt(0);
                        final String host = args.getString(1);
                        final String message = args.getString(2);
                        final int connectionTimeout = args.getInt(3);
                        final boolean secureConnection = args.getBoolean(4);                    

                        sendMessage(port, host, message, connectionTimeout, secureConnection);                                                  
                    } else {
    //                    return new PluginResult(PluginResult.Status.ERROR, "User did not specify host information");
                        callbackContext.error("User did not specify host information");
                        return true;
                    }
                } else {
    //                return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.error("Invalid Action");
                    return true;
                }
            } 

            catch (JSONException e) {
                Log.d("TCPSockets", "JSONException: " + e.getMessage());
    //          return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
                callbackContext.error("JSON Exception");
                return true;
            }

    //        return r;
    //      callbackContext.sendPluginResult(r);
            return true;
        }

        public void sendMessage(final int port, final String host, final String message, final int connectionTimeout, final boolean secureConnection)
        {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    String reply = "";
                    PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);

                    try {           
                        // If we don't want secure connections, then use Socket class
                        if(!secureConnection)
                        {
                            // Not SSL socket
                            Socket sock = new Socket(host, port);
                            Log.d("TCPSockets", "Socket created");
                            sock.setSoTimeout(connectionTimeout); // Time out all actions for 30 seconds

                            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                            Log.d("TCPSockets", "Created reader/writer");
                            out.println(message);
                            Log.d("TCPSockets", "Sent message");

                            reply = in.readLine();
                            Log.d("TCPSockets", "Received message: " + reply);

                            out.flush();

                            out.close();
                            in.close();
                            sock.close();
                        }
                        else // If we want secure connections, then use SSLSocket class
                        {               
                            // Create a trust manager that does not validate certificate chains
                            TrustManager[] trustAllCerts = new TrustManager[] {
                                new X509TrustManager() {
                                    public X509Certificate[] getAcceptedIssuers() {
                                        return null;                                    
                                    }

                                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                        // Trust always
                                    }

                                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                        // Trust always
                                    }
                                }
                            };

                            SSLContext sslContext = null;

                            try {
                                sslContext = SSLContext.getInstance("SSL");
                            } catch (NoSuchAlgorithmException e) {
                                Log.d("SSLTCPSockets", "No such algorithm");
    //                          return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result.setKeepCallback(false);
                                callbackContext.sendPluginResult(result);
                                return;
                            }

                            try {
                                sslContext.init(null, trustAllCerts, new SecureRandom());
                            } catch (KeyManagementException e) {
                                Log.d("SSLTCPSockets", "Key manager exception");
    //                          return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result.setKeepCallback(false);
                                callbackContext.sendPluginResult(result);
                                return;
                            }

                            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                            SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, port);
                            socket.setSoTimeout(connectionTimeout);
                            socket.setUseClientMode(true);

                            Log.d("SSLTCPSockets", "Connected to host");

                            SSLSession session = socket.getSession();

                            if (session.isValid())
                            {
                                Log.i(getClass().toString(), "Secure connection");
                                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                out.println(message);
                                Log.d("SSLTCPSockets", "Sent message");

                                reply = in.readLine();
                                Log.d("SSLTCPSockets", "Received message: " + reply);

                                out.flush();

                                out.close();
                                in.close();
                            }
                            else
                            {
                                Log.d("SSLTCPSockets", "Cannot create a secure connection");
    //                          return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                                result.setKeepCallback(false);
                                callbackContext.sendPluginResult(result);
                                return;
                            }

                            socket.close();
                        }

                        Log.d("TCPSockets", "Saving pluginResult");
                        result = new PluginResult(PluginResult.Status.OK, reply);
                        result.setKeepCallback(true);
                        callbackContext.sendPluginResult(result);                   
                    } 

                    catch (UnknownHostException e) {
                        Log.d("TCPSockets", "Unknown Host");
    //                  return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                        result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                        result.setKeepCallback(false);
                        callbackContext.sendPluginResult(result);
                        return;
                    } 

                    catch (java.net.SocketTimeoutException e) {
                        Log.d("TCPSockets", "Connection timed out");
    //                  return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Connection timed out. Please, try again");
                        result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Connection timed out. Please, try again");
                        result.setKeepCallback(false);
                        callbackContext.sendPluginResult(result);
                        return;
                    }

                    catch (IOException e) {
                        Log.d("TCPSockets", "IOException");
    //                  return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Unexpected error. Please, try again");
                        result = new PluginResult(PluginResult.Status.IO_EXCEPTION, "Unexpected error. Please, try again");
                        result.setKeepCallback(false);
                        callbackContext.sendPluginResult(result);
                        return;
                    } 
                }
            });
        }
    }
