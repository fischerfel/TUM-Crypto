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

            init(getIntent().getSerializableExtra(Const.EXTRA_DATA));
            return upperView;
        }
}
