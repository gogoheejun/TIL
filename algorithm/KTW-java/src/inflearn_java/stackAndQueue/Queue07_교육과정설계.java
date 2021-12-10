package inflearn_java.stackAndQueue;

import java.util.LinkedList;
import java.util.Queue;

public class Queue07_교육과정설계 {
    public String solution(String need, String plan){
        String answer = "YES";
        Queue<Character> Q = new LinkedList<>();
        for(char x : need.toCharArray()) Q.offer(x);

        for(char x : plan.toCharArray()){
            if(Q.contains(x)){
                if(x != Q.poll()) return "NO";
            }
        }
        if(!Q.isEmpty()) return "NO";
        return answer;
    }

    public static void main(String[] args){
        Queue07_교육과정설계 T = new Queue07_교육과정설계();
        System.out.println(T.solution("CBA","CBDAGE"));
    }
}
