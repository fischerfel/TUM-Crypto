public static void main(String[] args) throws ParseException, NoSuchAlgorithmException {

    String fileName = "bbb.mp4";
    Date date = new Date(System.currentTimeMillis());
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    String monthName = getMonthForInt(month);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int min = calendar.get(Calendar.MINUTE);

    System.out.println("year " +year +", month "+ month +",day " +day + ", hour " +hour +" min "+ min);

    String str = monthName +" "+ day+" "+year+" "+hour+":"+min+":52.454 UTC";
    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
    Date newDate = df.parse(str);
    long epoch = date.getTime();


    System.out.println(epoch);
    String input =  epoch +"/hls/"+fileName+" enigma";

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(input.getBytes());
    byte[] enc = md.digest();
    String md5Sum = new sun.misc.BASE64Encoder().encode(enc);
    String hash = md5Sum.substring( 0,md5Sum.length() -2);

    String   link = "http://tutorme.ae/hls/"+fileName+"?md5="+hash+"&expires="+epoch;

    System.out.println(link);
}
