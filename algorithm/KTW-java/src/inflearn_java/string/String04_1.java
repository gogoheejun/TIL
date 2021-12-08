package inflearn_java.string;

import java.util.ArrayList;

//문장 속 가장 긴 단어 찾기
//String.valueOf()
public class String04_1 {
    public ArrayList<String> solution(int n, String[] str){
        ArrayList<String> answer = new ArrayList<>();
        for(String x :str){
            char[] s = x.toCharArray();
            int left = 0;
            int right = x.length() -1;
            while(left<right){
                char tmp = s[left];
                s[left] = s[right];
                s[right] = tmp;
                left++;
                right--;
            }
            String tmp = String.valueOf(s);
            answer.add(tmp);
        }

        return answer;
    }

    public static void main(String[] args){
        String04_1 T = new String04_1();
        String[] strings = {"good","Time","Big"};
        System.out.println(T.solution(3,strings));
    }
}
