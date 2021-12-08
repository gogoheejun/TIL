package inflearn_java.string;

import java.util.ArrayList;

//문장 속 가장 긴 단어 찾기
public class String04 {
    public ArrayList<String> solution(int n, String[] str){
        ArrayList<String> answer = new ArrayList<>();
        for(String s : str){
            StringBuffer sb = new StringBuffer(s);
            String reversedStr = sb.reverse().toString();
            answer.add(reversedStr);
        }
        return answer;
    }

    public static void main(String[] args){
        String04 T = new String04();
        String[] strings = {"good","Time","Big"};
        System.out.println(T.solution(3,strings));
    }
}
