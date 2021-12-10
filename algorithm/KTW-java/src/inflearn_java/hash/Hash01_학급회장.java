package inflearn_java.hash;

import java.util.HashMap;

public class Hash01_학급회장 {
    public char solution(int n, String s){
        char answer = ' ';
        HashMap<Character, Integer> map = new HashMap<>();
        for(char x: s.toCharArray()){
            map.put(x,map.getOrDefault(x,0)+1);
        }
        int max = 0;
        for(char key : map.keySet()){
            if(map.get(key) > max){
                max = map.get(key);
                answer = key;
            }
        }
        return answer;
    }
    public static void main(String[] args){
        Hash01_학급회장 T = new Hash01_학급회장();
        System.out.println(T.solution(15,"BACBACCACCBDEDE"));
    }
}
