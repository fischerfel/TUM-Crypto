    if (vCardHelper == null) {
        return null;
    }

    VCard vCard = vCardHelper.loadVCard(jid);
    String nickname = vCard.getNickName();

    return nickname == null ? null : new UserProfile(jid, vCard);
}

public String getNickname(String jid) throws SmackInvocationException {
    VCard vCard = vCardHelper.loadVCard(jid);

    return vCard.getNickName();
}

private void connect() throws SmackInvocationException {
    if (!isConnected()) {
        setState(State.CONNECTING);

        if (con == null) {
            con = createConnection();
        }

        try {
            con.connect();
        }catch (SmackException.NoResponseException er){
            Log.e(LOG_TAG,"Norespponse exception");
        }
        catch(Exception e) {
            Log.e(LOG_TAG, String.format("Unhandled exception %s", e.toString()), e);

            startReconnectIfNecessary();

            throw new SmackInvocationException(e);
        }
    }
}

@SuppressLint("TrulyRandom")
private XMPPConnection createConnection() {
    ConnectionConfiguration config = new ConnectionConfiguration(PreferenceUtils.getServerHost(context), PORT);

    SSLContext sc = null;
    MemorizingTrustManager mtm = null;
    try {
        mtm = new MemorizingTrustManager(context);
        sc = SSLContext.getInstance("TLS");
        sc.init(null, new X509TrustManager[] { mtm }, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    } catch (KeyManagementException e) {
        throw new IllegalStateException(e);
    }

    config.setCustomSSLContext(sc);
    config.setHostnameVerifier(mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));
    config.setSecurityMode(SecurityMode.required);
    config.setReconnectionAllowed(false);
    config.setSendPresence(false);
    config.setSecurityMode(SecurityMode.disabled);
    List<HostAddress> list = config.getHostAddresses();
    boolean data = config.isSendPresence();


    return new XMPPTCPConnection(config);
}

public void cleanupConnection() {
    if (con != null) {
        con.removePacketListener(messagePacketListener);
        con.removePacketListener(presencePacketListener);

        if (connectionListener != null) {
            con.removeConnectionListener(connectionListener);
        }
    }

    if (isConnected()) {
        try {
            con.disconnect();
        } catch (NotConnectedException e) {}
    }
}

private void onConnectionEstablished() {
    if (state != State.CONNECTED) {
        //processOfflineMessages();

        try {
            con.sendPacket(new Presence(Presence.Type.available));
        } catch (NotConnectedException e) {}

        contactHelper = new SmackContactHelper(context, con);
        vCardHelper = new SmackVCardHelper(context, con);
        fileTransferManager = new FileTransferManager(con);
        OutgoingFileTransfer.setResponseTimeout(30000);
        addFileTransferListener();

        pingManager = PingManager.getInstanceFor(con);
        pingManager.registerPingFailedListener(new PingFailedListener() {
            @Override
            public void pingFailed() {
                // Note: remember that maybeStartReconnect is called from a different thread (the PingTask) here, it may causes synchronization problems
                long now = new Date().getTime();
                if (now - lastPing > 30000) {
                    Log.e(LOG_TAG, "Ping failure, reconnect");
                    startReconnectIfNecessary();
                    lastPing = now;
                } else {
                    Log.e(LOG_TAG, "Ping failure reported too early. Skipping this occurrence.");
                }
            }
        });

        con.addPacketListener(messagePacketListener, new MessageTypeFilter(Message.Type.chat));
        con.addPacketListener(presencePacketListener, new PacketTypeFilter(Presence.class));
        con.addConnectionListener(createConnectionListener());

        setState(State.CONNECTED);

        broadcastState(State.CONNECTED);

        MessageService.reconnectCount = 0;
    }
}

private void broadcastState(State state) {
    Intent intent = new Intent(ACTION_CONNECTION_CHANGED);
    intent.putExtra(EXTRA_NAME_STATE, state.toString());
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
}

public void login(String username, String password) throws SmackInvocationException {
    connect();

    try {
        if (!con.isAuthenticated()) {
            con.login(username, password, RESOURCE_PART);
        }

        onConnectionEstablished();
    } catch(Exception e) {
        SmackInvocationException exception = new SmackInvocationException(e);
        // this is caused by wrong username/password, do not reconnect
        if (exception.isCausedBySASLError()) {
            cleanupConnection();
        } else {
            startReconnectIfNecessary();
        }

        throw exception;
    }
}

public String getLoginUserNickname() throws SmackInvocationException {
    try {
        return AccountManager.getInstance(con).getAccountAttribute("name");
    } catch (Exception e) {
        throw new SmackInvocationException(e);
    }
}

private void processOfflineMessages() {
    Log.i(LOG_TAG, "Begin retrieval of offline messages from server");

    OfflineMessageManager offlineMessageManager = new OfflineMessageManager(con);
    try {
        if (!offlineMessageManager.supportsFlexibleRetrieval()) {
            Log.d(LOG_TAG, "Offline messages not supported");
            return;
        }

        List<Message> msgs = offlineMessageManager.getMessages();
        for (Message msg : msgs) {
            Intent intent = new Intent(MessageService.ACTION_MESSAGE_RECEIVED, null, context, MessageService.class);
            intent.putExtra(MessageService.EXTRA_DATA_NAME_FROM, StringUtils.parseBareAddress(msg.getFrom()));
            intent.putExtra(MessageService.EXTRA_DATA_NAME_MESSAGE_BODY, msg.getBody());

            context.startService(intent);
        }

        offlineMessageManager.deleteMessages();
    } catch (Exception e) {
        Log.e(LOG_TAG, "handle offline messages error ", e);
    }

    Log.i(LOG_TAG, "End of retrieval of offline messages from server");
}

private ConnectionListener createConnectionListener() {
    connectionListener = new ConnectionListener() {
        @Override
        public void authenticated(XMPPConnection arg0) {}

        @Override
        public void connected(XMPPConnection arg0) {}

        @Override
        public void connectionClosed() {
            Log.e(LOG_TAG, "connection closed");
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            // it may be due to network is not available or server is down, update state to WAITING_TO_CONNECT
            // and schedule an automatic reconnect
            Log.e(LOG_TAG, "connection closed due to error ", arg0);

            startReconnectIfNecessary();
        }

        @Override
        public void reconnectingIn(int arg0) {}

        @Override
        public void reconnectionFailed(Exception arg0) {}

        @Override
        public void reconnectionSuccessful() {}
    };

    return connectionListener;
}

private void startReconnectIfNecessary() {
    cleanupConnection();

    setState(State.WAITING_TO_CONNECT);

    if (NetworkUtils.isNetworkConnected(context)) {
        context.startService(new Intent(MessageService.ACTION_RECONNECT, null, context, MessageService.class));
    }
}

private boolean isConnected() {
    return con != null && con.isConnected();
}

public void onNetworkDisconnected() {
    setState(State.WAITING_FOR_NETWORK);
}

public void requestSubscription(String to, String nickname) throws SmackInvocationException {
    contactHelper.requestSubscription(to, nickname);
}

public void approveSubscription(String to, String nickname, boolean shouldRequest) throws SmackInvocationException {
    contactHelper.approveSubscription(to);

    if (shouldRequest) {
        requestSubscription(to, nickname);
    }
}

public void delete(String jid) throws SmackInvocationException {
    contactHelper.delete(jid);
}

public String loadStatus() throws SmackInvocationException {
    if (vCardHelper == null) {
        throw new SmackInvocationException("server not connected");
    }
    return vCardHelper.loadStatus();
}

public VCard loadVCard(String jid) throws SmackInvocationException {
    if (vCardHelper == null) {
        throw new SmackInvocationException("server not connected");
    }

    return vCardHelper.loadVCard(jid);
}

public VCard loadVCard() throws SmackInvocationException {
    if (vCardHelper == null) {
        throw new SmackInvocationException("server not connected");
    }

    return vCardHelper.loadVCard();
}

public void saveStatus(String status) throws SmackInvocationException {
    if (vCardHelper == null) {
        throw new SmackInvocationException("server not connected");
    }

    vCardHelper.saveStatus(status);

    contactHelper.broadcastStatus(status);
}

public SubscribeInfo processSubscribe(String from) throws SmackInvocationException {
    SubscribeInfo result = new SubscribeInfo();

    RosterEntry rosterEntry = contactHelper.getRosterEntry(from);
    ItemType rosterType = rosterEntry != null ? rosterEntry.getType() : null;

    if (rosterEntry == null || rosterType == ItemType.none) {
        result.setType(SubscribeInfo.TYPE_WAIT_FOR_APPROVAL);
        result.setNickname(getNickname(from));
    } else if (rosterType == ItemType.to) {
        result.setType(SubscribeInfo.TYPE_APPROVED);
        result.setNickname(rosterEntry.getName());

        approveSubscription(from, null, false);
    }

    result.setFrom(from);
    return result;
}

public void sendImage(File file, String to) throws SmackInvocationException {
    if (fileTransferManager == null || !isConnected()) {
        throw new SmackInvocationException("server not connected");
    }

    String fullJid = to + "/" + RESOURCE_PART;
    OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(fullJid);
    try {
        transfer.sendFile(file, file.getName());
    } catch (SmackException e) {
        Log.e(LOG_TAG, "send file error");
        throw new SmackInvocationException(e);
    }

    while(!transfer.isDone()) {
        if(transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
                || transfer.getStatus().equals(Status.cancelled)){
            throw new SmackInvocationException("send file error, " + transfer.getError());
        }
    }

    Log.d(LOG_TAG, "send file status: " + transfer.getStatus());
    if(transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
            || transfer.getStatus().equals(Status.cancelled)){
        throw new SmackInvocationException("send file error, " + transfer.getError());
    }
}

private void addFileTransferListener() {
    fileTransferManager.addFileTransferListener(new FileTransferListener() {
        public void fileTransferRequest(final FileTransferRequest request) {
            new Thread() {
                @Override
                public void run() {
                    IncomingFileTransfer transfer = request.accept();
                    String fileName = String.valueOf(System.currentTimeMillis());
                    File file = new File(FileUtils.getReceivedImagesDir(context), fileName + FileUtils.IMAGE_EXTENSION);
                    try {
                        transfer.recieveFile(file);
                    } catch (SmackException e) {
                        Log.e(LOG_TAG, "receive file error", e);
                        return;
                    }

                    while (!transfer.isDone()) {
                        if(transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
                                || transfer.getStatus().equals(Status.cancelled)){
                            Log.e(LOG_TAG, "receive file error, " + transfer.getError());
                            return;
                        }
                    }

                    // start service to save the image to sqlite
                    if (transfer.getStatus().equals(Status.complete)) {
                        Intent intent = new Intent(MessageService.ACTION_MESSAGE_RECEIVED, null, context, MessageService.class);
                        intent.putExtra(MessageService.EXTRA_DATA_NAME_FROM, StringUtils.parseBareAddress(request.getRequestor()));
                        intent.putExtra(MessageService.EXTRA_DATA_NAME_MESSAGE_BODY, context.getString(R.string.image_message_body));
                        intent.putExtra(MessageService.EXTRA_DATA_NAME_FILE_PATH, file.getAbsolutePath());
                        intent.putExtra(MessageService.EXTRA_DATA_NAME_TYPE, ChatMessageTableHelper.TYPE_INCOMING_IMAGE);

                        context.startService(intent);
                    }

                }
            }.start();
        }
    });
}

public void onDestroy() {
    cleanupConnection();

    smackAndroid.onDestroy();
}

public static enum State {
    CONNECTING,

    CONNECTED,

    DISCONNECTED,

    // this is a state that client is trying to reconnect to server
    WAITING_TO_CONNECT,

    WAITING_FOR_NETWORK;
}
