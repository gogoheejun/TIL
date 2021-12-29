package inflearn_java.dfsAndBfsUse;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class TomatoPoint{
    public int x, y;
    TomatoPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
}

public class BFS12_익은토마토 {
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    static int[][]  board, dis;
    static int n, m;
    static Queue<TomatoPoint> Q = new LinkedList<>();

    public void BFS(){
        while(!Q.isEmpty()){
            TomatoPoint tmp = Q.poll();
            for(int i=0; i<4; i++){
                int nx = tmp.x + dx[i];
                int ny = tmp.y + dy[i];
                if(nx >=1 && nx<=7 && ny>=1 && ny<=7 && board[nx][ny] == 0){
                    board[nx][ny] = 1; //지금 온 곳은 0 -> 1로 바꿔주고
                    Q.offer(new TomatoPoint(nx, ny)); // 큐에 상하좌우 가능한 통로좌표 넣어주고
                    dis[nx][ny] = dis[tmp.x][tmp.y] +1; //거리표에 거쳐온 만큼 거리를 더해줌.
                }
            }
        }
    }


    public static void main(String[] args) {
        BFS12_익은토마토 T = new BFS12_익은토마토();
        Scanner kb = new Scanner(System.in);
        m = kb.nextInt();
        n = kb.nextInt();
        board = new int[8][8];
        dis = new int[8][8];
        for(int i=1; i<=n; i++){
            for(int j=1; j<=m; j++){
                board[i][j] = kb.nextInt();
                if(board[i][j] == 1) Q.offer(new TomatoPoint(i,j));
            }
        }
        T.BFS(); //이미 위 48번째 줄에서 출발점을 넣었기 때문에 인자없어도 괜츈함
        boolean flag = true;
        int answer = Integer.MIN_VALUE;
        for(int i=0; i<n; i++){
            for(int j = 0; j<m; j++){
                if(board[i][j] == 0) flag = false;
            }
        }
        if(flag){
            for(int i=0; i<n; i++){
                for(int j=0; j<m; j++){
                    answer = Math.max(answer, dis[i][j]);
                }
            }
        }
    }
}
