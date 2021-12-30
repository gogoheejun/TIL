package inflearn_java.dfsAndBfsUse;

import java.util.Scanner;


public class DFS13_섬나라아일랜드 {
    static int answer = 0, n;
    static int[] dx = {-1,-1,0,1,1,1,0,-1};
    static int[] dy = {0,1,1,1,0,-1,-1,-1};

    public void DFS(int x, int y, int[][] board){
        for(int i=0; i<8; i++){
            int nx = x + dy[i];
            int ny = y + dy[i];
            if(nx >= 0 && nx<n && ny>=0 && board[nx][ny] == 1){
                board[nx][ny] = 0;
                DFS(nx, ny, board);
            }
        }
    }
    public void solution(int[][] board){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(board[i][j] == 1){
                    answer++;
                    board[i][j] = 0; //출발점을 0으로 바꾸고 움직여야함
                    DFS(i,j,board);
                }
            }
        }
    }

    public static void main(String[] args) {
        DFS13_섬나라아일랜드 T = new DFS13_섬나라아일랜드();
        Scanner kb = new Scanner(System.in);
        n = kb.nextInt();
        int[][] arr = new int[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; i<n; j++){
                arr[i][j] = kb.nextInt();
            }
        }
        T.solution(arr);
        System.out.println(answer);
    }
}
