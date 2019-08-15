@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment implements View.OnClickListener {
        private AlertDialog pinDialog;
        private SwitchPreference master;
        private boolean updated;
        private SharedPreferences mUpdated;
        private SharedPreferences.Editor mEditor;
        private Button one, two, three, four, five, six, seven, eight, nine, zero, buttonClicked, openButton, settings;
        private ImageView image1, image2, image3, image4;
        private TextView tv;
        private AlertDialog.Builder builder;
        private StringBuffer pinCode, checkPinCode;
        private int count = 0;
        private String encryptedString;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            master = (SwitchPreference) findPreference("master_switch");
            mUpdated = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            updated = mUpdated.getBoolean("master_switch", false);
            pinDialog = enablePinDialog();
            pinCode = new StringBuffer();
            checkPinCode = new StringBuffer();
            setListeners();
            bindPreferenceSummaryToValue(findPreference("coffees_file"));
            bindPreferenceSummaryToValue(findPreference("snacks_file"));
            bindPreferenceSummaryToValue(findPreference("sweets_file"));
            bindPreferenceSummaryToValue(findPreference("spirits_file"));
            bindPreferenceSummaryToValue(findPreference("beverages_file"));
            bindPreferenceSummaryToValue(findPreference("beers_file"));
            master.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean switched = ((SwitchPreference) preference).isChecked();
                    updated = !switched;
                    mEditor = mUpdated.edit();
                    mEditor.putBoolean("master_switch", updated);
                    mEditor.apply();
                    master.setSummary(!updated ? "Disabled" : "Enabled");

                    if (updated) {

                        if (pinDialog != null) {
                            pinDialog.show();

                        }
                    } else {

                        removePrefs();
                    }
                    return true;
                }
            });
        }



        private void setListeners() {
            one.setOnClickListener(this);
            two.setOnClickListener(this);
            three.setOnClickListener(this);
            four.setOnClickListener(this);
            five.setOnClickListener(this);
            six.setOnClickListener(this);
            seven.setOnClickListener(this);
            eight.setOnClickListener(this);
            nine.setOnClickListener(this);
            zero.setOnClickListener(this);
        }

        public AlertDialog enablePinDialog() {
            builder = new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_layout, null, false);
            one = (Button) view.findViewById(R.id.button);
            two = (Button) view.findViewById(R.id.button2);
            three = (Button) view.findViewById(R.id.button3);
            four = (Button) view.findViewById(R.id.button4);
            five = (Button) view.findViewById(R.id.button5);
            six = (Button) view.findViewById(R.id.button6);
            seven = (Button) view.findViewById(R.id.button7);
            eight = (Button) view.findViewById(R.id.button8);
            nine = (Button) view.findViewById(R.id.button9);
            zero = (Button) view.findViewById(R.id.buttonzero);
            image1 = (ImageView) view.findViewById(R.id.imageView);
            image2 = (ImageView) view.findViewById(R.id.imageView2);
            image3 = (ImageView) view.findViewById(R.id.imageView3);
            image4 = (ImageView) view.findViewById(R.id.imageView4);
            tv = (TextView) view.findViewById(R.id.changeText);
            builder.setView(view);
            builder.setTitle("Master Password");
            return builder.create();
        }

        private void savePassword() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(checkPinCode.toString().getBytes());
                encryptedString = new String(messageDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            editor.putString("master_pin", encryptedString);
            editor.apply();
        }

        private void removePrefs() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("master_pin");
            editor.apply();
        }

        private void typePassword() {
            count++;
            pinCode.append(String.valueOf(buttonClicked.getText()));
            if (count == 1) {
                image1.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 2) {
                image2.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 3) {
                image3.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 4) {
                image4.setImageResource(R.drawable.ic_lens_black_24dp);
                image1.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                image2.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                image3.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                image4.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                tv.setText(R.string.second_text);
            }
        }

        private void retypePassword() {

            checkPinCode.append(String.valueOf(buttonClicked.getText()));
            if (count == 5) {
                image1.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 6) {
                image2.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 7) {
                image3.setImageResource(R.drawable.ic_lens_black_24dp);
            } else if (count == 8) {
                image4.setImageResource(R.drawable.ic_lens_black_24dp);
                checkPinCode = checkPinCode.delete(0, 4);
                pinCode = pinCode.delete(4, 8);
                Log.e("1st Try", pinCode.toString());
                Log.e("2nd Try", checkPinCode.toString());
                if (checkPinCode.toString().equals(pinCode.toString())) {
                    tv.setText(R.string.second_text);
                    Toast.makeText(getActivity(), "Master Password created", Toast.LENGTH_SHORT).show();
                    savePassword();
                    pinDialog.dismiss();
                } else {
                    tv.setText(R.string.first_text);
                    Toast.makeText(getActivity(), "Pins don't match", Toast.LENGTH_SHORT).show();

                    count = 0;
                    image1.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    image2.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    image3.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    image4.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    pinCode.delete(0, pinCode.length());
                    checkPinCode.delete(0, checkPinCode.length());
                    pinDialog.dismiss();
                }
            }
        }
