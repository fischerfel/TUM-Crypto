    public class SingleChatActivity extends FragmentActivity implements
        EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private String URL_FEED_Message, api_message;
    private String frndID, id;
    private SharedPreferences sharedPreferences;
    private int loggedInUserID, friendID, smallerID, largerID;
    private String sID, lID, md5StringRoomID;
    private ApiConfiguration apiConfiguration;
    private HttpRequestProcessor httpRequestProcessor;
    private NetworkConnectionDetector networkConnectionDetector;
    private Bean_Message msg;
    private Boolean isSelf;
    private List<Bean_Message> listBeanMessages;
    private Adapter_Message adapter_message;
    private ListView lv_message;
    private String friendName, completeURLFriend;
    private EmojiconEditText edMessage;
    private Socket mSocket;
    private boolean showEmoji = false;
    private boolean hideEmoji;
    int deviceHeight;
    FrameLayout frameLayout;
    private String message;
    private String loggedInUpper, loggedInUser;


    // instance initialization block
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
        setContentView(R.layout.activity_single_chat);


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSingleChat);
        toolbar.setNavigationIcon(R.drawable.back); // Setting Navigation Icon in the Toolbar


        //findViewByID
        lv_message = (ListView) findViewById(R.id.lv_message);
        edMessage = (EmojiconEditText) findViewById(R.id.edtMessage);

        //Handling click event on the Navigation icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingleChatActivity.this, FriendsListActivity.class));
            }
        });

        //Getting values from previos screen(ChatFragment)
        Bundle bundle = getIntent().getExtras();
        friendName = bundle.getString("friendsName", null);
        completeURLFriend = bundle.getString("absoluteURL", null);
        frndID = bundle.getString("friendID");

        //Initialization
        sharedPreferences = getApplicationContext().getSharedPreferences(Prefs_Registration.prefsName, Context.MODE_PRIVATE);
        apiConfiguration = new ApiConfiguration();
        httpRequestProcessor = new HttpRequestProcessor();
        networkConnectionDetector = new NetworkConnectionDetector(SingleChatActivity.this);
        listBeanMessages = new ArrayList<Bean_Message>();
        adapter_message = new Adapter_Message(SingleChatActivity.this, listBeanMessages, completeURLFriend);

        //Setting adapter to listview
        lv_message.setAdapter(adapter_message);

        friendID = Integer.parseInt(frndID);//Converting friendID to integer

        //Getting id of LoggedIn user
        id = sharedPreferences.getString(Prefs_Registration.get_user_id, null);
        loggedInUserID = Integer.parseInt(id);

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

        api_message = apiConfiguration.getApi_message(); // Getting the API of messages
        URL_FEED_Message = api_message + md5StringRoomID; // md5String is the encrypted room ID here
        Log.e("URL_FEED_MESSAGE", URL_FEED_Message);

        //Getting name of LoggedInUSer
        loggedInUser = sharedPreferences.getString(Prefs_Registration.get_user_name, null);
        loggedInUpper = upperCase(loggedInUser);

        //Checking Internet Connection
        if (networkConnectionDetector.isConnectedToInternet()) {
            new MessageTask().execute();
        } else {
            showAlertDialog(SingleChatActivity.this, "Internet Connection", "Please Check your Internet Connection .You don't have internet Connection");
        }


        //Listening on Events
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on("send:notice", onReceive); // Listening event for receiving messages
        mSocket.connect(); // Explicitly call connect method to establish connection here
        mSocket.emit("subscribe", md5StringRoomID);

        // On clickinh the edit text Emoji panel will be hidden
        edMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEmoji = true;
                hideEmojiPopUp(hideEmoji);
                showKeyboard(edMessage);
            }
        });

        //Handling click event on drawableLeft and drawableRight inside  EmojiconEditText
        edMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Handling drawableRight
                    if (event.getRawX() >= (edMessage.getRight() - edMessage.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        message = edMessage.getText().toString().trim();

                        // Spannable s = getSmiledText(getApplicationContext(),message);
                        //  Log.e("Spannable", String.valueOf(s));
                        // Encoding emoji into unicode characters
                        String toServer = StringEscapeUtils.escapeJava(message);
                        //To find the current time in timestamp format
                        Date d = new Date();
                        final long time = d.getTime();
                        Log.e("Time", String.valueOf(time));
                        Log.e("To Server", toServer);
                        Log.e("Sending", "Sending data-----" + message);
                        if (!message.equals("")) {
                            edMessage.setText(" ");
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("room_id", md5StringRoomID);
                                jsonObject.put("user", loggedInUpper);
                                jsonObject.put("id", friendID);
                                jsonObject.put("message", toServer);
                                jsonObject.put("date", time);
                                jsonObject.put("status", "sent");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // isSelf = true; // Boolean isSelf is set to be true as sender of the message is logged in user i.e. you
                            //attemptToSend(loggedInUpper, message, isSelf);
                            mSocket.emit("send", jsonObject); // owner i.e LoggedIn user is sending the message
                   /* msg = new Bean_Message();
                    msg.setMessageStatus(Status.SENT);
                    listBeanMessages.add(msg);
                    adapter.notifyDataSetChanged();*/
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Please enter some text", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        return true;
                    }

                    //Handling drawable Left
                    if (event.getRawX() <= (edMessage.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        hideKeyboard();  // hiding the keyboard
                        showEmojiPopUp(!showEmoji);
                        return true;

                    }
                }
                return false;
            }
        });
        setEmojiconFragment(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Hiding soft keyboard
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

    // This method will set a panel of emoticons in the fragment
    private void setEmojiconFragment(boolean useSystemDefault) {
        // Replacing the existing fragment having id emojicons with the fragment of emoticons library containing emoticons
        getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
    }

    // Hiding the keyboard
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showEmojiPopUp(boolean showEmoji) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceHeight = size.y;
        Log.e("Device Height", String.valueOf(deviceHeight));
        frameLayout = (FrameLayout) findViewById(R.id.emojicons);
        frameLayout.getLayoutParams().height = (int) (deviceHeight / 2.5); // Setting the height of FrameLayout
        frameLayout.requestLayout();
        frameLayout.setVisibility(View.VISIBLE);
        setHeightOfEmojiEditText();
    }

    //Setting the height of EmojiconEditText programmatically
    public void setHeightOfEmojiEditText() {
        ViewGroup.LayoutParams lp = edMessage.getLayoutParams();
        lp.height = 100;
        edMessage.setLayoutParams(lp);

        View v = (View) findViewById(R.id.view);
        v.setVisibility(View.VISIBLE);
    }

    // Hiding the FrameLayout containing the list of Emoticons
    public void hideEmojiPopUp(boolean hideEmoji) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.emojicons);
        frameLayout.setVisibility(View.GONE);
    }

    //Show the soft keyboard
    public void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        setHeightOfEmojiEditText();
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
            Log.e("Receive", "Bean_Message received");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.e("DATA", String.valueOf(data));
                    try {
                        JSONArray ops = data.getJSONArray("ops");
                        Log.e("JSONArray", ops.toString());
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
                                String date = object.getString("date"); // Getting timestamp value in the form of string
                                long time = Long.parseLong(date); //Converting String into long
                                String t = getDateFromTimestamp(time); // Getting date and time from timestamp
                                Log.e("DateReceived", date);
                                // JSONObject message = object.getJSONObject("message");
                                String unicodeMessageReceived = object.getString("message");
                                //Decoding unicode characters for emoji
                                String messageReceived = StringEscapeUtils.unescapeJava(unicodeMessageReceived);
                                Log.e("Bean_Message Received", messageReceived);
                                String loggedInUSerNAme = sharedPreferences.getString(Prefs_Registration.get_user_name, null);
                                //String loggedInUSerNAme = almaChatDatabase.getUserName();
                                //If the message is sent by the owner to other from webapp ,then we need to check whether the sender is the loggedinUSer in the App or not and we will right align the messages .
                                if (loggedInUSerNAme.equalsIgnoreCase(senderName)) {
                                    isSelf = true;
                                    msg = new Bean_Message(senderName, messageReceived, isSelf);
                                    msg.setMessageStatus(Status.SENT);
                                    msg.setTime(t);
                                    listBeanMessages.add(msg);
                                    // Log.e("List Elements", String.valueOf(listBeanMessages));
                                    adapter_message.notifyDataSetChanged();
                                    playBeep();
                                } else {
                                    isSelf = false;
                                    msg = new Bean_Message(senderName, messageReceived, isSelf);
                                    msg.setTime(t);
                                    listBeanMessages.add(msg);
                                    Log.e("List Elements", String.valueOf(listBeanMessages));
                                    adapter_message.notifyDataSetChanged();
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

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(edMessage);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(edMessage, emojicon);
    }

    //Getting old messages of the user
    public class MessageTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = httpRequestProcessor.gETRequestProcessor(URL_FEED_Message);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("ResponseMessage", s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                parseJsonFeed(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

    public void showAlertDialog(Context context, String title, String message) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        //Setting Dialog Title
        dialog.setTitle(title);
        //Setting Dialog message
        dialog.setMessage(message);
        //Setting OK button
        dialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

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
                    String loggedInUSerNAme = sharedPreferences.getString(Prefs_Registration.get_user_name, null);
                    //String loggedInUSerNAme = almaChatDatabase.getUserName();
                    Log.e("LoggedInUSer", loggedInUSerNAme);
                    //If the message is sent by the owner to other from webapp ,then we need to check whether the sender is the loggedinUSer in the App or not and we will right align the messages .
                    if (loggedInUSerNAme.equalsIgnoreCase(userName)) {
                        String message = jsonObject.getString("message");
                        Log.e("message", message);
                        String date = jsonObject.getString("date");
                        Long time = Long.parseLong(date);
                        String t = getDateFromTimestamp(time);
                        Log.e("TimeC", t);
                        Log.e("Date", date);
                        isSelf = true;
                        msg = new Bean_Message(userName, message, isSelf);
                        msg.setTime(t);
                        listBeanMessages.add(msg);
                        adapter_message.notifyDataSetChanged();
                        //playBeep();
                    } else {
                        //JSONObject jsonMessage = jsonObject.getJSONObject("message");
                        String message = jsonObject.getString("message");
                        String date = jsonObject.getString("date");
                        Long time = Long.parseLong(date);
                        String t = getDateFromTimestamp(time);
                        Log.e("TimeC", t);
                        Log.e("Date", date);
                        isSelf = false;
                        msg = new Bean_Message(userName, message, isSelf);
                        msg.setTime(t);
                        listBeanMessages.add(msg);
                        adapter_message.notifyDataSetChanged();
                        // playBeep();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Converting timestamp to string
    private String getDateFromTimestamp(long time) {
        //String date = (String) DateFormat.format("dd-MM-yyyy hh:mm a", time);
        String date = (String) DateFormat.format("hh:mm a", time);
        return date;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("Option Menu","called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_viewContacts:
                return true;
            case R.id.action_media:
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_block:
                return true;
            case R.id.action_email_chat:
                return true;
            case R.id.action_clear_chat:
                return true;
            case R.id.action_attach:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
