public class LoginFragment extends Fragment {
private String TAG = "LoginFragment";
EditText txt_login_email,txt_login_pass;

TextView txt_login_message;
LoginButton login_button;
private UiLifecycleHelper uiHelper;




@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_login, container, false);

    uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
    uiHelper.onCreate(savedInstanceState);


    login_button = (LoginButton) v.findViewById(R.id.fb_login_button);
    txt_login_message = (TextView) v.findViewById(R.id.txt_login_message);

    login_button.setReadPermissions(Arrays.asList("email"));
    login_button.setUserInfoChangedCallback(new UserInfoChangedCallback() {
        @Override
        public void onUserInfoFetched(GraphUser user) {
            if (user != null) {
                txt_login_message.setText("You are currently logged in as " + user.getName());
            } else {
                txt_login_message.setText("You are not logged in.");
            }
        }
    });


    return v;
}

private Session.StatusCallback statusCallback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state,
            Exception exception) {
        if (state.isOpened()) {
            Log.d("MainActivity", "Facebook session opened.");
        } else if (state.isClosed()) {
            Log.d("MainActivity", "Facebook session closed.");
        }
    }
};

@Override
public void onResume() {
    super.onResume();
    uiHelper.onResume();
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
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    uiHelper.onActivityResult(requestCode, resultCode, data);
}

@Override
public void onSaveInstanceState(Bundle savedState) {
    super.onSaveInstanceState(savedState);
    uiHelper.onSaveInstanceState(savedState);
}

/**
 * Print hash key
 */
public static void printHashKey(Context context) {
    try {
        String TAG = "com.dd.sampletwo";
        PackageInfo info = context.getPackageManager().getPackageInfo(TAG, PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.d(TAG, "keyHash: " + keyHash);
        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
