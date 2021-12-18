package notes.joongonara;

public class Nothing3 {
    public int solution(int n){
        //5로 먼저 나누기
        int threeBox = 0;
        int fiveBox = n / 5;
        while(fiveBox >= 0){
            int c = n - 5*fiveBox;
            if(c == 0) return fiveBox + threeBox;

            //나머지를 3으로 나누기
            if(c % 3 == 0){
                threeBox = c/3;
                return fiveBox + threeBox;
            }
            fiveBox--;
        }
        return -1;
    }

    public static void main(String[] args){
        Nothing3 T = new Nothing3();
        System.out.println(T.solution(21));
    }
}