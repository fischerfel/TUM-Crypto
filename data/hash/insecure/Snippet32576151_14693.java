import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;


public class MP1 {


private static class WordCountComparator implements Comparator<Map.Entry<String, Integer>> {
    public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
    {
        if(o2.getValue().compareTo( o1.getValue()) != 0) {
            return o2.getValue().compareTo(o1.getValue());
        } 
        else {
            return o1.getKey().compareTo(o2.getKey());
        }
    }
}

Random generator;
String userName;
String inputFileName;
String delimiters = " \t,;.?!-:@[](){}_*/";
String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
        "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
        "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
        "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
        "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
        "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
        "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
        "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
        "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
        "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA");
    messageDigest.update(seed.toLowerCase().trim().getBytes());
    byte[] seedMD5 = messageDigest.digest();

    long longSeed = 0;
    for (int i = 0; i < seedMD5.length; i++) {
        longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
    }

    this.generator = new Random(longSeed);
}

Integer[] getIndexes() throws NoSuchAlgorithmException {
    Integer n = 10000;
    Integer number_of_lines = 50000;
    Integer[] ret = new Integer[n];
    this.initialRandomGenerator(this.userName);
    for (int i = 0; i < n; i++) {
        ret[i] = generator.nextInt(number_of_lines);
    }
    return ret;
}

public MP1(String userName, String inputFileName) {
    this.userName = userName;
    this.inputFileName = inputFileName;
}

public String[] process() throws Exception {
    String[] ret = new String[20];

    File file = new File(this.inputFileName);
    Scanner scanner = new Scanner(file);
    String[] lines = new String[50000];

    int i = 0;
    while(scanner.hasNextLine()){
        lines[i] = scanner.nextLine();
        i++;
    }
    Integer[] indices = getIndexes();

    String[] records = new String[10000];

    //ArrayList<String> words = new ArrayList<String>();
    Map<String, Integer> wordCount = new HashMap<String, Integer>();

    i = 0;
    for(Integer index:indices){

        records[i] = lines[index].toLowerCase().trim();
        StringTokenizer tokenOfString = new StringTokenizer(records[i], this.delimiters);
        i++;

        while(tokenOfString.hasMoreTokens()){

            String token = tokenOfString.nextToken();

            if(!Arrays.asList(stopWordsArray).contains(token)){

                if(wordCount.get(token) == null) {

                    wordCount.put(token,1);
                }
                else{
                    wordCount.put(token, wordCount.get(token + 1));
                }

                }
            }
        }
    List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());
    Collections.sort(list, new WordCountComparator() );

    for (i = 0; i < 20; i++ ){

        ret[i] = list.get(i).getKey() + "\t" + Integer.toString(list.get(i).getValue());

    }

    return ret;

}

public static void main(String[] args) throws Exception {
    if (args.length < 1){
        System.out.println("MP1 <User ID>");
    }
    else {
        String userName = args[0];
        String inputFileName = "/home/unknown/git/cloudapp-mp1/input.txt";
        MP1 mp = new MP1(userName, inputFileName);
        String[] topItems = mp.process();
        for (String item: topItems){
            System.out.println(item);
        }
    }
}
