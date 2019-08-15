   public String getUniqueDeviceId() {
        String deviceID = null;
        String androidID = sessionManager.getUniqueID().get("uniqueid");
        if(androidID!=null)
        {
            if(androidID.length()>0)
            {
                return androidID;
            }
            else
            {
                String uniqueId = "";
                //TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                //uniqueId = uniqueId.concat(TelephonyMgr.getDeviceId());
                String buildParams = "99" +
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                        Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                        Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                        Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                        Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                        Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                        Build.USER.length() % 10;
                uniqueId = uniqueId.concat(buildParams);
                uniqueId = uniqueId.concat(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                uniqueId = uniqueId.concat(wm.getConnectionInfo().getMacAddress());
//                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                uniqueId = uniqueId.concat(mBluetoothAdapter.getAddress());
                // Initiate digest with MD5
                MessageDigest mDigest = null;
                try {
                    mDigest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                mDigest.update(uniqueId.getBytes(), 0, uniqueId.length());
                byte[] digestedBytes = mDigest.digest();
                String deviceId = "";
                for (int i = 0; i < digestedBytes.length; i++) {
                    int b = (0xFF & digestedBytes[i]);
                    // Additional Padding
                    if (b <= 0xF) {
                        deviceId += "0";
                    }
                    // concat at the end
                    deviceId = deviceId.concat(Integer.toHexString(b));
                }
                deviceID  = deviceId.substring(0, 24);
                sessionManager.createUniqueID(deviceID);
                return deviceID;
            }
        }
        else
        {
            String uniqueId = "";
            //TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            //uniqueId = uniqueId.concat(TelephonyMgr.getDeviceId());
            String buildParams = "99" +
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
            uniqueId = uniqueId.concat(buildParams);
            uniqueId = uniqueId.concat(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            uniqueId = uniqueId.concat(wm.getConnectionInfo().getMacAddress());
//            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            uniqueId = uniqueId.concat(mBluetoothAdapter.getAddress());
            // Initiate digest with MD5
            MessageDigest mDigest = null;
            try {
                mDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            mDigest.update(uniqueId.getBytes(), 0, uniqueId.length());
            byte[] digestedBytes = mDigest.digest();
            String deviceId = "";
            for (int i = 0; i < digestedBytes.length; i++) {
                int b = (0xFF & digestedBytes[i]);
                // Additional Padding
                if (b <= 0xF) {
                    deviceId += "0";
                }
                // concat at the end
                deviceId = deviceId.concat(Integer.toHexString(b));
            }
            deviceID  = deviceId.substring(0, 24);
            sessionManager.createUniqueID(deviceID);
            return deviceID;
        }
    }
