public class EgyptianPyramid {
    public static void main(String[] args) {
        
        int size = Integer.parseInt(args[0]);
        int bricks = Integer.parseInt(args[1]);

        char[][] pyramid = new char [size][size];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                pyramid[i][j] = '=';
            }
        }

        for (int row = size - 1; row >= 0; row--){
            int emptySpace = size - row - 1; //num of empty spaces on both sides
            int numBricksinRow = size - 2 * emptySpace;
            if (numBricksinRow > 0 && bricks > 0){
                for (int col = emptySpace; col < size - emptySpace; col++){
                    if (bricks > 0){
                        pyramid[row][col] = 'X';
                        bricks--;
                    } else {
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                System.out.print(pyramid[i][j]);
            }
            System.out.println();
            
        }

        System.out.println(bricks + " Bricks Remaining");

    }
}
