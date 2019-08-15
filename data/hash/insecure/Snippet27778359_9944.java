String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] digest = md5.digest(android_id.getBytes());
String deviceId = Base64.encodeToString(digest, 0);

AdRequest adRequest = new dRequest.Builder().addTestDevice("17BB429573B0FA8677D27957B98C7005").build();
adView.loadAd(adRequest);
