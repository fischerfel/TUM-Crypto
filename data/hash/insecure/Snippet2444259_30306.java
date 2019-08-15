String hashString = "";
        Map<String, String> sortedMap = null;
        if (parameters instanceof TreeMap) {
            sortedMap = (TreeMap<String, String>) parameters; 
        } else {
            sortedMap = new TreeMap<String, String>(parameters);
        }

        try {
            Iterator<String> iter = sortedMap.keySet().iterator();
            StringBuilder sb = new StringBuilder();
            synchronized (iter) {
                while (iter.hasNext()) {
                    String key = iter.next();
                    sb.append(key);
                    sb.append("=");
                    String value = sortedMap.get(key);
                    sb.append(value == null ? "" : value);
                }
            }
            sb.append(secret);

            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] digested = digest.digest(sb.toString().getBytes());

            BigInteger bigInt = new BigInteger(1, digested);
            hashString = bigInt.toString(16);
            while (hashString.length() < 32) {
                hashString = "0" + hashString;
            }
        } catch (NoSuchAlgorithmException nsae) {
            // TODO: handle exception
            logger.error(e.getLocalizedMessage(), e);
        }

        return hashString;
