package inflearn_java.string;

//문장 속 가장 긴 단어 찾기
public class String03 {
    public String solution(String str){
        String[] strings = str.split(" ");
        int max = 0;
        String answer = "";
        for(String s : strings){
            int length = s.length();
            if(length > max)  {
                max = Math.max(length,max);
                answer = s;
            }
        }
        return answer;
    }

    public static void main(String[] args){
        String03 T = new String03();
        System.out.println(T.solution("it is time to study"));
    }
}
