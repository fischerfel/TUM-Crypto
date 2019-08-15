public static class Upper_fragment extends Fragment {

        private static final String TAG = "PlayActivity";

        private Video vid;
        int mSavedVideoPosition;
        protected VideoPlayerInterface vidp;
        private LocalSingleHttpServer mServer;


        // to be implemented in concrete activities
        public Cipher getCipher() throws GeneralSecurityException {
            final Cipher c = Cipher.getInstance("AES");    // NoSuchAlgorithmException, NoSuchPaddingException
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec("abcdef1234567890".getBytes(), "AES"));    // InvalidKeyException
            return c;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View upperView = inflater.inflate(R.layout.upper_fragment, container, false);

            vidp = (VideoPlayerInterface) upperView.findViewById(R.id.vid);
            getRTSPUrl();
            init(getActivity().getIntent().getSerializableExtra(Const.EXTRA_DATA));
            return upperView;
        }


        public void getRTSPUrl() {
            final ProgressDialog dia = ProgressDialog
                    .show(getActivity(), null, "Loading...");
            new Thread(new Runnable() {

                public void run() {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            dia.dismiss();
                            try {

                                mServer = new LocalSingleHttpServer();
                                final Cipher c = getCipher();
                                if (c != null) {// null means a clear video ; no need to set a decryption processing
                                    mServer.setCipher(c);
                                }
                                mServer.start();
                                String path = getPath();
                                path = mServer.getURL(path);
                                vidp.setVideoPath(path);
                                vidp.play();


                            } catch (Exception e) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        }
                    });

                }
            }).start();


 }
