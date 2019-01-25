package engine;

public class AllowedMovesDirections {
    public boolean[] direction = new boolean[8];

    public AllowedMovesDirections(){
        for (int i =0 ; i<8 ; i++){
            direction[i]=false;
        }
    }

    public void addDirection(int direction){
        this.direction[direction] = true;
    }

    public void printAllowedDirections(){
        System.out.print("mozliwe kierunki ruchu w tym punkcie: ");
        for (int p =0 ; p<8 ; p++){
            if(direction[p]){
                System.out.print(Integer.toString(p) + " ");
            }
        }
        System.out.print("\n");
    }

    public boolean isPossibleToBounce(){
        boolean response = false;
        for (int i =0 ; i<8 ; i++){
            if (direction[i] == false){
                response = true;
                break;
            }
        }
        return response;
    }

    public boolean amIStuck(){
        int count = 0;
        for (int i =0 ; i<8 ; i++){
            if (direction[i] == false){
                count++;
            }
        }
        return count != 8;
    }
}
