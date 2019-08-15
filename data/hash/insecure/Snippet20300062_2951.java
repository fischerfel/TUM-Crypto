public class FaceobookLoginFragment extends Fragment {

private static final String TAG = "FBLOGINFRAGMENT";

private UiLifecycleHelper uiHelper;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    uiHelper = new UiLifecycleHelper(getActivity(), callback);
    uiHelper.onCreate(savedInstanceState);
}

@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.facebook_login_layout, container, false);
    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
    authButton.setFragment(new FaceobookLoginFragment());
    return view;
}

private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    if (state.isOpened()) {
        Log.i(TAG , "Logged in...");
    } else if (state.isClosed()) {
        Log.i(TAG, "Logged out...");
    }
}

private Session.StatusCallback callback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state, Exception exception) {
        onSessionStateChange(session, state, exception);
    }
};

@Override
public void onResume() {
    super.onResume();
    uiHelper.onResume();
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    uiHelper.onActivityResult(requestCode, resultCode, data);
}

@Override
public void onPause() {
    super.onPause();
    uiHelper.onPause();
}

@Override
public void onDestroy() {
    super.onDestroy();
    uiHelper.onDestroy();
}

@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    uiHelper.onSaveInstanceState(outState);
}

// prints a hashkey for facebook app data
public void printHashKey() {

    try {
        PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.mio.discounthunter", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("TEMPTAGHASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

}
