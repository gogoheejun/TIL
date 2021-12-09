package inflearn_java.array;

import java.util.Arrays;

//등수 구하기(같은 점수면 같은 등수 여러명)
public class Array08 {
    public int[] solution(int n, int[] arr){
        int[] answer= new int[n];
        for(int i=0; i<n; i++){
            int cnt = 1;
            for(int j=0; j<n; j++){
                if(arr[j] > arr[i]) cnt++;
            }
            answer[i] = cnt;
        }

        return answer;
    }

    public static void main(String[] args) {
        Array08 T = new Array08();

        System.out.println(Arrays.toString(T.solution(6, new int[]{87, 89, 92, 100, 76,89})));
    }
}