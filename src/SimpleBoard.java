public class SimpleBoard {

    private Tile[][] boardArray;
    private boolean active = false;

    public SimpleBoard(Tile[][] inputBoard){

        boardArray = new Tile[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new Tile();
                boardArray[i][j].setState(inputBoard[i][j].getState());
            }
        }
        state = State.Blank;
    }

    public SimpleBoard(){
        boardArray = new Tile[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new Tile();
                boardArray[i][j].setState(State.Blank);
            }
        }
        state = State.Blank;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setActive(boolean x){
        active = x;
        
    }

    public boolean getActive(){
        return active;
    }

    public Tile getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public int getMoveTally(){
        int blankTally = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j].getState() == State.Blank){
                    blankTally++;
                }
            }
        }
        return 9 - blankTally;
    }

    public Tile getBoardTile(int x, int y){
        return boardArray[x][y];
    }

    public int checkBoard(int x, int y){
        int result = 0;
        int player = 0; 

        if(boardArray[x][y].getState() == State.Player1){
            player = 1;
        }else if(boardArray[x][y].getState() == State.Player2){
            player = 2;
        }

        if(boardArray[x][0].getState().equals(boardArray[x][1].getState()) && boardArray[x][0].getState().equals(boardArray[x][2].getState())){
            result = player;
        }else if(boardArray[0][y].getState().equals(boardArray[1][y].getState()) && boardArray[0][y].getState().equals(boardArray[2][y].getState())){
            result = player;
        }else if(((boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState()))
             || (boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState()))) && boardArray[1][1].getState() != State.Blank){
            result = player;
            
        }

        
        return result;
    }
    
}
