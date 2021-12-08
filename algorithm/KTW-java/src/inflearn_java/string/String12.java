package inflearn_java.string;

//암호
//replace, 이진법변환
public class String12 {
    public String solution(int n,String s){
        String answer = "";
        for(int i=0; i<s.length(); i++){
            String tmp = s.substring(0,7).replace('#','1').replace('*','0'); //0번인덱스부터 7번인덱스 전까지
            int num = Integer.parseInt(tmp, 2);
            answer+=(char)num;
            s = s.substring(7);
        }
        return answer;
    }

    public static void main(String[] args){
        String12 T = new String12();
        System.out.println(T.solution(4,"#****###**#####**#####**##**"));
    }
}
