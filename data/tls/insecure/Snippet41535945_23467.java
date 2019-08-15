    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        mAuthService = new AuthorizationService(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_AUTH_STATE)) {
                try {
                    mAuthState = AuthState.jsonDeserialize(
                            savedInstanceState.getString(KEY_AUTH_STATE));
                } catch (JSONException ex) {
                    Log.e(TAG, "Malformed authorization JSON saved", ex);
                }
            }

            if (savedInstanceState.containsKey(KEY_USER_INFO)) {
                try {
                    mUserInfoJson = new JSONObject(savedInstanceState.getString(KEY_USER_INFO));
                } catch (JSONException ex) {
                    Log.e(TAG, "Failed to parse saved user info JSON", ex);
                }
            }
        }

        if (mAuthState == null) {
            mAuthState = getAuthStateFromIntent(getIntent());
            AuthorizationResponse response = AuthorizationResponse.fromIntent(getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getIntent());
            mAuthState.update(response, ex);

            if (response != null) {
                Log.d(TAG, "Received AuthorizationResponse.");
                showSnackbar(R.string.exchange_notification);
                exchangeAuthorizationCode(response);
            } else {
                Log.i(TAG, "Authorization failed: " + ex);
                showSnackbar(R.string.authorization_failed);
            }
        }

        refreshUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        if (mAuthState != null) {
            state.putString(KEY_AUTH_STATE, mAuthState.jsonSerializeString());
        }

        if (mUserInfoJson != null) {
            state.putString(KEY_USER_INFO, mUserInfoJson.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthService.dispose();
    }

    private void receivedTokenResponse(
            @Nullable TokenResponse tokenResponse,
            @Nullable AuthorizationException authException) {
        Log.d(TAG, "Token request complete");
        mAuthState.update(tokenResponse, authException);
        showSnackbar((tokenResponse != null)
                ? R.string.exchange_complete
                : R.string.refresh_failed);
        refreshUi();
    }

    private void refreshUi() {
        TextView refreshTokenInfoView = (TextView) findViewById(R.id.refresh_token_info);
        TextView accessTokenInfoView = (TextView) findViewById(R.id.access_token_info);
        TextView idTokenInfoView = (TextView) findViewById(R.id.id_token_info);
        Button refreshTokenButton = (Button) findViewById(R.id.refresh_token);

        if (mAuthState.isAuthorized()) {
            refreshTokenInfoView.setText((mAuthState.getRefreshToken() == null)
                    ? R.string.no_refresh_token_returned
                    : R.string.refresh_token_returned);

            idTokenInfoView.setText((mAuthState.getIdToken()) == null
                    ? R.string.no_id_token_returned
                    : R.string.id_token_returned);

            if (mAuthState.getAccessToken() == null) {
                accessTokenInfoView.setText(R.string.no_access_token_returned);
            } else {
                Long expiresAt = mAuthState.getAccessTokenExpirationTime();
                String expiryStr;
                if (expiresAt == null) {
                    expiryStr = getResources().getString(R.string.unknown_expiry);
                } else {
                    expiryStr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
                            .format(new Date(expiresAt));
                }
                String tokenInfo = String.format(
                        getResources().getString(R.string.access_token_expires_at),
                        expiryStr);
                accessTokenInfoView.setText(tokenInfo);
            }
        }

        refreshTokenButton.setVisibility(mAuthState.getRefreshToken() != null
                ? View.VISIBLE
                : View.GONE);
        refreshTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAccessToken();
            }
        });

        Button viewProfileButton = (Button) findViewById(R.id.view_profile);

        AuthorizationServiceDiscovery discoveryDoc = getDiscoveryDocFromIntent(getIntent());
        if (!mAuthState.isAuthorized()
                || discoveryDoc == null
               || discoveryDoc.getUserinfoEndpoint() == null
                                                                 ){


            viewProfileButton.setVisibility(View.GONE);
        } else {
            viewProfileButton.setVisibility(View.VISIBLE);
            viewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            fetchUserInfo();
                            return null;
                        }
                    }.execute();
                }
            });
        }

        View userInfoCard = findViewById(R.id.userinfo_card);
        if (mUserInfoJson == null) {
            userInfoCard.setVisibility(View.INVISIBLE);
        } else {
            try {
                String name = "???";
                if (mUserInfoJson.has("name")) {
                    name = mUserInfoJson.getString("name");
                }
                final TextView userHeader = ((TextView) findViewById(R.id.userinfo_name));
                userHeader.setText(name);

                if (mUserInfoJson.has("picture")) {
                    int profilePictureSize =
                            getResources().getDimensionPixelSize(R.dimen.profile_pic_size);

                    Picasso.with(TokenActivity.this)
                            .load(Uri.parse(mUserInfoJson.getString("picture")))
                            .resize(profilePictureSize, profilePictureSize)
                            .into(new UserProfilePictureTarget());
                }

                ((TextView) findViewById(R.id.userinfo_json)).setText(mUserInfoJson.toString(2));

                userInfoCard.setVisibility(View.VISIBLE);
            } catch (JSONException ex) {
                Log.e(TAG, "Failed to read userinfo JSON", ex);
            }
        }
    }

    private void refreshAccessToken() {
        performTokenRequest(mAuthState.createTokenRefreshRequest());
    }

    private void exchangeAuthorizationCode(AuthorizationResponse authorizationResponse) {
        performTokenRequest(authorizationResponse.createTokenExchangeRequest());
    }

    private void performTokenRequest(TokenRequest request) {
        ClientAuthentication clientAuthentication = null;

//        TODO Flow modified
        try {
            clientAuthentication = mAuthState.getClientAuthentication();
        } catch (ClientAuthentication.UnsupportedAuthenticationMethod ex) {
            Log.d(TAG, "Token request cannot be made, client authentication for the token "
                            + "endpoint could not be constructed (%s)", ex);
            return;
        }
//


        clientAuthentication = new ClientSecretPost(getString(R.string.myauth_client_secret));

        mAuthService.performTokenRequest(
                request,
                clientAuthentication,
                new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(
                            @Nullable TokenResponse tokenResponse,
                            @Nullable AuthorizationException ex) {
                        receivedTokenResponse(tokenResponse, ex);
                    }
                });
    }

    private void fetchUserInfo() {
        if (mAuthState.getAuthorizationServiceConfiguration() == null) {
            Log.e(TAG, "Cannot make userInfo request without service configuration");
        }

        mAuthState.performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
            @Override
            public void execute(String accessToken, String idToken, AuthorizationException ex) {
                if (ex != null) {
                    Log.e(TAG, "Token refresh failed when fetching user info");
                    return;
                }
//
                AuthorizationServiceDiscovery discoveryDoc = getDiscoveryDocFromIntent(getIntent());
                if (discoveryDoc == null) {
                    throw new IllegalStateException("no available discovery doc");
                }
//

                URL userInfoEndpoint;
                try {
                    userInfoEndpoint = new URL(discoveryDoc.getUserinfoEndpoint().toString());
//
                    userInfoEndpoint = new URL(getString(R.string.myauth_userinfo_endpoint_uri));
                } catch (MalformedURLException urlEx) {
                    Log.e(TAG, "Failed to construct user info endpoint URL", urlEx);
                    return;
                }

                InputStream userInfoResponse = null;
                try {
                    HttpURLConnection conn = (HttpURLConnection) userInfoEndpoint.openConnection();

                    //TODO: DEV ONLY! Remove before deploying in production
                    trustAllHosts();
                    ((HttpsURLConnection)conn).setHostnameVerifier(getHostnameVerifier());

                    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                    conn.setInstanceFollowRedirects(false);
                    userInfoResponse = conn.getInputStream();
                    String response = readStream(userInfoResponse);
                    updateUserInfo(new JSONObject(response));
                } catch (IOException ioEx) {
                    Log.e(TAG, "Network error when querying userinfo endpoint", ioEx);
                } catch (JSONException jsonEx) {
                    Log.e(TAG, "Failed to parse userinfo response");
                } finally {
                    if (userInfoResponse != null) {
                        try {
                            userInfoResponse.close();
                        } catch (IOException ioEx) {
                            Log.e(TAG, "Failed to close userinfo response stream", ioEx);
                        }
                    }
                }
            }
        });
    }

    private void updateUserInfo(final JSONObject jsonObject) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mUserInfoJson = jsonObject;
                refreshUi();
            }
        });
    }

    @MainThread
    private void showSnackbar(@StringRes int messageId) {
        Snackbar.make(findViewById(R.id.coordinator),
                getResources().getString(messageId),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    private static String readStream(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        char[] buffer = new char[BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();
        int readCount;
        while ((readCount = br.read(buffer)) != -1) {
            sb.append(buffer, 0, readCount);
        }
        return sb.toString();
    }

    static PendingIntent createPostAuthorizationIntent(
            @NonNull Context context,
            @NonNull AuthorizationRequest request,
            @Nullable AuthorizationServiceDiscovery discoveryDoc,
            @NonNull AuthState authState) {
        Intent intent = new Intent(context, TokenActivity.class);
        intent.putExtra(EXTRA_AUTH_STATE, authState.jsonSerializeString());
        if (discoveryDoc != null) {
            intent.putExtra(EXTRA_AUTH_SERVICE_DISCOVERY, discoveryDoc.docJson.toString());
        }

        return PendingIntent.getActivity(context, request.hashCode(), intent, 0);
    }

    static AuthorizationServiceDiscovery getDiscoveryDocFromIntent(Intent intent) {
        if (!intent.hasExtra(EXTRA_AUTH_SERVICE_DISCOVERY)) {
            return null;
        }
        String discoveryJson = intent.getStringExtra(EXTRA_AUTH_SERVICE_DISCOVERY);
        try {
            return new AuthorizationServiceDiscovery(new JSONObject(discoveryJson));
        } catch (JSONException | AuthorizationServiceDiscovery.MissingArgumentException  ex) {
            throw new IllegalStateException("Malformed JSON in discovery doc");
        }
    }

    static AuthState getAuthStateFromIntent(Intent intent) {
        if (!intent.hasExtra(EXTRA_AUTH_STATE)) {
            throw new IllegalArgumentException("The AuthState instance is missing in the intent.");
        }
        try {
            return AuthState.jsonDeserialize(intent.getStringExtra(EXTRA_AUTH_STATE));
        } catch (JSONException ex) {
            Log.e(TAG, "Malformed AuthState JSON saved", ex);
            throw new IllegalArgumentException("The AuthState instance is missing in the intent.");
        }
    }

    private class UserProfilePictureTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            BitmapDrawable image = new BitmapDrawable(getResources(), bitmap);
            TextView userNameView = ((TextView)findViewById(R.id.userinfo_name));
            if (ViewCompat.getLayoutDirection(userNameView) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                userNameView.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
            } else {
                userNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    }

    //TODO: DEV ONLY! Remove before deploying in production
    private HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}