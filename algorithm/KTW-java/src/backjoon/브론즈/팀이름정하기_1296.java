package backjoon.브론즈;


import java.util.Scanner;

public class 팀이름정하기_1296 {
    public String solution(String name,  int n, String[] teams){

        int biggestOdd = Integer.MIN_VALUE;
        String answer = "";

        for(String team : teams){
            int L = getCount(name, team, 'L');
            int O = getCount(name, team, 'O');
            int V = getCount(name, team, 'V');
            int E = getCount(name, team, 'E');

            int odd = getOdd(L,O,V,E);
            if(odd > biggestOdd) {
                biggestOdd = odd;
                answer = team;
            }else if(odd == biggestOdd && ( 0 < team.compareTo(answer))){
                biggestOdd = odd;
                answer = team;
            }
        }

        return answer;
    }

    private int getOdd(int l, int o, int v, int e) {
        return ((l+o)*(l+v)*(l+e)*(o+v)*(o+e)*(v+e)) % 100;
    }

    private int getCount(String name, String team, char ch) {
        int cnt = 0;
        char[] nameChars = name.toCharArray();
        for(char c : nameChars){
            if( c == ch) cnt++;
        }

        char[] teamChars = team.toCharArray();
        for(char c : teamChars){
            if( c == ch) cnt++;
        }
        return cnt;
    }

    public static void main(String[] args) {
        팀이름정하기_1296 T = new 팀이름정하기_1296();
        Scanner kb = new Scanner(System.in);

        String name = kb.nextLine();
        int n = kb.nextInt();
        String[] teams = new String[n];
        for(int i=0; i<n; i++){
            teams[i] = kb.next();
        }
        kb.close();

        String answer = T.solution(name, n, teams);
        System.out.println(answer);

    }
}
