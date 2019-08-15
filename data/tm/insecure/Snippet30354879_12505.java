    package com.habel.sockets;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;

    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.SSLSocket;
    import javax.net.ssl.SSLSocketFactory;
    import java.net.Socket;
    import java.net.UnknownHostException;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;

    import java.security.KeyManagementException;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.security.cert.X509Certificate;

    import org.json.JSONArray;
    import org.json.JSONException;

    import android.util.Log;

    import org.apache.cordova.CordovaPlugin;
    import org.apache.cordova.PluginResult;
    import org.apache.cordova.CallbackContext;

    public class TCPSockets extends CordovaPlugin { 
        public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);

    //      Log.d("TCPSockets", "Plugin Called");
            try {
                if (action.equals("sendMessage")) {

                    if (args != null) 
                    {
                        int port  = args.getInt(0);
                        String host = args.getString(1);
                        String message = args.getString(2);
                        int connectionTimeout = args.getInt(3);
                        boolean secureConnection = args.getBoolean(4);

                        r = sendMessage(port, host, message, connectionTimeout, secureConnection);
                        r.setKeepCallback(true);
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
            callbackContext.sendPluginResult(r);
            return true;
        }

        public PluginResult sendMessage(int port, String host, String message, int connectionTimeout, boolean secureConnection)
        {
            String reply = "";

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

                    SSLContext sslContext;

                    try {
                        sslContext = SSLContext.getInstance("SSL");
                    } catch (NoSuchAlgorithmException e) {
                        Log.d("SSLTCPSockets", "No such algorithm");
                        return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                    }

                    try {
                        sslContext.init(null, trustAllCerts, new SecureRandom());
                    } catch (KeyManagementException e) {
                        Log.d("SSLTCPSockets", "Key manager exception");
                        return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                    }

                    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                    SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, port);
                    socket.setSoTimeout(connectionTimeout);
                    socket.setUseClientMode(true);

                    Log.d("SSLTCPSockets", "Connected to status host");

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
                        return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
                    }

                    socket.close();
                }

                return new PluginResult(PluginResult.Status.OK, reply);
            } 

            catch (UnknownHostException e) {
                Log.d("TCPSockets", "Unknown Host");
                return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Cannot connect to server. Please, try again");
            } 

            catch (java.net.SocketTimeoutException e) {
                Log.d("TCPSockets", "Connection timed out");
                return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Connection timed out. Please, try again");
            }

            catch (IOException e) {
                Log.d("TCPSockets", "IOException");
                return new PluginResult(PluginResult.Status.IO_EXCEPTION, "Unexpected error. Please, try again");
            } 
        }
    }
