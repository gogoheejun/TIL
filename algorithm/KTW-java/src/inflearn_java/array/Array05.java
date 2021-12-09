package inflearn_java.array;

//소수판별(에라토스테네스 체)
public class Array05 {
    public int solution(int n){
        int answer = 0;
        int[] ch = new int[n+1];
        for(int i=2; i<=n; i++){
            if(ch[i] == 0){
                answer++;
                //i의 배수는 소수가 아니므로 다 체크
                for(int j=i; j<=n; j=j+i) ch[j] = 1;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        inflearn_java.array.Array05 T = new inflearn_java.array.Array05();

        System.out.println(T.solution(20));
    }
}