package programmers.kakao;

import java.util.ArrayList;
import java.util.List;

//문제링크: https://programmers.co.kr/learn/courses/30/lessons/17683?language=java
public class Q211208_방금그곡 {
    public String solution(String m, String[] musicinfos) {
        String answerTitle = "(None)";
        int answerPlayTime = 0;
        for (String musicinfo : musicinfos) {
            String[] infoArray = musicinfo.split(",");
            String startTime = infoArray[0];
            String endTime = infoArray[1];
            String title = infoArray[2];
            String melody = infoArray[3];

            //재생시간
            int playTime = getPlayTime(startTime, endTime);
            //재생시간동안 재생된 멜로디
            List<String> realMelody = getPlayedMelody(playTime, melody);
//            System.out.println(realMelody);
            //포함여부
//            if (containsMyMelody(realMelody,m) && playTime > answerPlayTime){
//                answerTitle = title;
//                answerPlayTime = playTime;
//            }
        }
        return answerTitle;
    }

    private boolean containsMyMelody(char[] realMelody, String m) {
        char[] myMelody = m.toCharArray();
        int j = 0;
        for (char c : realMelody) {
            if (c == myMelody[j]) {
                j++;
                if (j == myMelody.length) return true;
            } else {
                j = 0;
            }
        }
        return false;
    }

    private List<String> getPlayedMelody(int playTime, String melody) {
//        char[] realMelody = new char[playTime];
        char[] melodyChars = melody.toCharArray();
        List<String> realMelody = new ArrayList<>();
        for(int i=0; i<melodyChars.length; i++){
            if(melodyChars[i+1] == '#'){
                continue;
            }else{
                String now = melodyChars[i]+"";
                if(melodyChars[i] == '#'){
                    now = melodyChars[i-1]+"#";
                }
                realMelody.add(now);
            }
        }

        List<String> playedMelody = new ArrayList<>();
        int j=0;
        for(int i=0; i<playTime; i++){
            if(j>realMelody.size()) j = 0;
            playedMelody.add(realMelody.get(j));
            j++;
        }
        System.out.println("playedMelody: "+playedMelody);
        return realMelody;
    }

    private int getPlayTime(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int starMin = Integer.parseInt(startTime.split(":")[1]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);
        int endMin = Integer.parseInt(endTime.split(":")[1]);

        return (endHour-startHour)*60 + (endMin - starMin);
    }

    public static void main(String[] args){
        Q211208_방금그곡 T = new Q211208_방금그곡();
        System.out.println(T.solution("ABC",new String[]{"12:00,12:14,HELLO,C#DEFGAB", "13:00,13:05,WORLD,ABCDEF"}));
    }
}
