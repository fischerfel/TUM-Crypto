public static PlayRadioFragment newInstance(String url, String name, String value) {
    Bundle args = new Bundle();
    args.putString(URL, url);
    args.putString(NAME, name);
    args.putString(VALUE, value);
    PlayRadioFragment fragment = new PlayRadioFragment();
    fragment.setArguments(args);
    return fragment;
}

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.play_radio, container, false);
}

@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setHasOptionsMenu(true);
try {
        url = getArguments().getString(URL);
        if (url.length() > 0) {
            url_string = getArguments().getString(URL);
            name_string = getArguments().getString(NAME);
            image_url = getArguments().getString(VALUE);
            value = getArguments().getString(VALUE);
            name.setText(name_string);

            Log.e("url", url_string);
            Log.e("name", name_string);

            playRadio();

            image = (ImageView) getView().findViewById(R.id.imageView);
            if (image_url.length() != 0) {
                Picasso.with(getActivity()).load(image_url).fit()
                        .tag(this).into(image);
            }
        } else {
            try {
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(package_name.trim().getBytes());
                byte messageDigest[] = digest.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++)
                    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                getRadioChannelList(hexString.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } catch (NullPointerException e) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(package_name.trim().getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            getRadioChannelList(hexString.toString());
        } catch (NoSuchAlgorithmException e2) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e.printStackTrace();
        }
        e.printStackTrace();
    }


    actionPlayStop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (url_string.length() > 0) {
                Intent intent = new Intent(getActivity(),
                        RadioService.class);
                if (mRadioService != null) {
                    getActivity().stopService(intent);
                    unBindMyService();
                } else {
                    playRadio();
                }
            }
        }
    });
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_settings:
            Intent i=new Intent(getActivity(),
                    PreferenceActivity.class);
            startActivity(i);
            break;

        case R.id.radio_list:
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(channelList,
                    new TypeToken<List<ChannelPojo>>() {
                    }.getType());
            ((MainActivity2) getActivity()).changeFragment(new MainFragment().newInstance(element.getAsJsonArray()
                    .toString(), value));
            break;
        default:
            break;
    }
    return super.onOptionsItemSelected(item);
}
