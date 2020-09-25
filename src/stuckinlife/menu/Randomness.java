package stuckinlife.menu;

public class Randomness {

    public static void main(String[] args) {
        double d0 = 0;
        System.out.println((d0 = Math.nextUp(d0)));
        double d1 = 1;
        System.out.println(d1 = (Math.nextUp(d1) - 1));
        System.out.println(2D / (d0 + d1));

        //9.007199254740992E15
        System.out.println((1.1139E-4 * 9.007199254740992E15) / (1000 * 60 * 60));
        System.out.println(12 * 3600 * 1000);
        int iter = 1000000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {
            if (Math.random() == Math.random()) {
                System.out.println("skfj");
            }
        }
        System.out.println((System.currentTimeMillis() - time) / (iter * 1D));

    }
}
