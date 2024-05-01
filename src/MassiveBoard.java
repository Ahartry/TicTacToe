public class MassiveBoard {

    private LargeBoard[][] boardArray;
    private boolean active = false;

    public MassiveBoard (LargeBoard[][] inputBoard){

        boardArray = new LargeBoard[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new LargeBoard();
                boardArray[i][j].setState(inputBoard[i][j].getState());
                boardArray[i][j].setActive(inputBoard[i][j].getActive());
            }
        }
        state = State.Blank;
    }

    public MassiveBoard (){
        boardArray = new LargeBoard[3][3];
        System.out.println("Generating Massive Board");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new LargeBoard();
                boardArray[i][j].setState(State.Blank);
                boardArray[i][j].setActive(true);
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
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j].getState() == State.Blank){
                    boardArray[i][j].setActive(x);
                }else{
                    boardArray[i][j].setActive(false);
                }
            }
        }
    }

    public boolean getActive(){
        return active;
    }

    public LargeBoard getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public LargeBoard getBoardTile(int x, int y){
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

        //System.out.println(player);
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

    public void calculateActive(int xl, int yl, int xb, int yb, int xc, int yc){
        //clear active
        this.setActive(false);

        //check if last move was victory
        if(boardArray[xl][yl].getBoardArray(xb, yb).checkBoard(xc, yc) != 0){
            if(boardArray[xb][yb].getState() == State.Blank){
                boardArray[xb][yb].setActive(true);
            }else{
                this.setActive(true);
            }

        }else if(boardArray[xl][yl].getBoardArray(xc, yc).getState() == State.Blank){
            if(boardArray[xl][yl].getState() == State.Blank){
                boardArray[xl][yl].getBoardArray(xc, yc).setActive(true);
            }else{
                this.setActive(true);
            }
        }else{
            if(boardArray[xl][yl].getState() == State.Blank){
                boardArray[xl][yl].setActive(true);
            }else{
                this.setActive(true);
            }
        }
    }
    
}
