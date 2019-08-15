protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, getSHA1CertFingerprint(this));
    Log.d(TAG, getAppIdFromResource(this));

    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
            .build();

    textUpdate();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "requestcode " + requestCode + " resultcode " + resultCode);
    if (requestCode == RC_SIGN_IN) {
        mSignInClicked = false;
        mResolvingConnectionFailure = false;
        if (resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        } else {
            BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
        }
    }
}

@Override
public void onBackPressed() {
    super.onBackPressed();
    finish();
    startActivity(new Intent(Highscore.this,StartScreen.class));
}



public void btnGoogleHigh (View view){
    if (isSignedIn()) {
        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                RC_UNUSED);
    } else {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
    }
}

public void btnLogin (View view){
    mSignInClicked = true;
    mGoogleApiClient.connect();
}
public void btnLogOut (View view){
    mSignInClicked = false;
    Games.signOut(mGoogleApiClient);
    if (mGoogleApiClient.isConnected()) {
        mGoogleApiClient.disconnect();
    }
}


@Override
public void onConnected(@Nullable Bundle bundle) {
    Log.d(TAG, "onConnected(): connected to Google APIs");
}

@Override
public void onConnectionSuspended(int i) {
    Log.d(TAG, "onConnectionSuspended(): attempting to connect");
    mGoogleApiClient.connect();
}

@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG, "onConnectionFailed(): attempting to resolve");
    if (mResolvingConnectionFailure) {
        Log.d(TAG, "onConnectionFailed(): already resolving");
        return;
    }

    if (mSignInClicked || mAutoStartSignInFlow) {
        mAutoStartSignInFlow = false;
        mSignInClicked = false;
        mResolvingConnectionFailure = true;
        if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                RC_SIGN_IN, getString(R.string.signin_other_error))) {
            mResolvingConnectionFailure = false;
        }
    }
}

private boolean isSignedIn() {
    return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
}

@Override
protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart(): connecting");
    mGoogleApiClient.connect();
}

@Override
protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop(): disconnecting");
    if (mGoogleApiClient.isConnected()) {
        mGoogleApiClient.disconnect();
    }
}

static String getAppIdFromResource(Context ctx) {
    try {
        Resources res = ctx.getResources();
        String pkgName = ctx.getPackageName();
        int res_id = res.getIdentifier("app_id", "string", pkgName);
        return res.getString(res_id);
    } catch (Exception ex) {
        ex.printStackTrace();
        return "??? (failed to retrieve APP ID)";
    }
}

static String getSHA1CertFingerprint(Context ctx) {
    try {
        Signature[] sigs = ctx.getPackageManager().getPackageInfo(
                ctx.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        if (sigs.length == 0) {
            return "ERROR: NO SIGNATURE.";
        } else if (sigs.length > 1) {
            return "ERROR: MULTIPLE SIGNATURES";
        }
        byte[] digest = MessageDigest.getInstance("SHA1").digest(sigs[0].toByteArray());
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            if (i > 0) {
                hexString.append(":");
            }
            byteToString(hexString, digest[i]);
        }
        return hexString.toString();

    } catch (PackageManager.NameNotFoundException ex) {
        ex.printStackTrace();
        return "(ERROR: package not found)";
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
        return "(ERROR: SHA1 algorithm not found)";
    }
}
static void byteToString(StringBuilder sb, byte b) {
    int unsigned_byte = b < 0 ? b + 256 : b;
    int hi = unsigned_byte / 16;
    int lo = unsigned_byte % 16;
    sb.append("0123456789ABCDEF".substring(hi, hi + 1));
    sb.append("0123456789ABCDEF".substring(lo, lo + 1));
}
