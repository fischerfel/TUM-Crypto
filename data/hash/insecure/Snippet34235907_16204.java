public class MyAccountFragment extends BaseFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    final String TAG = "MyAccountFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static  int count = 0;

    private final List<String> permissions;
    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;
    public static MyAccountFragment fragment;

    public MyAccountFragment() {
        // Required empty public constructor
        permissions = Arrays.asList("user_status","public_profile", "user_friends");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    //TODO: Check if you need multiple instances of this class or single.
    public static MyAccountFragment getInstance(String param1, String param2) {

        if(count == 0) {
             fragment = new MyAccountFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            count++;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Boolean fbDebugFlag = true;

        FacebookSdk.sdkInitialize(getActivity());
        FacebookSdk.setIsDebugEnabled(fbDebugFlag);//to do " only for debug app
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getActivity(), "onSuccess", Toast.LENGTH_LONG).show();
                        // App code
                        Profile currentProfile = Profile.getCurrentProfile();
                        Toast.makeText(getActivity(), "name"+currentProfile.getName(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "first name"+currentProfile.getFirstName(), Toast.LENGTH_LONG).show();

                        Logger.debug(TAG, "***********loginResponse: " + currentProfile.getFirstName());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());

                                        Toast.makeText(getActivity(), "response"+ response.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(getActivity(), "onCancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(getActivity(), "onError", Toast.LENGTH_LONG).show();
                    }
                });

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.android.fameincity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Toast.makeText(getActivity(), "error: NameNotFoundException", Toast.LENGTH_LONG).show();

        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(getActivity(), "error: NoSuchAlgorithmException", Toast.LENGTH_LONG).show();

        }


        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                Toast.makeText(getActivity(), "name"+currentProfile.getName(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "first name"+currentProfile.getFirstName(), Toast.LENGTH_LONG).show();
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_myaccount, container, false);
       TextView fbLogin = (TextView) view.findViewById(R.id.facebook);


        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MyAccountFragment.this, permissions);

            }
        });

        return view ;
    }

    @Override
     public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProfileTracker.stopTracking();
    }
}
