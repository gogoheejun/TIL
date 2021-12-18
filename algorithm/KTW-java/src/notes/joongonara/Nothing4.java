package notes.joongonara;

import java.util.*;

public class Nothing4 {
    public int solution(int K, String[] user_scores){
        int answer = 0;
        HashMap<String, Integer> scoreMap = new HashMap<>();
        ArrayList<String> orderList = new ArrayList<>();

        for(String userScore : user_scores){
            String user = userScore.split(" ")[0];
            int score = Integer.parseInt(userScore.split(" ")[1]);

            //기존보다 높은 경우에만 새 점수로 업데이트
            int originalScore = scoreMap.getOrDefault(user,-1);
            if(originalScore < score) scoreMap.put(user, score);

            //업데이트 된 점수가 몇등인지 비교
            //첨엔 일단 add
            if(orderList.size() < K && !orderList.contains(user)) {
                //제일 처음
                if(orderList.isEmpty()) {
                    orderList.add(user);
                    answer++;
                    continue;
                }
                //순서 지키며 넣기
                else{
                    int size = orderList.size();
                    for(int i=0; i<size; i++){
                        if(score> scoreMap.get(orderList.get(i))){
                            orderList.add(i,user);
                            answer++;
                            continue;
                        }
                    }
                    orderList.add(user);
                    answer++;
                    continue;
                }
            }

            //orderList 개수가 k개 이상일 때부터
            for(int i=0; i<K; i++){
                //내가 나오는 경우 더이상 비교 안함
                if(orderList.get(i).equals(user)) break;

                //순위권 애들보다 점수가 높으면 안에 껴넣기
                if(score > scoreMap.get(orderList.get(i))){
                    orderList.add(i,user);
                    orderList.remove(K); //마지막애는 탈락
                    answer++;
                    break;
                }
            }
        }


        return answer;
    }

    public static void main(String[] args){
        Nothing4 T = new Nothing4();

        String[] scores = new String[]{"alex111 100", "cheries2 200", "coco 150", "luna 100", "alex111 120", "coco 300", "cheries2 110"};
        String[] scores2 = new String[]{"alex111 100", "cheries2 200", "alex111 200", "cheries2 150", "coco 50", "coco 200"};
        String[] scores3 = new String[]{"cheries2 200", "alex111 100", "coco 150", "puyo 120"};
        System.out.println(T.solution(2,scores3));
    }
}