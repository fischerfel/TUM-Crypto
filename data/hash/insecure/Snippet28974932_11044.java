package margana;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Solution {

    private final String givenLetterSet;
    private String file;
    private final ExecutorService executorService = Executors.newFixedThreadPool(16);

    LetterNode root = new LetterNode('\u03A9', null); // omega root node
    private Map<Character, Long> countedOriginalLetters = new HashMap<Character, Long>();

    /**
     * Mixed Anatree class
     */
    public static class LetterNode implements Comparable<LetterNode> {
        private final char letter;// does not matter for the root node
        private boolean ending;
        private Map<Character, LetterNode> leaves = new HashMap<Character, LetterNode>();
        private LetterNode motherNode;
        private String wholeCachedWord;
        private int length = 1;

        public LetterNode(char oneLetter, LetterNode mom) {
            letter = oneLetter;
            if (mom != null) {
                if (mom.motherNode != null) {
                    length += mom.length;// all consecutive nodes minus mom length
                }
                motherNode = mom;
            }
        }

        public char getLetter() {
            return letter;
        }

        public Character getCharacter() {
            return Character.valueOf(letter);
        }

        public boolean isEnding() {
            return ending;
        }

        public void setEnding(boolean ending) {
            this.ending = ending;
        }

        public Map<Character, LetterNode> getLeaves() {
            return leaves;
        }

        public int getLength() {
            return length;
        }

        public LetterNode getMotherNode() {
            return motherNode;
        }

        public String compileNodesIntoWord() {
            if (wholeCachedWord != null) {
                return wholeCachedWord;
            }
            LetterNode node = motherNode;
            StringBuilder buffer = new StringBuilder(length);
            buffer.append(letter);
            while (node.motherNode != null) {
                buffer.insert(0, node.letter);
                if (node.motherNode.motherNode == null) {
                    break;
                }
                node = node.motherNode;
            }
            wholeCachedWord = buffer.toString();
            return wholeCachedWord;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LetterNode that = (LetterNode) o;
            if (letter != that.letter) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return (int) letter;
        }

        @Override
        public int compareTo(LetterNode o) {
            return Character.compare(letter, o.letter);
        }

        @Override
        public String toString() {
            if (ending) {
                return compileNodesIntoWord();
            }
            return String.valueOf(letter);
        }
    }

    public Solution(String anagram, String dictionaryFile) {
        file = dictionaryFile;
        byte[] tempArray = anagram.toLowerCase().replaceAll(" ", "").getBytes();
        Arrays.sort(tempArray);
        givenLetterSet = new String(tempArray);
        for (char oneChar : anagram.toLowerCase().toCharArray()) {
            Long numberOfOccurrences = countedOriginalLetters.get(Character.valueOf(oneChar));
            if (numberOfOccurrences == null) {
                countedOriginalLetters.put(new Character(oneChar), new Long(1));
            } else {
                countedOriginalLetters.put(new Character(oneChar), new Long(numberOfOccurrences.longValue() + 1));
            }
        }
    }

    /**
     * Rule out rubbish words
     *
     * @param oneWord
     * @return
     */
    private boolean invalidAgainstGivenSentence(String oneWord) {
        if (oneWord.length() > givenLetterSet.length()) {
            return true;
        }
        for (char oneChar : oneWord.toLowerCase().toCharArray()) {
/*            if (oneChar == "'".charAt(0)) {// to regards ' as a letter
                continue;
            }*/
            Long amountOfParticularLetter = countedOriginalLetters.get(Character.valueOf(oneChar));
            if (amountOfParticularLetter == null) {
                return true;
            }
        }
        return false;
    }

    public void growTree() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String oneWord;
        long depth = 0; // for fun
        long candidate = 0;
        boolean isNewWord = false;
        while ((oneWord = br.readLine()) != null) {
            if (invalidAgainstGivenSentence(oneWord)) {
                continue;//is not a valid chunk of the given anagram
            }
            LetterNode previousNode = root;
            isNewWord = false;
            for (char one : oneWord.toCharArray()) {
                LetterNode currentLetter = previousNode.getLeaves().get(Character.valueOf(one));
                if (currentLetter == null) {// letter does not exists, let us add it
                    LetterNode newNode = new LetterNode(one, previousNode);
                    previousNode.getLeaves().put(Character.valueOf(one), newNode);
                    currentLetter = newNode;
                    isNewWord = true;
                }
                previousNode = currentLetter;
            }
            if (isNewWord) {
                candidate += 1;
            }
            previousNode.setEnding(true);
            depth = Math.max(depth, previousNode.getLength());
        }
        System.out.println("Created an anatree comprising of " + candidate + " words, and " + depth + " levels");
        br.close();
    }

    public void solve(String md5) throws NoSuchAlgorithmException {
        List<LetterNode> foundWords = new ArrayList<LetterNode>();
        LinkedList<Character> input = new LinkedList<Character>();
        Set<Character> inputSet = new HashSet<Character>();
        for (Character one : givenLetterSet.toCharArray()) {
            input.add(one);
            inputSet.add(one);
        }
        NavigableSet<LetterNode> firstLevel = new TreeSet(root.getLeaves().values()).descendingSet();
        for (LetterNode node: firstLevel) {
            if (inputSet.contains(node.getCharacter())) {
                executorService.execute(new SolverRunnable(foundWords, input, node, md5.toLowerCase()));
            }
        }
        executorService.shutdown();
    }

    class SolverRunnable implements Runnable {
        private List<LetterNode> initialWords;
        private List<Character> spareCharacters;
        private LetterNode initialNode;
        private String md5Hash;

        public SolverRunnable(List<LetterNode> foundWords, List<Character> spareLetters, LetterNode route, String md5) {
            initialNode = route;
            initialWords = foundWords;
            spareCharacters = spareLetters;
            md5Hash = md5;
        }

        public void run() {
            System.out.println("Started solving branch '" + initialNode.getCharacter() + "' from root ");
            try {
                solve(initialWords, spareCharacters, initialNode, md5Hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private void solve(List<LetterNode> foundWords, List<Character> spareLetters, LetterNode route, String md5) throws NoSuchAlgorithmException {
        List<LetterNode> localFoundWords = new ArrayList<LetterNode>(foundWords);
        List<Character> workspace = new LinkedList<Character>();
        LetterNode current = route;
        workspace.addAll(spareLetters);
        while (!current.getLeaves().isEmpty()) {
            if (!workspace.contains(current.getCharacter())) {
                break;
            }
            workspace.remove(current.getCharacter());
            if (current.getLeaves().size() > 1) {// start solving recursively then quit
                for (LetterNode node: new TreeSet<LetterNode>(current.getLeaves().values())) {//checking every branch
                    if (workspace.contains(node.getCharacter())) {
                        solve(localFoundWords, workspace, node, md5);
                    }
                }
                break;//we solve routes without forks
            }
            if (workspace.isEmpty()) {
                break;
            }
            if (current.isEnding()) {//recursively solving a shorter word first then continue
                localFoundWords.add(current);
                startOver(workspace, localFoundWords, md5);
                localFoundWords.remove(current);
            }
            current = (LetterNode) current.getLeaves().values().toArray()[0];
        }
        if (current.isEnding()) {
            localFoundWords.add(current);
            workspace.remove(current.getCharacter());
            if (workspace.isEmpty()) {
                check(localFoundWords, md5);
                return;
            }
            startOver(workspace, localFoundWords, md5);
        }
    }

    private void check(List<LetterNode> localFoundWords, String md5) throws NoSuchAlgorithmException {
        if (isPreliminaryValid(localFoundWords)) {
            String phrase = concatenateNodesWithSpaces(localFoundWords);
            if (md5.equalsIgnoreCase(digest(phrase))) {
                System.out.println(phrase);
                executorService.shutdownNow();
                System.exit(0);
            }
        }
    }

    private void startOver(List<Character> workspace, List<LetterNode> localFoundWords, String md5) throws NoSuchAlgorithmException {
        for (LetterNode node: root.getLeaves().values()) {
            if (workspace.contains(node.getCharacter())) {
                solve(localFoundWords, workspace, node, md5);
            }
        }
    }

    public boolean isPreliminaryValid(List<LetterNode> words) {
        StringBuilder builder = new StringBuilder();
        int total = 0;
        for (LetterNode word : words) {
            builder.append(word.compileNodesIntoWord());
            total += word.length;
        }
        if (total != givenLetterSet.length()) {
            return false;
        }
        char[] letters = builder.toString().toCharArray();
        Arrays.sort(letters);
        return new String(letters).equals(givenLetterSet);
    }

    public static String concatenateNodesWithSpaces(List<LetterNode> words) {
        StringBuilder builder = new StringBuilder();
        int spaces = words.size() - 1;
        for (LetterNode word : words) {
            builder.append(word.compileNodesIntoWord());
            if (spaces > 0) {
                spaces--;
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static String digest(String original) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        StringBuilder sb = new StringBuilder(34);
        for (byte b : md.digest()) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Solution s = new Solution(args[0], args[1]);
        s.growTree();
/*
        s.solve("BE2B1B1409746B5416F44FB6D9C16A55");// cop pop
        //s.solve("493DF2D8AC7EDB14CD50CA07A539A805");// cop p'op
*/
        s.solve(args[2]); //frisco bay area
    }

}
