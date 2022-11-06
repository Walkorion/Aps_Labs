package LAB3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

class GreedyCoins {

    public static int minNumCoins(int coins[], int sum) {
        //Vasiot kod ovde

        // vnesueme niza so moneti
        // vnesueme suma za kako preku tie moneti da ja dobieme sumata
        //pr: 5 1 2 10   i suma 19  -> rez: 4
        // treba da iskoristime 4 moneti


        //sortirana e 1 2 5 10
        Arrays.sort(coins);
        int br=0;

        //vrtime obratno
        for(int i= coins.length-1; i>=0; i--){
            while(sum >= coins[i]){
                sum-=coins[i];
                //da broime kolku moneti ni treba
                br++;
            }
        }

        return br;


    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String coinsStringLine = input.nextLine();
        String coinsString[] = coinsStringLine.split(" ");
        int coins[] = new int[coinsString.length];
        for(int i=0;i<coinsString.length;i++) {
            coins[i] = Integer.parseInt(coinsString[i]);
        }

        int sum = input.nextInt();

        System.out.println(minNumCoins(coins, sum));
    }
}