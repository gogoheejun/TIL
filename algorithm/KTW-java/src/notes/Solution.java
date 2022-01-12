package notes;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int firstUniqChar(String s) {
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        for(int i=0; i<s.length(); i++){
            if(map.getOrDefault(chars[i],0) > 1){

            }else{
                map.put(chars[i],1);
            }
        }
    }
}