package inflearn_java.dfsAndBfsUse;

import java.util.Scanner;


public class DFS10_미로탐색 {
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    static int[][]  board;
    static int answer = 0;

    public void DFS(int x, int y){ //s: start
        if(x == 7 && y==7) answer++;
        else{
            for(int i=0; i<4; i++){
                int nx = x + dx[i];
                int ny = y + dy[i];
                if(nx>=1 && nx<= 7 && ny>=1 && ny<=7 && board[nx][ny] ==0){
                    board[nx][ny] = 1;
                    DFS(nx, ny);
                    board[nx][ny] = 0; //빽할 때 0으로 다시 바꿔줘야 하는 거 주의
                }
            }
        }
    }


    public static void main(String[] args) {
        DFS10_미로탐색 T = new DFS10_미로탐색();
        Scanner kb = new Scanner(System.in);
        board = new int[8][8];
        for(int i=1; i<=7; i++){
            for(int j=1; j<=7; j++){
                board[i][j] = kb.nextInt();
            }
        }
        board[1][1] = 1;
        T.DFS(1,1);
        System.out.println(answer);
    }
}
