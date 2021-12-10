package inflearn_java.hash;

import java.util.ArrayList;
import java.util.HashMap;
//map.getOrDefault, map.size() 키종류 뽑아옴, map.remove(키) 해당키랑 벨류 삭제
public class Hash02_매출액종류 {
    public ArrayList<Integer> solution(int n, int k, int[] arr){
        ArrayList<Integer> answer = new ArrayList<>();
        HashMap<Integer,Integer> HM = new HashMap<>();
        for(int i=0; i<k-1; i++){
            HM.put(arr[i], HM.getOrDefault(arr[i], 0)+1);
        }
        int lt = 0;
        for(int rt = k-1; rt<n; rt++){
            HM.put(arr[rt],HM.getOrDefault(rt,0)+1);
            answer.add(HM.size());
            HM.put(arr[lt], HM.get(arr[lt])-1);
            if(HM.get(arr[lt]) == 0) HM.remove(arr[lt]);
            lt++;
        }
        return answer;
    }
    public static void main(String[] args){
        Hash02_매출액종류 T = new Hash02_매출액종류();
        System.out.println(T.solution(7,4, new int[]{20,12,20,10,23,17,10}));
    }
}
