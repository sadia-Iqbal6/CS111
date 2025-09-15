public class floor {
    
    public static int ceiling (double number){
        double num = 0.0;
        double x = 0.0;
        while (number > 0){
            x = number % 10;
            if (x >= args.length){
                num = (int) x;
            }
            number /= 10;
        }
        return num;
    }

    public static void main(String[] args) {
        double k = Double.parseDouble(args[0]);
        System.out.println(ceiling(k));
    }
}
