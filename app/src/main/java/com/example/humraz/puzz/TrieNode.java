package com.example.humraz.ghost2;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private char value;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        value = '\0';
    }

    public TrieNode(char c) {
        children = new HashMap<>();
        isWord = false;
        value = c;
    }

    public TrieNode findNode(String s) {
        char current = s.charAt(0);
        if (this.children.containsKey(current)) {
            TrieNode child = this.children.get(current);
            return child.findNode(s.substring(1, s.length()-1));
        } else {
            return null;
        }
    }

    public void add(String s) {
        if (s != null) {
            Character current = s.charAt(0);
            if (s.length() >= 1) {
                String restOfTheWord = s.substring(1, s.length() - 1);
                if (!this.children.containsKey(current)) {
                    TrieNode child = new TrieNode(current);
                    this.children.put(current, child);
                    child.add(restOfTheWord);
                } else {
                    TrieNode child = this.children.get(current);
                    child.add(restOfTheWord);
                }

                if (s.length() == 1)
                    this.isWord = true;
            }
        }
    }

    public boolean isWord(String s) {
        char current = s.charAt(0);
        if (s.length() > 1) {
            if (this.children.containsKey(current)) {
                TrieNode child = this.children.get(current);
                return child.isWord(s.substring(1, s.length()-1));
            } else {
                return false;
            }
        } else if (s.length() == 1) {
            return this.isWord(s);
        }
        return false;
    }

    public void findWords(String prefix, TrieNode temp, ArrayList<String> words) {
        for (TrieNode x: temp.children.values()) {
            String currentStr = prefix + x.value;
            if (x.isWord) words.add(currentStr);
            if (x.children.size() > 0) findWords(currentStr, x, words);
        }
    }

    public String getAnyWordStartingWith(String s) {
        ArrayList<String> words = new ArrayList<>();
        Random r = new Random();
        String prefix;
        if (s == null) {
            char randchar = (char) (r.nextInt(26) + 'a');
            prefix = Character.toString(randchar);
        } else {
            prefix = s;
        }

        TrieNode temp = this.findNode(prefix);
        if (temp != null) {
            if (temp.isWord) words.add(prefix);
            this.findWords(prefix, temp, words);

            if (words.size() != 0) {
                return words.get(r.nextInt(words.size()));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getGoodWordStartingWith(String s) {
        ArrayList<String> words = new ArrayList<>();
        Random r = new Random();
        String prefix;
        if (s == null) {
            char randchar = (char) (r.nextInt(26) + 'a');
            prefix = Character.toString(randchar);
        } else {
            prefix = s;
        }

        TrieNode temp = this.findNode(prefix);
        if (temp != null) {
            if (temp.isWord) words.add(prefix);
            this.findWords(prefix, temp, words);

            if (words.size() != 0) {
                int siz = r.nextInt(words.size());
                String str = words.get(siz);
                /*if (words.size() != 1 && s != null) {
                    while ((str.length() == s.length() + 1)) {
                        // words.remove(i);
                        i = r.nextInt(words.size());
                        str = words.get(i);
                    }
                }*/
                return str;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
