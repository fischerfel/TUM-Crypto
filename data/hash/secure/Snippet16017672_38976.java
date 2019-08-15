otp=otp.concat(imei).concat(imsi).concat(username).concat(password).concat(hr).concat(min).concat(day).concat(year).concat(month).concat(dday);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(otp.getBytes(),0,otp.getBytes().length);
            byte byteData[]=null;
            md.digest(byteData,0,32);
            StringBuffer sb = new StringBuffer();
            for (int  j= 0; j < byteData.length; j++) {
            sb.append(Integer.toString((byteData[j] & 0xff) + 0x100, 16).substring(1));
            }
