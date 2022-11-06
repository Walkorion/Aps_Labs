package LAB3;

import java.util.Scanner;

class MinDistance {

    public static float minimalDistance(float points[][]) {
        //Vasiot kod ovde
        // sqrt(sqr(x2-x1) + sqr(y2-y1))

        //x [i][0]  y[i][1]

        float x1, x2, y1, y2;
        float d;
        float min=99999;
        int suma=0;

//        if(i<= points.length){
//
//        }

        for(int i=0; i< points.length; i++){
            //da odi od prvo so naredno
            for(int j=i+1; j< points.length; j++){
                x1 = points[i][0];
                y1 = points[i][1];

                x2 = points[j][0];
                y2 = points[j][1];

                d = (float)Math.sqrt(((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1)));

                if(d < min){
                    min = d;
                }
            }


        }

        return  min;

    }

    public static void main(String [] args) {
        Scanner input = new Scanner(System.in);

        int N = input.nextInt();

        float points[][] = new float[N][2];  // x i y 2 tocki

        for(int i=0;i<N;i++) {
            points[i][0] = input.nextFloat();
            points[i][1] = input.nextFloat();
        }

        System.out.printf("%.2f\n", minimalDistance(points));
    }
}