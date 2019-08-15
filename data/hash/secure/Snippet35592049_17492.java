  Instructors hash code in integers  
    //106 | 31 | 65 | 19 | 230 | 164 | 247 | 192 | 75 | 119 | 40 | 12 | 5 |
    //402 | 78 | 87 | 193 | 75 | 125 | 234 | 113 | 109 | 181 | 229 | 216 | 
    //56 | 76 | 109 | 213 | 247 | 123 | 126
    char c1 = (char) 106;
    char c2 = (char) 31;
    char c3 = (char) 65;
    char c4 = (char) 19;
    char c5 = (char) 230;
    char c6 = (char) 164;
    char c7 = (char) 247;
    char c8 = (char) 192;
    char c9 = (char) 75;
    char c10 = (char) 119;
    char c11 = (char) 40;
    char c12 = (char) 12;
    char c13 = (char) 5;
    char c14 = (char) 402;
    char c15 = (char) 78;
    char c16 = (char) 87;
    char c17 = (char) 193;
    char c18 = (char) 75;
    char c19 = (char) 125;
    char c20 = (char) 234;
    char c21 = (char) 113;
    char c22 = (char) 109;
    char c23 = (char) 181;
    char c24 = (char) 229;
    char c25 = (char) 216;
    char c26 = (char) 56;
    char c27 = (char) 76;
    char c28 = (char) 109;
    char c29 = (char) 213;
    char c30 = (char) 247;
    char c31 = (char) 123;
    char c32 = (char) 126;

    String mystery = ("" + c1 + c2 + c3 + c4 + c5 + c6 + c7 + c8 + c9 + c10 + c11
            + c12 + c13 + c14 + c15 + c16 + c17 + c18 + c19 + c20 + c21
            + c22 + c23 + c24 + c25 + c26 + c27 + c28 + c29 + c30 + c31 + c32);
    System.out.print(mystery);

    File dictionary = new File("Dictionary");
    Scanner in = new Scanner(dictionary);

    String _word;
    char[] word;
    int length;
    boolean[] binVal;
    int[] iteratorVal;
    int iterator;
    boolean flag = false;
    int counter = 0;

    while (in.hasNextLine() && !flag) {
        _word = in.nextLine();
        //counter++;
        //if (counter % 10 == 0) {
        //    System.out.println(_word);
       // }else if(counter > 1000){
        //    counter = 0;
        //}

        word = _word.toCharArray();

        length = (int) (Math.pow(2, word.length));

        binVal = new boolean[word.length];
        iteratorVal = new int[word.length];
        iterator = length;
        for (int i = 0; i < word.length; i++) {
            binVal[i] = true;
            iterator = iterator / 2;                
          //System.out.print(iterator + "||");
            iteratorVal[i] = iterator;
        }

        String permutation = "";
        for (int i = 1; i <= length; i++) {
            //System.out.println(permutation);
            permutation = "";

            for (int n = 0; n < word.length; n++) {

                if (binVal[n] == true) {
                    permutation = permutation + Character.toLowerCase(word[n]);
                    //make uppercase [n]
                } else {
                    permutation = permutation + Character.toUpperCase(word[n]);
                    //make lowercase [n]
                }
            }
            for (int n = 0; n < iteratorVal.length; n++) {
                if (i % iteratorVal[n] == 0) {
                    binVal[n] = !binVal[n];
                }
            }

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(permutation.getBytes());
            String encryptedString = new String(messageDigest.digest());
            if (encryptedString.equals(mystery)) {
                System.out.println("You Found it!!!" + permutation);
                flag = true;
            }
        }
    }

}
 }
