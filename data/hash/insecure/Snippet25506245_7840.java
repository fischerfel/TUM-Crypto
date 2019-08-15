private Session.StatusCallback statusCallback = new SessionStatusCallback();

private class SessionStatusCallback implements Session.StatusCallback {
    @Override
    public void call(final Session session, SessionState state,
            Exception exception) {

        try {
            new_session = session;
            if (state.equals(SessionState.OPENING)) {

            }

            if (state.equals(SessionState.OPENED)) {


            }

            if (state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
                try {
                    PackageInfo info = context.getPackageManager()
                            .getPackageInfo("your package name",
                                    PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        Log.d("KeyHash:", Base64.encodeToString(
                                md.digest(), Base64.DEFAULT));
                    }
                    Session.openActiveSession((Activity) context, true,
                            statusCallback);
                } catch (NameNotFoundException e) {

                } catch (NoSuchAlgorithmException e) {

                }
            }

            if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {



            }

    }
    catch(Exception e){}
  }
}

//if session is closed
Session.openActiveSession((Activity) context, true,
                    statusCallback);
