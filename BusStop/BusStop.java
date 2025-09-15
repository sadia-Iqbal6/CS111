public class BusStop{
    public static void main(String[] args) {
        
        char[] busRoutes = new char[args.length-1];
        for(int i = 0; i < args.length - 1; i++){
            busRoutes[i] = args[i].charAt(0);
        }
        char serenasBus = args[args.length - 1].charAt(0);
        boolean busFound = false;

        for (int i = 0; i < busRoutes.length; i++){
            if (busRoutes[i] == serenasBus){
                System.out.println(i + 1);
                busFound = true;
                break;
            } 
        }
        if (!busFound){
            System.out.println("-1");
        }

    }
}