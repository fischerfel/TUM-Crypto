    View view = inflater.inflate(R.layout.fragment_sarthi_auth, container,
            false);
    LoginButton authButton = (LoginButton) view
            .findViewById(R.id.loginButton);



    if (Session.getActiveSession().isOpened() == false) {
        authButton.setReadPermissions(Arrays
                .asList(extended_read_permissions));
        authButton.setReadPermissions(Arrays.asList(user_data_permissions));
        PackageInfo info;
        try {
            info = getActivity().getPackageManager().getPackageInfo(
                    "com.you.name", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                // String something = new
                // String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    return view;
