package inflearn_java.dfsAndBfsUse;

import java.util.Arrays;
import java.util.Collections;

//DFS 어렵다. 꼭 다시 해보자.프로그래머스에서 연습 다시 해보기
//내림차순과 answer값 체크해서 return시키기 통해서 시간 단축하기
public class DFS05_동전교환 {
    static int n, m, answer = Integer.MAX_VALUE;
    public void DFS(int L, int sum, Integer[] arr){
        if(sum>m);
        //합계가 최소값인 answer를 초과하면 더진행할 필요 없다
        if(L>=answer) return;
        if(sum == m){
            answer = Math.min(answer, L);
        }else {
            for(int i=0; i<n; i++){
                DFS(L+1, sum+arr[i], arr);
            }
        }
    }


    public static void main(String[] args) {
        DFS05_동전교환 T = new DFS05_동전교환();
        n = 3;
        Integer[] arr = new Integer[]{1,2,5};
        m = 15;
        //큰 숫자 먼저 넣으면 시간 빨라짐
        //단, Collections.reverseOrder()는 기본타입(int)는 안먹힘...Integer로 해야함.
        Arrays.sort(arr, Collections.reverseOrder());
        T.DFS(0,0,arr);

        System.out.println(answer);
    }
}
