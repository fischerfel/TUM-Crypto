package home.chat;

import *;

public class MyXMPP {

public static boolean connected = false;
public static boolean loggedin = false;
public static boolean isconnecting = false;
public static boolean isToasted = false;
private boolean chat_created = false;
private String serverAddress;
public static XMPPTCPConnection connection;
public static String loginUser;
public static String passwordUser;
Gson gson;
static MyService context;
public static MyXMPP instance = null;
public static boolean instanceCreated = false;
private Handler mHandler = new Handler();
public static ReconnectionManager connMgr;
int[] rt_arr = {2,2,2,5,5,10,10};
int curr_delay = 0;
public static ConnectivityManager cm = null;
public NetworkInfo activeNetwork = null;
public static Roster myRoster;

static ArrayList<ChatMessage> msg_array = new ArrayList<ChatMessage>(); //буфер сообщений для отправки
public static ArrayList<HashMap<String,String>> msg_queue = new ArrayList<>();
public static ArrayList<HashMap<String,String>> stat_list = new ArrayList<>();
public String senderName = "";

public MyXMPP(MyService context, String serverAdress, String logiUser,
              String passwordser) {
    this.serverAddress = serverAdress;
    this.loginUser = logiUser;
    this.passwordUser = passwordser;
    this.context = context;
    init();
}

public static MyXMPP getInstance(MyService context, String server,
                                 String user, String pass) {
    if (instance == null) {
        instance = new MyXMPP(context, server, user, pass);
        instanceCreated = true;
        Log.e("MyXMPP","create new instance");
    }
    return instance;
}

public org.jivesoftware.smack.chat.Chat Mychat;

ChatManagerListenerImpl mChatManagerListener;
MMessageListener mMessageListener;

String text = "";
String mMessage = "", mReceiver = "";

static {
    try {
        Class.forName("org.jivesoftware.smack.ReconnectionManager");
    } catch (ClassNotFoundException ex) {
        Log.e("E:","problem loading reconnection manager");
        // problem loading reconnection manager
    }
}

public void init() {
    gson = new Gson();
    mMessageListener = new MMessageListener();
    mChatManagerListener = new ChatManagerListenerImpl();
    initialiseConnection();
}

private void initialiseConnection() {

    cm =
            (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    activeNetwork = cm.getActiveNetworkInfo();

    XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
            .builder();
    config.setSendPresence(true);
    config.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.required);
    config.setCompressionEnabled(true);
    config.setServiceName(GlobalVariables.service);
    config.setHost(serverAddress);
    config.setPort(5222);
    config.setDebuggerEnabled(true);

    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        MemorizingTrustManager mtm = new MemorizingTrustManager(context);
        sc.init(null, new X509TrustManager[] { mtm }, new java.security.SecureRandom());
        config.setCustomSSLContext(sc);
        config.setHostnameVerifier(mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    } catch (KeyManagementException e) {
        throw new IllegalStateException(e);
    }
    connection = new XMPPTCPConnection(config.build());
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();
    connection.addConnectionListener(connectionListener);

    PingManager pingManager = PingManager.getInstanceFor(connection);
    pingManager.setPingInterval(900); // 15 min
    pingManager.registerPingFailedListener(new PingFailedListener(){
        @Override
        public void pingFailed() {
            // Do operation to handle if ping fail like force reconnect etc
            Log.e("PingManager","Ping Failed, reconnection");
            connected = false;
            chat_created = false;
            loggedin = false;
            //disconnect();
            connect("ping_manager");
        }

    });
    ServerPingWithAlarmManager.getInstanceFor(connection).isEnabled(); //для пинга во время глубокого сна
}

public void disconnect() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            connection.disconnect();
        }
    }).start();
}

public void connect(final String caller) {
    AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected synchronized Boolean doInBackground (Void...arg0){
            if (connection.isConnected())
                return false;
            isconnecting = true;
            if (isToasted)
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(context, caller + "=>connecting....", Toast.LENGTH_LONG).show();
                    }
                });
            Log.e("Connect() Function", caller + "=&gt;connecting....");

            try {
                connection.setPacketReplyTimeout(20000);
                // Enable automatic reconnection
                connMgr = ReconnectionManager.getInstanceFor(connection);
                connMgr.enableAutomaticReconnection();
                connMgr.setFixedDelay(2);
                //connMgr.setDefaultFixedDelay(1);
                //connMgr.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
                connection.connect();
                DeliveryReceiptManager dm = DeliveryReceiptManager
                        .getInstanceFor(connection);
                dm.setAutoReceiptMode(AutoReceiptMode.always);
                dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                    @Override
                    public void onReceiptReceived(final String fromid,
                                                  final String toid, final String msgid,
                                                  final Stanza packet) {

                    }
                });
                connected = true;
                MainActivity.fConn = true;
            } catch (IOException e) {
                if (isToasted)
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(
                                            context,
                                            "(" + caller + ")"
                                                    + "IOException: ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                Log.e("(" + caller + ")", "IOException: " + e.getMessage());
            } catch (SmackException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                      // Toast.makeText(context, "(" + caller + ")" + "SMACKException: ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("(" + caller + ")",
                        "SMACKException: " + e.getMessage());
                //mHandler.post(checkConn); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<-- Problem place
                //instance = null;
                //MyService.getInstance().destroyService(); 
            } catch (XMPPException e) {
                if (isToasted)

                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(
                                            context,
                                            "(" + caller + ")"
                                                    + "XMPPException: ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                Log.e("connect(" + caller + ")",
                        "XMPPException: " + e.getMessage());

            }
            return isconnecting = false;
        }
    } ;
    connectionThread.execute();
}

public static void login() {
    try {
        Log.e(loginUser,passwordUser);
        loginUser = GlobalVariables.user_id;
        passwordUser = GlobalVariables.user_password;
        connection.login(loginUser, passwordUser);
        Log.e("LOGIN", "Yey! We're connected to the Xmpp server!");
        sendBufMessages();
        myRoster = Roster.getInstanceFor(connection);
        if (!myRoster.isLoaded()) {
            try{ myRoster.reloadAndWait(); }
            catch (Exception e) {System.out.println(e);}
        }
        myRoster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
        myRoster.addRosterListener(new RosterListener() {
            public void entriesAdded(Collection<String> addresses) {}
            public void entriesDeleted(Collection<String> addresses) {}
            public void entriesUpdated(Collection<String> addresses) {}
            public void presenceChanged(Presence presence) {
                Log.e("Roster","Presence changed: " + presence.getFrom() + " " + presence);
                String uname = presence.getFrom();
                int pos = uname.indexOf('@',0);
                uname = uname.substring(0,pos);
                Presence.Type ptype = presence.getType(); //Presence.type.available
                for(int i=0;i<stat_list.size();i++){
                    HashMap<String,String> item = new HashMap<String, String>();
                    item = stat_list.get(i);
                    if (uname.equals(item.get("user_id").toString())) { stat_list.remove(i); break;}
                }
                HashMap<String,String> item = new HashMap<>();
                item.put("user_id",uname);
                if (ptype == Presence.Type.available){ item.put("onl","true"); }
                if (ptype == Presence.Type.unavailable){ item.put("onl","false"); }
                stat_list.add(0,item);
                if (MainActivity.chatlist_selected) { ChatList.getInstance().startStatProc(); }
                if (GlobalVariables.onchat == true & GlobalVariables.vuser_id.equals(uname)){
                    if (ptype == Presence.Type.available) { Chat.setUserStatus("onl"); }
                    if (ptype == Presence.Type.unavailable) {Chat.setUserStatus("offl");}
                }
            }
        });
    } catch (XMPPException | SmackException | IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private class ChatManagerListenerImpl implements ChatManagerListener {
    @Override
    public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                            final boolean createdLocally) {
        if (!createdLocally)
            chat.addMessageListener(mMessageListener);

    }

}

public void sendMessage(ChatMessage chatMessage) {
.....
}

public class XMPPConnectionListener implements ConnectionListener {
    @Override
    public void connected(final XMPPConnection connection) {

        Log.e("xmpp", "Connected!");
        connected = true;
        curr_delay = 0; connMgr.setFixedDelay(2);
            login();
    }

    @Override
    public void connectionClosed() {
        if (isToasted)

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Toast.makeText(context, "ConnectionCLosed!",
                            Toast.LENGTH_SHORT).show();

                }
            });
        Log.d("xmpp", "ConnectionCLosed!");
        connected = false;
        chat_created = false;
        loggedin = false;
    }

    @Override
    public void connectionClosedOnError(Exception arg0) {
        if (isToasted)
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, "ConnectionClosedOn Error!!",
                            Toast.LENGTH_SHORT).show();

                }
            });
        Log.e("xmpp", "ConnectionClosedOn Error!");
        connected = false;
        chat_created = false;
        loggedin = false;
    }

    @Override
    public void reconnectingIn(int arg0) {

        Log.e("xmpp", "Reconnectingin " + arg0);
        if (arg0==0 & curr_delay<5){
            curr_delay++;
            connMgr.setFixedDelay(rt_arr[curr_delay]);
        }
        loggedin = false;
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        if (isToasted)

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    Toast.makeText(context, "ReconnectionFailed!",
                            Toast.LENGTH_SHORT).show();

                }
            });
        Log.d("xmpp", "ReconnectionFailed!");
        connected = false;

        chat_created = false;
        loggedin = false;
    }

    @Override
    public void reconnectionSuccessful() {
        if (isToasted)

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Toast.makeText(context, "REConnected!",
                            Toast.LENGTH_SHORT).show();

                }
            });
        Log.d("xmpp", "ReconnectionSuccessful");
        curr_delay = 0; connMgr.setFixedDelay(2);
        connected = true;
        //MainActivity.fConn = true;
        chat_created = false;
        loggedin = false;
    }

    @Override
    public void authenticated(XMPPConnection arg0, boolean arg1) {
        Log.d("xmpp", "Authenticated!");
        loggedin = true;

        ChatManager.getInstanceFor(connection).addChatListener(
                mChatManagerListener);

        chat_created = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
        if (isToasted)

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Toast.makeText(context, "Connected!",Toast.LENGTH_SHORT).show();

                }
            });
    }
}

private class MMessageListener implements ChatMessageListener {

    @Override
    public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                               final Message message) {
        Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                + message);

...
        }
    }



private Runnable checkConn = new Runnable() {
    public void run() {
        try {
            Socket socket = new Socket(host, 5222);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch(Exception e){Log.e("Socket",e.toString());}
        disconnect();
        Log.e("MyXMPP","disconnect");
        mHandler.postDelayed(checkConn2,3000);
    }
};
private Runnable checkConn2 = new Runnable() {
    public void run() {
        connect("After SmackException");
    }
};


}
