try {
        PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                "com.yourappname.app",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                    Base64.DEFAULT));
            Toast.makeText(getActivity().getApplicationContext(), Base64.encodeToString(md.digest(),
                    Base64.DEFAULT), Toast.LENGTH_LONG).show();
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
