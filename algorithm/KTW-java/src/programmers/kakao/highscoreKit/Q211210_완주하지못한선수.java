package programmers.kakao.highscoreKit;

import inflearn_java.string.String01;

import java.util.Arrays;

//문제링크: https://programmers.co.kr/learn/courses/30/lessons/42576
//replaceFirst 적용. 여러명이 있을 때 하나만 바꾸고 싶을때.
public class Q211210_완주하지못한선수 {
    public String solution(String[] participant, String[] completion) {
        String parti = Arrays.toString(participant);
        // System.out.println(parti);
        for(int i=0; i<completion.length; i++){
            parti = parti.replaceFirst(completion[i],"").replace(", ","").replace("[","").replace("]","");
        }
        // System.out.println(parti);
        return parti;
    }

    public static void main(String[] args) {
        Q211210_완주하지못한선수 T = new Q211210_완주하지못한선수();
        String[] participant = new String[]{"leo", "kiki", "eden"};
        String[] completion = new String[]{"eden", "kiki"};
        System.out.println(T.solution(participant, completion));
    }
}
