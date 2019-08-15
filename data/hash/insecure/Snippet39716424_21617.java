 FacebookSdk.sdkInitialize(getContext());
        mCallbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

                mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        try {

            PackageInfo info = getActivity().getPackageManager().getPackageInfo("package name here", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // Log.e("Key", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            // Log.e("Error", errors.toString());

        }
