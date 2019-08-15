public class ChatActivity extends Activity {
public static final String TAG = ChatActivity.class.getSimpleName();
EditText edMessage;
Button sendMessage;
private Socket mSocket;
String sID, lID, md5StringRoomID, message, friendName, loggedInUser;
String frndID;
int smallerID, largerID;
AlmaChatDatabase almaChatDatabase;
// Chat messages list adapter
private MessagesListAdapter adapter;
private List<Message> listMessages;
private ListView listViewMessages;
boolean isSelf = false; // to check whether the message is owned by you or not.true means message is owned by you .
Message msg;
int loggedInUserID;
private String URL_FEED_Message = "";
APIConfiguration apiConfiguration;

{
    try {
        mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        Log.e("Socket", String.valueOf(mSocket));
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    sendMessage = (Button) findViewById(R.id.btnSendMessage);
    almaChatDatabase = new AlmaChatDatabase(getApplicationContext());
    loggedInUserID = almaChatDatabase.getUserID(); // Getting ID of the Logged in user from the database
    Log.e("UserID", "Id of Logged in user  " + loggedInUserID);

    listMessages = new ArrayList<Message>();
    adapter = new MessagesListAdapter(getApplicationContext(), listMessages);
    listViewMessages = (ListView) findViewById(R.id.list_view_messages);
    listViewMessages.setAdapter(adapter);

    // Getting the ID of the friend from the previous screen using getExtras
    Bundle bundle = getIntent().getExtras();
    frndID = bundle.getString("ID");
    Log.e("FriendID", frndID);
    final int friendID = Integer.parseInt(frndID);
    friendName = bundle.getString("name");
    Log.e("FriendName", friendName);
    loggedInUser = almaChatDatabase.getUserName(); // Name of logged in user
    Log.e("LoggedInUser", loggedInUser);
    // Converting first lowercase letter of every word in Uppercase
    final String loggedInUpper = upperCase(loggedInUser);
    //To find the current time
    Date d = new Date();
    final long time = d.getTime();
    // Comparing the loggedInUserId and friendID
    if (friendID < loggedInUserID) {
        smallerID = friendID;
        largerID = loggedInUserID;
    } else {
        smallerID = loggedInUserID;
        largerID = friendID;
    }
    sID = String.valueOf(smallerID);
    lID = String.valueOf(largerID);
    String combinedID = sID + lID;
    Log.e("combined ID", combinedID);
    md5StringRoomID = convertPassMd5(combinedID); // Encrypting the combinedID to generate Room ID
    Log.e("md5StringRoomID", md5StringRoomID);

    // Using the API for loading old chat messages
    apiConfiguration = new APIConfiguration();
    String api_message = apiConfiguration.getApi_message(); // Getting the API of messages
    URL_FEED_Message = api_message + md5StringRoomID; // md5String is the encrypted room ID here
    Log.e("URL_FEED_MESSAGE", URL_FEED_Message);

    Log.e("Network request", "Fresh Request");
    // We first check for cached request
    Cache cache = AppController.getInstance().getRequestQueue().getCache();
    Cache.Entry entry = cache.get(URL_FEED_Message);
    if (entry != null) {
        // fetch the data from cache
        try {
            String data = new String(entry.data, "UTF-8");
            try {
                parseJsonFeed(new JSONArray(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    } else {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_FEED_Message, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("JsonArray", String.valueOf(jsonArray));
                if (jsonArray != null) {
                    parseJsonFeed(jsonArray);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ErrorResponse", String.valueOf(volleyError));
            }
        }
        );
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    edMessage = (EditText) findViewById(R.id.edtMessage);
    //Listening on Events
    mSocket.on(Socket.EVENT_CONNECT, onConnect);
    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
    mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
    mSocket.on("send:notice", onReceive); // Listening event for receiving messages
    mSocket.connect(); // Explicitly call connect method to establish connection here
    mSocket.emit("subscribe", md5StringRoomID);

    sendMessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //mSocket.emit("subscribe", md5String);
            message = edMessage.getText().toString().trim();
            Log.e("messageSent", message);
            if (!message.equals("")) {
                edMessage.setText(" ");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("room_id", md5StringRoomID);
                    jsonObject.put("user", loggedInUpper);
                    jsonObject.put("id", friendID);
                    jsonObject.put("message", message);
                    jsonObject.put("date", time);
                    jsonObject.put("status", "sent");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isSelf = true; // Boolean isSelf is set to be true as sender of the message is logged in user i.e. you
                attemptToSend(loggedInUpper, message, isSelf);
                mSocket.emit("send", jsonObject); // owner i.e LoggedIn user is sending the message
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please enter some text", Toast.LENGTH_LONG).show();

                    }
                });
            }
        }

    });

}

//Adding message in the arrayList
public void attemptToSend(String senderName, String message, boolean isSelf) {
    msg = new Message(senderName, message, isSelf);
    listMessages.add(msg);
    adapter.notifyDataSetChanged();
    playBeep();
}

// Playing sound when the message is sent by the owner
public void playBeep() {
    try {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// encrypting string into MD5
public static String convertPassMd5(String pass) {
    String password = null;
    MessageDigest mdEnc;
    try {
        mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(pass.getBytes(), 0, pass.length());
        pass = new BigInteger(1, mdEnc.digest()).toString(16);
        while (pass.length() < 32) {
            pass = "0" + pass;
        }
        password = pass;
    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    }
    return password;
}

// Converting first lowercase letter of every word in Uppercase
String upperCase(String source) {
    StringBuffer res = new StringBuffer();
    String[] strArr = source.split(" ");
    for (String str : strArr) {
        char[] stringArray = str.trim().toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        str = new String(stringArray);
        res.append(str).append(" ");
    }
    return res.toString().trim();
}

// Event Listeners
private Emitter.Listener onConnect = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        Log.e("Socket", "Connected");
    }
};


private Emitter.Listener onConnectionError = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        Log.e("Error", "Error in connecting server");
    }
};
private Emitter.Listener onDisconnect = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        Log.e("Disconnect", "Socket Disconnected");
    }
};

// Event Listener for receiving messages
private Emitter.Listener onReceive = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
        Log.e("Receive", "Message received");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                Log.e("DATA", String.valueOf(data));
                try {
                    JSONArray ops = data.getJSONArray("ops");
                    for (int i = 0; i < ops.length(); i++) {
                        JSONObject object = ops.getJSONObject(i);
                        String roomID = object.getString("room_id");
                        Log.e("RoomID", roomID); // Getting room ID from JSON array
                        Log.e("Md5RoomID", md5StringRoomID); // Getting room id which we have created using logged in user ID and room id of the user through which chat has to be done
                        //Comparing the room IDs
                        if (md5StringRoomID.equals(roomID)) {
                            String senderName = object.getString("user");
                            Log.e("Sender Name", senderName);
                            String senderID = object.getString("id");
                            Log.e("SenderID", senderID);
                            JSONObject message = object.getJSONObject("message");
                            String messageReceived = message.getString("text");
                            Log.e("Message Received", messageReceived);
                            String loggedInUSerNAme = almaChatDatabase.getUserName();
                            //If the message is sent by the owner to other from webapp ,then we need to check whether the sender is the loggedinUSer in the App or not and we will right align the messages .
                            if (loggedInUSerNAme.equalsIgnoreCase(senderName)) {
                                isSelf = true;
                                msg = new Message(senderName, messageReceived, isSelf);
                                listMessages.add(msg);
                                Log.e("List Elements", String.valueOf(listMessages));
                                adapter.notifyDataSetChanged();
                                playBeep();
                            } else {
                                isSelf = false;
                                msg = new Message(senderName, messageReceived, isSelf);
                                listMessages.add(msg);
                                Log.e("List Elements", String.valueOf(listMessages));
                                adapter.notifyDataSetChanged();
                                playBeep();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Playing sound when the message is sent by other
    public void playBeep() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

};

// Parsing JSon Array which corresponds to the old chat messages
public void parseJsonFeed(JSONArray jsonArray) {
    for (int i = 0; i < jsonArray.length(); i++) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String roomID = jsonObject.getString("room_id");
            Log.e("RoomID", roomID);
            Log.e("Md5RoomID", md5StringRoomID);
            // If Room ID(created using id of logged in user and id of friend) matches with the room id obtained from JSON String
            if (md5StringRoomID.equals(roomID)) {
                String userName = jsonObject.getString("user");
                Log.e("Name", userName);
                String loggedInUSerNAme = almaChatDatabase.getUserName();
                Log.e("LoggedInUSer", loggedInUSerNAme);
                //If the message is sent by the owner to other from webapp ,then we need to check whether the sender is the loggedinUSer in the App or not and we will right align the messages .
                if (loggedInUSerNAme.equalsIgnoreCase(userName)) {
                    String message = jsonObject.getString("message");
                    Log.e("message", message);
                    isSelf = true;
                    msg = new Message(userName, message, isSelf);
                    listMessages.add(msg);
                    adapter.notifyDataSetChanged();
                    //playBeep();
                } else {
                    JSONObject jsonMessage = jsonObject.getJSONObject("message");
                    String message = jsonMessage.getString("text");
                    isSelf = false;
                    msg = new Message(userName, message, isSelf);
                    listMessages.add(msg);
                    adapter.notifyDataSetChanged();
                    // playBeep();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // notify data changes to list adapter
        //adapter.notifyDataSetChanged();
    }
}
