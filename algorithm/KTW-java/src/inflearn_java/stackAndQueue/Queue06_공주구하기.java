package inflearn_java.stackAndQueue;

import java.util.LinkedList;
import java.util.Queue;

//q.offer(x) x 오른쪽에 추가
//q.poll() 맨왼쪽꺼 꺼내기(왼쪽에서 오른쪽으로 쌓인다는 가정하에)
//q.peek() 맨왼쪽꺼 뭔지 확인만 하기
//q.contains(x) x있는지 확인하기- 중요
public class Queue06_공주구하기 {
    public int solution(int n, int k){
        int answer = 0;
        Queue<Integer> Q = new LinkedList<>();
        for(int i=1; i<=n; i++) Q.offer(i);
        while(!Q.isEmpty()){
            //k=3이라면 앞 두번은 그냥 뒤로 다시 보내고
            for(int i=1; i<k; i++) Q.offer(Q.poll());
            //그다음애를 ㄹㅇ로 삭제
            Q.poll();
            //마지막 한명 남으면 당첨
            if(Q.size()==1) answer = Q.poll();
        }
        return answer;
    }

    public static void main(String[] args){
        Queue06_공주구하기 T = new Queue06_공주구하기();
        System.out.println(T.solution(8,3));
    }
}
