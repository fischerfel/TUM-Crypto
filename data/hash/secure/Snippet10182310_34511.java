private static final String apiKey = "38m8nyev284nddci49940303094"; 
private static final String apiUser = "esdt34ds"; 

long unixTimeStamp = System.currentTimeMillis() / 1000L;

String newFeedRequest = "1.0/evoStructure?timestamp=" + unixTimeStamp;
String fixturesFeedURL = "https://secure.website.com/_services/api/" + newFeedRequest;

MessageDigest md = MessageDigest.getInstance("SHA-256");


md.update(fixturesFeedURL.getBytes("UTF-8"),apiKey.getBytes("UTF-8"),apiUser.getBytes("UTF-8")); // Change this to "UTF-16" if needed
byte[] digest = md.digest();
