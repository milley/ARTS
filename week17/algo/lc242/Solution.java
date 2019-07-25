package com.milley.validanagram;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public boolean isAnagram(String s, String t) {
        Map<Character, Integer> sMap = new HashMap<>(s.length());
        Map<Character, Integer> tMap = new HashMap<>(t.length());

        for (Character ch : s.toCharArray()) {
            int v = sMap.get(ch) == null ? 0 : sMap.get(ch);
            sMap.put(ch, ++v);
        }

        for (Character ch : t.toCharArray()) {
            int v = tMap.get(ch) == null ? 0 : tMap.get(ch);
            tMap.put(ch, ++v);
        }

        return sMap.equals(tMap);
    }

    public boolean isAnagramB(String s, String t) {
        int[] sArray = new int[26];
        int[] tArray = new int[26];

        for (char ch : s.toCharArray()) {
            sArray[ch - 'a']++;
        }

        for (char ch : t.toCharArray()) {
            tArray[ch - 'a']++;
        }

        for (int i = 0, j = 0; i < 26 && j < 26;) {
            if (sArray[i] != tArray[j]) {
                return false;
            }
            i++;
            j++;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().isAnagramB("anagram", "nagaram"));
    }
}
