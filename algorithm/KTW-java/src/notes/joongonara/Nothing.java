package notes.joongonara;

public class Nothing {
    public String solution(String vote){
        String answer = "";
        int aCount = 0;
        int bCount = 0;
        int cCount = 0;

        char[] votes = vote.toCharArray();
        for(char x : votes){
            if(x == 'A') aCount++;
            if(x == 'B') bCount++;
            if(x == 'C') cCount++;
        }

        int totalCount = votes.length;

        if(cCount >= totalCount/2) answer= "C";
        else if(aCount > bCount) answer = "A";
        else if(bCount > aCount) answer = "B";
        else answer = "AB";

        return answer;
    }

    public static void main(String[] args){
        Nothing T = new Nothing();
        System.out.println(T.solution("AAACABCBBB"));
    }
}