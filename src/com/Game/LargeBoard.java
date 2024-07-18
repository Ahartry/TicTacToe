package com.Game;

import java.util.ArrayList;

public class LargeBoard {

    private SimpleBoard[][] boardArray;
    private boolean active = false;
    private ArrayList<LargeLocation> locationList = new ArrayList<>();

    public LargeBoard(SimpleBoard[][] inputBoard){

        boardArray = new SimpleBoard[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new SimpleBoard();
                boardArray[i][j].setState(inputBoard[i][j].getState());
                boardArray[i][j].setActive(inputBoard[i][j].getActive());
            }
        }
        state = State.Blank;
    }

    public LargeBoard(){
        boardArray = new SimpleBoard[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new SimpleBoard();
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

    public boolean getActive(){
        return active;
    }

    public SimpleBoard getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public SimpleBoard getBoardTile(int x, int y){
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

    public int checkEntireBoard(){
        int result = 0;

        //verticals
        for(int i = 0; i < 3; i++){
            if(boardArray[i][0].getState().equals(boardArray[i][1].getState()) && (boardArray[i][0].getState().equals(boardArray[i][2].getState()))){
                if(boardArray[i][0].getState().equals(State.Player1)){
                    result = 1;
                }else if(boardArray[i][0].getState().equals(State.Player2)){
                    result = 2;
                }
            }
        }

        //horizontals
        for(int i = 0; i < 3; i++){
            if(boardArray[0][i].getState().equals(boardArray[1][i].getState()) && (boardArray[0][i].getState().equals(boardArray[2][i].getState()))){
                if(boardArray[0][i].getState().equals(State.Player1)){
                    result = 1;
                }else if(boardArray[0][i].getState().equals(State.Player2)){
                    result = 2;
                }
            }
        }

        //diagonals
        if(boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                result = 1;
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                result = 2;
            }
        }
        if(boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                result = 1;
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                result = 2;
            }
        }
        
        return result;
    }

    public ArrayList<Integer> listActiveBoards(){
        ArrayList<Integer> activeList = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            int x1 = i % 3;
            int y1 = (int) Math.floor(i / 3);

            if(getBoardArray(x1, y1).getActive()){
                activeList.add(i);
            }
        }

        return activeList;
    }

    public ArrayList<Integer> listActiveCells(){
        ArrayList<Integer> activeList = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            int x1 = i % 3;
            int y1 = (int) Math.floor(i / 3);

            if(getBoardArray(x1, y1).getActive()){
                for(int j = 0; j < 9; j++){
                    int x2 = j % 3;
                    int y2 = (int) Math.floor(j / 3);
                    if(getBoardArray(x1, y1).getBoardTile(x2, y2).getState() == State.Blank){
                        activeList.add((i * 10) + j);
                        //System.out.println("Adding active tile: " + ((i * 10) + j));
                    }
                }
                
            }
        }

        return activeList;
    }

    public void move(int move, boolean player){
        //player true is player two
        //huh wait that rhymes, good mnemonic

        //silly preamble
        int board = (int) Math.floor(move / 10);
        int boardX = board % 3;
        int boardY = (int) Math.floor(board / 3);
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY = (int) Math.floor(cell / 3);

        //System.out.println("Move " + move + ", board: " + boardX + ", " + boardY + ", cell: " + cellX + ", " + cellY + " (Cell: " + cell + ")");

        getBoardArray(boardX, boardY).setBoardTile(cellX, cellY, player);
        add(boardX, boardY, cellX, cellY, player);

        //does stuff in case of victory
        int result = getBoardArray(boardX, boardY).checkBoard(cellX, cellY);
        if(result == 1){
            getBoardArray(boardX, boardY).setState(State.Player1);
            //clearLocationList(boardX, boardY);
        }else if(result == 2){
            getBoardArray(boardX, boardY).setState(State.Player2);
            //clearLocationList(boardX, boardY);
        }
        result = checkBoard(boardX, boardY);
        if(result == 1){
            setState(State.Player1);
            //clearLocationList(boardX, boardY);
        }else if(result == 2){
            setState(State.Player2);
            //clearLocationList(boardX, boardY);
        }
    }

    public void unmove(int move){
        //player true is player two
        //huh wait that rhymes, good mnemonic

        //silly preamble
        int board = (int) Math.floor(move / 10);
        int boardX = board % 3;
        int boardY = (int) Math.floor(board / 3);
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY = (int) Math.floor(cell / 3);

        int result = checkBoard(boardX, boardY);
        if(result != 0){
            //System.out.println("test");
            setState(State.Blank);
        }
        result = getBoardArray(boardX, boardY).checkBoard(cellX, cellY);
        if(result != 0){
            //System.out.println("test");
            getBoardArray(boardX, boardY).setState(State.Blank);
        }

        //System.out.println("unMove " + move + ", board: " + boardX + ", " + boardY + ", cell: " + cellX + ", " + cellY + " (Cell: " + cell + ")");

        getBoardArray(boardX, boardY).getBoardTile(cellX, cellY).setState(State.Blank);
        //remove(boardX, boardY, cellX, cellY);
        removeLast();

        //silly preamble
        // board = (int) Math.floor(previous / 10);
        // boardX = board % 3;
        // boardY = (int) Math.floor(board / 3);
        // cell = previous - (board * 10);
        // cellX = cell % 3;
        // cellY = (int) Math.floor(cell / 3);

        // calculateActive(cellX, cellY);
    }

    public void calculateActive(int xc, int yc){
        //clear active
        if(getBoardArray(xc, yc).getState() == State.Blank){
            setActive(false);
            getBoardArray(xc, yc).setActive(true);
        }else{
            setActive(true);
        }
    }

    public void calculateActive(int move){
        int board = (int) Math.floor(move / 10);
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY = (int) Math.floor(cell / 3);

        if(getBoardArray(cellX, cellY).getState() == State.Blank){
            setActive(false);
            getBoardArray(cellX, cellY).setActive(true);
        }else{
            setActive(true);
        }
    }

    public void add(int x1, int y1, int x2, int y2, boolean turn){
        locationList.add(new LargeLocation(x1, y1, x2, y2, turn));
    }

    public void remove(int x1, int y1, int x2, int y2){
        for(int i = 0; i < locationList.size(); i++){
            if(locationList.get(i).getBX() == x1 && locationList.get(i).getBY() == y1 && locationList.get(i).getCX() == x2 && locationList.get(i).getCY() == y2){
                locationList.get(i);
                break;
            }
        }
    }

    public void removeLast(){
        locationList.removeLast();
    }

    public LargeLocation get(int i){
        return locationList.get(i);
    }

    public void clearLocationList(int x, int y){
        //System.out.println("Clearing list for board " + x + ", " + y);
        for(int i = 0; i < locationList.size(); i++){
            if(get(i).getBX() == x && get(i).getBY() == y){
                locationList.remove(i);
                i--;
            }
        }
    }

    public int getLocSize(){
        return locationList.size();
    }

    // public void setActiveList(ArrayList<Integer> list){
    //     setActive(false);
    //     for(int i = 0; i < list.size(); i++){
    //         int board = (int) Math.floor(list.get(i) / 10);
    //         int boardX = board % 3;
    //         int boardY = (int) Math.floor(board / 3);
    //         int cell = list.get(i) - (board * 10);
    //         int cellX = cell % 3;
    //         int cellY = (int) Math.floor(cell / 3);
    //         getBoardArray(boardX, boardY).setActive(true);
    //     }
    // }
}