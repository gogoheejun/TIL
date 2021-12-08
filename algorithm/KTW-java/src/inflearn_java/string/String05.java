package inflearn_java.string;

import java.util.ArrayList;

//특정문자 뒤집기
//String.valueOf(s);
public class String05 {
    public String solution(String str){
        String answer;
        char[] s = str.toCharArray();
        int lt = 0, rt = str.length()-1;
        while(lt<rt){
            if(!Character.isAlphabetic(s[lt]))lt++;
            else if(!Character.isAlphabetic(s[rt])) rt--;
            else{
                char tmp = s[lt];
                s[lt] = s[rt];
                s[rt] = tmp;
                lt++;
                rt--;
            }
        }
        answer = String.valueOf(s);
        return answer;
    }

    public static void main(String[] args){
        String05 T = new String05();
        System.out.println(T.solution("abcd#e!fg"));
    }
}
