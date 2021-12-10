package inflearn_java.stackAndQueue;

import java.util.Stack;

//Stack<Character>
public class Sack01_올바른괄호 {
    public String solution(String str){
        Stack<Character> stack = new Stack<>();
        for(char x : str.toCharArray()){
            if(x == '(') stack.push(x);
            else{
                if(stack.isEmpty()) return "NO";
                stack.pop();
            }
        }
        if(!stack.isEmpty()) return "NO";
        return "YES";
    }

    public static void main(String[] args){
        Sack01_올바른괄호 T = new Sack01_올바른괄호();
        String input = "(()(()))(()";
        System.out.println(T.solution(input));
    }
}
