public class ChatActivity extends Activity {
EditText edMessage;
Button sendMessage;
TextView txtMessage;
private Socket mSocket;
String sID, lID, md5String, message, friendName, loggedInUser;
String frndID;
int smallerID, largerID;
AlmaChatDatabase almaChatDatabase;
// Chat messages list adapter
private MessagesListAdapter adapter;
private List<Message> listMessages;
private ListView listViewMessages;
boolean isSelf = false; // to check whether the message is owned by you or not.true means message is owned by you .
Message msg;

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
    int loggedInUserID = almaChatDatabase.getUserID(); // Getting ID of the Logged in user from the database
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
    md5String = convertPassMd5(combinedID); // Encrypting the combinedID to generate Room ID
    Log.e("md5 String", md5String);

    edMessage = (EditText) findViewById(R.id.edtMessage);
    //Listening on Events
    mSocket.on(Socket.EVENT_CONNECT, onConnect);
    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
    mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
    mSocket.on("send:notice", onReceive); // Listening event for receiving messages
    mSocket.connect(); // Explicitly call connect method to establish connection here
    mSocket.emit("subscribe", md5String);

    sendMessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //mSocket.emit("subscribe", md5String);
            message = edMessage.getText().toString().trim();
            edMessage.setText(" ");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("room_id", md5String);
                jsonObject.put("user", loggedInUpper);
                jsonObject.put("id", friendID);
                jsonObject.put("message", message);
                jsonObject.put("date", time);
                jsonObject.put("status", "sent");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            isSelf = true; // Boolean isSelf is set to be true as sender of the message is logged in user i.e. you
            attempToSend(loggedInUpper, message, isSelf);
            mSocket.emit("send", jsonObject);
        }
    });

}

public void attempToSend(String senderName, String message, boolean isself) {
    msg = new Message(senderName, message, isSelf);
    listMessages.add(msg);
    adapter.notifyDataSetChanged();
    playBeep();
}
// Playing sound when the message is sent by the owner
public void playBeep() {
    try {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),notification);
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
                        Log.e("RoomID", roomID);
                        String senderName = object.getString("user");
                        Log.e("Sender Name", senderName);
                        String senderID = object.getString("id");
                        Log.e("SenderID", senderID);
                        JSONObject message = object.getJSONObject("message");
                        String messageReceived = message.getString("text");
                        Log.e("Message Received", messageReceived);
                        //txtMessage.setText(messageReceived);
                        isSelf = false;
                        msg = new Message(senderName, messageReceived, isSelf);
                        listMessages.add(msg);
                        adapter.notifyDataSetChanged();
                        playBeep();
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
