package com.Game;

import java.util.ArrayList;

public class QuantumBoard3D {

    private QuantumTile[][][] boardArray;
    private int result = 0;
    private int moveCount = 1;
    private ArrayList<Integer> skippedList;

    public QuantumBoard3D(){
        boardArray = new QuantumTile[3][3][3];
        skippedList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    boardArray[i][j][k] = new QuantumTile();
                    boardArray[i][j][k].setState(State.Blank);
                }
            }
        }

        state = State.Blank;
    }

    public int checkLoops(int turn){

        int start = getBoardWithMove(turn);
        int next = getLink(turn, start);

        result = 0;

        //System.out.println("\nStarting location: Square " + start);

        skippedList.add(turn);
        iterativeSearch(start, next);

        skippedList.clear();

        return result;
    }

    public void iterativeSearch(int start, int loc){

        int z = loc / 9;

        int x = loc % 3;
        int y = ((loc - (z * 9)) / 3);

        if(loc == start){
            result = 1;
            System.out.println("Loop found");
        }

        if(result == 1){
            return;
        }

        mainLoop:
        for(int i = 0; i < boardArray[x][y][z].getMovesList().size(); i++){

            int move = boardArray[x][y][z].getMovesList().get(i);
            int link = getLink(move, (x + (y * 3) + (z * 9)));

            //checks for skip list
            for(int j = 0; j < skippedList.size(); j++){
                if(skippedList.get(j) == boardArray[x][y][z].getMovesList().get(i)){
                    //System.out.println("Skipping move " + skippedList.get(j));
                    continue mainLoop;
                }
            }

            //does the stuff otherwise
            skippedList.add(move);
            iterativeSearch(start, link);
        }

    }

    public int getLink(int turn, int start){
        int boardResult = -1;

        mainLoop:
        for(int i = 0; i < 27; i++){

            int z = i / 9;
            int x = i % 3;
            int y = ((i - (z * 9)) / 3);

            if(i != start){
                for(int j = 0; j < boardArray[x][y][z].getMovesList().size(); j++){
                    if(boardArray[x][y][z].getMovesList().get(j) == turn){
                        boardResult = i;
                        break mainLoop;
                    }
                }
            }

        }

        return boardResult;
    }

    public int getBoardWithMove(int turn){

        int boardResult = 0;
        mainLoop:
        for(int i = 0; i < 27; i++){

            int z = i / 9;
            int x = i % 3;
            int y = ((i - (z * 9)) / 3);

            for(int j = 0; j < boardArray[x][y][z].getMovesList().size(); j++){
                if(boardArray[x][y][z].getMovesList().get(j) == turn){
                    boardResult = i;
                    break mainLoop;
                }
            }
        }

        return boardResult;
    }

    public int getOtherBoardWithMove(int turn){

        int boardResult = 0;
        boolean first = false;
        mainLoop:
        for(int i = 0; i < 27; i++){

            int z = (int) Math.floor(i / 9);
            int x = i % 3;
            int y = (int) Math.floor((i - (z * 9)) / 3);

            for(int j = 0; j < boardArray[x][y][z].getMovesList().size(); j++){
                if(first && boardArray[x][y][z].getMovesList().get(j) == turn){
                    boardResult = i;
                    break mainLoop;
                }
                if(boardArray[x][y][z].getMovesList().get(j) == turn){
                    first = true;
                }
            }
        }

        return boardResult;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setBoardTile(int x, int y, int z, boolean player){
        if(player){
            boardArray[x][y][z].setState(State.Player2);
        }else{
            boardArray[x][y][z].setState(State.Player1);
        }
        
    }

    public QuantumTile getBoardTile(int x, int y, int z){
        return boardArray[x][y][z];
    }

    public int checkBoard(int x, int y, int z){
        int result = 0;
        int player = 0; 

        if(boardArray[x][y][z].getState() == State.Player1){
            player = 1;
        }else if(boardArray[x][y][z].getState() == State.Player2){
            player = 2;
        }

        //verticals
        if(boardArray[x][y][0].getState().equals(boardArray[x][y][1].getState()) && boardArray[x][y][0].getState().equals(boardArray[x][y][2].getState())){
            result = player;
        }

        //horizontal
        if(boardArray[x][0][z].getState().equals(boardArray[x][1][z].getState()) && boardArray[x][0][z].getState().equals(boardArray[x][2][z].getState())){
            result = player;
        }

        //the other
        if(boardArray[0][y][z].getState().equals(boardArray[1][y][z].getState()) && boardArray[0][y][z].getState().equals(boardArray[2][y][z].getState())){
            result = player;
        }

        //I HAVE NOT DONE DIAGONAlS
        //AND I DO NOT WANT TO DO THEM
        //FREE ME FROM THIS

        return result;
    }

    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        //horizontals z
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j][0].getState().equals(boardArray[i][j][1].getState()) && (boardArray[i][j][0].getState().equals(boardArray[i][j][2].getState()))){
                    if(boardArray[i][j][0].getState().equals(State.Player1)){
                        player1++;
                        int highest = getHighestMove(boardArray[i][j][0], boardArray[i][j][1], boardArray[i][j][0]);
                        if(highest > highestp1){
                            highestp1 = highest;
                        }
    
                    }else if(boardArray[i][j][0].getState().equals(State.Player2)){
                        player2++;
                        int highest = getHighestMove(boardArray[i][j][0], boardArray[i][j][1], boardArray[i][j][0]);
                        if(highest > highestp2){
                            highestp2 = highest;
                        }
                    }
                }
            }
        }

        //horizontals x
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[0][j][i].getState().equals(boardArray[1][j][i].getState()) && (boardArray[0][j][i].getState().equals(boardArray[2][j][i].getState()))){
                    if(boardArray[0][j][i].getState().equals(State.Player1)){
                        player1++;
                        int highest = getHighestMove(boardArray[0][j][i], boardArray[1][j][i], boardArray[2][j][i]);
                        if(highest > highestp1){
                            highestp1 = highest;
                        }
    
                    }else if(boardArray[0][j][i].getState().equals(State.Player2)){
                        player2++;
                        int highest = getHighestMove(boardArray[0][j][i], boardArray[1][j][i], boardArray[2][j][i]);
                        if(highest > highestp2){
                            highestp2 = highest;
                        }
                    }
                }
            }
        }

        //horizontals y
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[j][0][i].getState().equals(boardArray[j][1][i].getState()) && (boardArray[j][0][i].getState().equals(boardArray[j][2][i].getState()))){
                    if(boardArray[j][0][i].getState().equals(State.Player1)){
                        player1++;
                        int highest = getHighestMove(boardArray[j][0][i], boardArray[j][1][i], boardArray[j][2][i]);
                        if(highest > highestp1){
                            highestp1 = highest;
                        }
    
                    }else if(boardArray[j][0][i].getState().equals(State.Player2)){
                        player2++;
                        int highest = getHighestMove(boardArray[j][0][i], boardArray[j][1][i], boardArray[j][2][i]);
                        if(highest > highestp2){
                            highestp2 = highest;
                        }
                    }
                }
            }
        }

        //diagonals
        for(int i = 0; i < 3; i++){
            if(boardArray[i][0][0].getState().equals(boardArray[i][1][1].getState()) && boardArray[i][0][0].getState().equals(boardArray[i][2][2].getState())){
                if(boardArray[i][1][1].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[i][0][0], boardArray[i][1][1], boardArray[i][2][2]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[i][1][1].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[i][0][0], boardArray[i][1][1], boardArray[i][2][2]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
            if(boardArray[i][2][0].getState().equals(boardArray[i][1][1].getState()) && boardArray[i][2][0].getState().equals(boardArray[i][0][2].getState())){
                if(boardArray[i][1][1].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[i][2][0], boardArray[i][1][1], boardArray[i][0][2]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[i][1][1].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[i][2][0], boardArray[i][1][1], boardArray[i][0][2]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
            if(boardArray[0][i][0].getState().equals(boardArray[1][i][1].getState()) && boardArray[0][i][0].getState().equals(boardArray[2][i][2].getState())){
                if(boardArray[1][i][1].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[0][i][0], boardArray[1][i][1], boardArray[2][i][2]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[1][i][1].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[0][i][0], boardArray[1][i][1], boardArray[2][i][2]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
            if(boardArray[2][i][0].getState().equals(boardArray[1][i][1].getState()) && boardArray[2][i][0].getState().equals(boardArray[0][i][2].getState())){
                if(boardArray[1][i][1].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[2][i][0], boardArray[1][i][1], boardArray[0][i][2]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[1][i][1].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[2][i][0], boardArray[1][i][1], boardArray[0][i][2]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
            if(boardArray[0][0][i].getState().equals(boardArray[1][1][i].getState()) && boardArray[0][0][i].getState().equals(boardArray[2][2][i].getState())){
                if(boardArray[1][1][i].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[0][0][i], boardArray[1][1][i], boardArray[2][2][i]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[1][1][i].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[0][0][i], boardArray[1][1][i], boardArray[2][2][i]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
            if(boardArray[2][0][i].getState().equals(boardArray[1][1][i].getState()) && boardArray[2][0][i].getState().equals(boardArray[0][2][i].getState())){
                if(boardArray[1][1][i].getState().equals(State.Player1)){
                    player1++;
                    int highest = getHighestMove(boardArray[2][0][i], boardArray[1][1][i], boardArray[0][2][i]);
                    if(highest > highestp1){
                        highestp1 = highest;
                    }  
                }else if(boardArray[1][1][i].getState().equals(State.Player2)){
                    player2++;
                    int highest = getHighestMove(boardArray[2][0][i], boardArray[1][1][i], boardArray[0][2][i]);
                    if(highest > highestp2){
                        highestp2 = highest;
                    }  
                }
            }
        }
        
        //true diagonals
        if(boardArray[0][0][0].getState().equals(boardArray[1][1][1].getState()) && boardArray[0][0][0].getState().equals(boardArray[2][2][2].getState())){
            if(boardArray[1][1][1].getState().equals(State.Player1)){
                player1++;
                int highest = getHighestMove(boardArray[0][0][0], boardArray[1][1][1], boardArray[2][2][2]);
                if(highest > highestp1){
                    highestp1 = highest;
                }  
            }else if(boardArray[1][1][1].getState().equals(State.Player2)){
                player2++;
                int highest = getHighestMove(boardArray[0][0][0], boardArray[1][1][1], boardArray[2][2][2]);
                if(highest > highestp2){
                    highestp2 = highest;
                }  
            }
        }
        if(boardArray[0][0][2].getState().equals(boardArray[1][1][1].getState()) && boardArray[0][0][0].getState().equals(boardArray[2][2][0].getState())){
            if(boardArray[1][1][1].getState().equals(State.Player1)){
                player1++;
                int highest = getHighestMove(boardArray[0][0][2], boardArray[1][1][1], boardArray[2][2][0]);
                if(highest > highestp1){
                    highestp1 = highest;
                }  
            }else if(boardArray[1][1][1].getState().equals(State.Player2)){
                player2++;
                int highest = getHighestMove(boardArray[0][0][2], boardArray[1][1][1], boardArray[2][2][0]);
                if(highest > highestp2){
                    highestp2 = highest;
                }  
            }
        }
        if(boardArray[0][2][0].getState().equals(boardArray[1][1][1].getState()) && boardArray[0][2][0].getState().equals(boardArray[2][0][2].getState())){
            if(boardArray[1][1][1].getState().equals(State.Player1)){
                player1++;
                int highest = getHighestMove(boardArray[0][2][0], boardArray[1][1][1], boardArray[2][0][2]);
                if(highest > highestp1){
                    highestp1 = highest;
                }  
            }else if(boardArray[1][1][1].getState().equals(State.Player2)){
                player2++;
                int highest = getHighestMove(boardArray[0][2][0], boardArray[1][1][1], boardArray[2][0][2]);
                if(highest > highestp2){
                    highestp2 = highest;
                }  
            }
        }
        if(boardArray[2][0][0].getState().equals(boardArray[1][1][1].getState()) && boardArray[2][0][0].getState().equals(boardArray[0][2][2].getState())){
            if(boardArray[1][1][1].getState().equals(State.Player1)){
                player1++;
                int highest = getHighestMove(boardArray[2][0][0], boardArray[1][1][1], boardArray[0][2][2]);
                if(highest > highestp1){
                    highestp1 = highest;
                }  
            }else if(boardArray[1][1][1].getState().equals(State.Player2)){
                player2++;
                int highest = getHighestMove(boardArray[2][0][0], boardArray[1][1][1], boardArray[0][2][2]);
                if(highest > highestp2){
                    highestp2 = highest;
                }  
            }
        }

        //silly stuff with turn count and checking stuff
        if(player1 == player2 && player1 != 0){
            if(highestp1 < highestp2){
                result = 1;
            }else{
                result = 2;
            }
        }else if(player1 > player2){
            result = 1;
        }else if(player2 > player1){
            result = 2;
        }
        return result;
    }

    public int getHighestMove(int first, int second, int third){
        int move1 = boardArray[first % 3][(first - ((first / 9) * 9)) / 3][first / 9].getTurn();
        int move2 = boardArray[second % 3][(second - ((second / 9) * 9)) / 3][second / 9].getTurn();
        int move3 = boardArray[third % 3][(third - ((third / 9) * 9)) / 3][third / 9].getTurn();

        return Math.max(move1, Math.max(move2, move3));
    }

    public int getHighestMove(QuantumTile first, QuantumTile second, QuantumTile third){
        int move1 = first.getTurn();
        int move2 = second.getTurn();
        int move3 = third.getTurn();

        return Math.max(move1, Math.max(move2, move3));
    }

    public void printBoard(){
        System.out.println();

        for(int i = 0; i < 9; i++){
            int z = (int) Math.floor(i / 9);
            int x = i % 3;
            int y = (int) Math.floor((i - (z * 9)) / 3);

            for(int j = 0; j < boardArray[x][y][z].getMovesList().size(); j++){
                System.out.print(boardArray[x][y][z].getMovesList().get(j));
            }
            if(boardArray[x][y][z].getMovesList().size() < 3){
                for(int j = 0; j < (3 - boardArray[x][y][z].getMovesList().size()); j++){
                    System.out.print("0");
                }
            }
            System.out.print(" ");
        }
    }

    public void printTile(int x, int y, int z){
        System.out.println("\nMoves at " + x + ", " + y);
        for(int i = 0; i < boardArray[x][y][z].getMovesList().size(); i++){
            System.out.print(boardArray[x][y][z].getMovesList().get(i) + " ");
        }
    }

    public int getMoveLocationInArray(int tile, int turn){
        int result = 0;

        for(int i = 0; i < boardArray[tile % 3][(int) Math.floor((tile - (Math.floor(tile / 9) * 9)) / 3)][(int) Math.floor(tile / 9)].getMovesList().size(); i++){
            if(boardArray[tile % 3][(int) Math.floor((tile - (Math.floor(tile / 9) * 9)) / 3)][(int) Math.floor(tile / 9)].getMovesList().get(i) == turn){
                result = i;
            }
        }

        return result;
    }

    public void collapseTile(int loc, int turn){

        int z = loc / 9;
        int x = loc % 3;
        int y = (loc - (z * 9)) / 3;

        if(turn % 2 != 0){
            boardArray[x][y][z].setState(State.Player1);
        }else{
            boardArray[x][y][z].setState(State.Player2);
        }

        boardArray[x][y][z].setTurn(turn);

        boardArray[x][y][z].getMovesList().clear();

        checkBrokenLinks();

    }

    public void checkBrokenLinks(){
        int linksResolved = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int l = 0; l < 3; l++){

                    //iterates through moves on each square
                    for(int k = 0; k < boardArray[i][j][l].getMovesList().size(); k++){

                        int loc = (i + (3 * j) + (9 * l));

                        int move = boardArray[i][j][l].getMovesList().get(k);

                        int link = getLink(move, loc);
                        
                        if(link == -1){
                            //System.out.println("No link for tile " + loc + ", move " + move);
                            collapseTile(loc, move);
                            linksResolved++;
                        }
                    }
                }

            }
        }
        if(linksResolved > 0){
            checkBrokenLinks();
        }
    }
    
    public QuantumBoard getQuantumBoard(int slice){
        QuantumBoard board = new QuantumBoard(this, slice);
        return board;
    }

    public void clear(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i % 3][i / 3][j].getState() != State.Blank){
                    boardArray[i % 3][i / 3][j].getMovesList().clear();
                }
            }

        }
    }

    public void incrementMoveCount(){
        moveCount++;
    }

}


