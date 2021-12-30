package backjoon;

import java.util.Scanner;

public class 괄호9012 {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] inputs = new String[n];
        for(int i=0; i<n; i++){
            inputs[i] = in.nextLine();
        }

        System.out.println(inputs[0]+"  "+ inputs[1]);

        in.close();
    }
}
