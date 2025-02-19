package com.Game;

import java.util.ArrayList;

public class QuantumBoard extends Board{

    private int result = 0;
    private int moveCount = 1;

    //I'm trusting past self that splitting these up is useful
    private ArrayList<Move>[] quantumCacheList;
    private State[] boardState;
    private int[] boardTurn;
    private ArrayList<Move> quantumSkipList;

    @SuppressWarnings("unchecked")
    public QuantumBoard(){
        boardState = new State[9];
        boardTurn = new int[9];
        quantumSkipList = new ArrayList<>();
        quantumCacheList = new ArrayList[9];
        for(int i = 0; i < 9; i++){
            quantumCacheList[i] = new ArrayList<>();
            boardTurn[i] = 0;
            boardState[i] = State.Blank;
        }

        state = State.Blank;
    }

    public QuantumBoard(QuantumBoard3D board, int slice){
        // boardArray = new QuantumTile[3][3];
        // skippedList = new ArrayList<>();
        // quantumCacheList = new ArrayList[27];

        for(int i = 0; i < 9; i++){
            quantumCacheList[i] = new ArrayList<>(board.getQuantumCacheList()[i + (9 * slice)]);
            boardState[i] = board.getBoardStatelist()[i + (9 * slice)];
        }
    }
    
    public int checkLoops(Move move){

        int start = move.loc;
        //printTile(start % 3, start / 3);
        //System.out.println(turn);
        int next = move.loc2;

        result = 0;

        //System.out.println("\nStarting location: Square " + start);

        quantumSkipList.add(move);
        iterativeSearch(start, next);

        quantumSkipList.clear();

        return result;
    }

    public void iterativeSearch(int start, int loc){

        if(loc == start){
            result = 1;
            //System.out.println("Loop found");
        }

        if(result == 1){
            return;
        }

        mainLoop:
        for(int i = 0; i < quantumCacheList[loc].size(); i++){

            //checks for skip list
            for(int j = 0; j < quantumSkipList.size(); j++){
                if(quantumSkipList.get(j) == quantumCacheList[loc].get(i)){
                    //System.out.println("Skipping move " + skippedList.get(j));
                    continue mainLoop;
                }
            }

            //does the stuff otherwise
            quantumSkipList.add(quantumCacheList[loc].get(i));
            if(quantumCacheList[loc].get(i).loc == loc){
                iterativeSearch(start, quantumCacheList[loc].get(i).loc2);
            }else{
                iterativeSearch(start, quantumCacheList[loc].get(i).loc);
            }
        }

    }

    public void incrementMoveCount(){
        moveCount++;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        for(int i = 0; i < 3; i++){
            //I'm just copying this from quantumboard3d, hopefully this works
            //horizontal
            if(boardState[i * 3].equals(boardState[i * 3 + 1]) && boardState[i * 3].equals(boardState[i * 3 + 2])){
                if(boardState[i * 3].equals(State.Player1)){
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            //vertical
            if(boardState[i].equals(boardState[i + 3]) && boardState[i].equals(boardState[i + 6])){
                if(boardState[i].equals(State.Player1)){
                    player1++;
                    int highest = Math.max(boardTurn[i], Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    player2++;
                    int highest = Math.max(boardTurn[i], Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //diagonals
        if(boardState[0].equals(boardState[4]) && boardState[0].equals(boardState[8])){
            if(boardState[4].equals(State.Player1)){
                player1++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }
            }else if(boardState[4].equals(State.Player2)){
                player2++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[2].equals(boardState[4]) && boardState[2].equals(boardState[6])){
            if(boardState[4].equals(State.Player1)){
                player1++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }
            }else if(boardState[4].equals(State.Player2)){
                player2++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
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

    public int getHighestMove(QuantumMove first, QuantumMove second, QuantumMove third){
        int move1 = first.getTurn();
        int move2 = second.getTurn();
        int move3 = third.getTurn();

        return Math.max(move1, Math.max(move2, move3));
    }

    // public int getMoveLocationInArray(int tile, int turn){
    //     int result = 0;

    //     for(int i = 0; i < boardArray[tile % 3][(int) Math.floor(tile / 3)].getMovesList().size(); i++){
    //         if(boardArray[tile % 3][(int) Math.floor(tile / 3)].getMovesList().get(i) == turn){
    //             result = i;
    //         }
    //     }

    //     return result;
    // }

    public void collapseTile(int loc, int turn){

        if(boardState[loc] != State.Blank){
            System.out.println("Cannot collape tile " + loc + ", already occupied");
            return;
        }

        boardTurn[loc] = turn;

        if(turn % 2 != 0){
            boardState[loc] =  State.Player1;
        }else{
            boardState[loc] =  State.Player2;
        }

        //available -= (Math.pow(2, loc));

        //does the chain reaction to collapse tiles
        for(int i = 0; i < quantumCacheList[loc].size(); i++){
            int m1 = quantumCacheList[loc].get(i).loc;
            int m2 = quantumCacheList[loc].get(i).loc2;
            int t = quantumCacheList[loc].get(i).turn;

            //I believe the blank check is to stop the chain reaction from going on forever
            if(m1 == loc){
                if(boardState[m2].equals(State.Blank)){
                    collapseTile(m2, t);
                }
            }else{
                if(boardState[m1].equals(State.Blank)){
                    collapseTile(m1, t);
                }
            }
        }

    }

    public void uncollapseTile(int loc){

        if(boardState[loc] == State.Blank){
            return;
        }

        boardTurn[loc] = 0;

        boardState[loc] = State.Blank;

        //available += (Math.pow(2, loc));

        for(int i = 0; i < quantumCacheList[loc].size(); i++){
            int m1 = quantumCacheList[loc].get(i).loc;
            int m2 = quantumCacheList[loc].get(i).loc2;

            if(m1 == loc){
                if(!boardState[m2].equals(State.Blank)){
                    uncollapseTile(m2);
                }
            }else{
                if(!boardState[m1].equals(State.Blank)){
                    uncollapseTile(m1);
                }
            }
        }

    }

    // public void checkBrokenLinks(){
    //     int linksResolved = 0;

    //     for(int i = 0; i < 3; i++){
    //         for(int j = 0; j < 3; j++){

    //             //iterates through moves on each square
    //             for(int k = 0; k < boardArray[i][j].getMovesList().size(); k++){

    //                 int loc = (i + (3 * j));

    //                 int move = boardArray[i][j].getMovesList().get(k);

    //                 int link = getLink(move, loc);
                    
    //                 if(link == -1){
    //                     //System.out.println("No link for tile " + loc + ", move " + move);
    //                     collapseTile(loc, move);
    //                     linksResolved++;
    //                 }
    //             }
    //         }
    //     }
    //     if(linksResolved > 0){
    //         checkBrokenLinks();
    //     }
    // }
    
    public int getMoveCount(){
        return moveCount;
    }

    public int getTurn(){
        return moveCount;
    }

    public void move(Move move){
        quantumCacheList[move.loc].add(move);
        quantumCacheList[move.loc2].add(move);
        moveCount++;
        move.print();
        //incrementMoveCount();
    }

    public void unmove(Move move){
        boolean one = quantumCacheList[move.loc].remove(move);
        boolean two = quantumCacheList[move.loc2].remove(move);

        if(!(one || two)){
            System.out.println("Failed to remove a move object (move not found)");
        }
        moveCount--;

        uncollapseTile(move.loc);
    }

    // @SuppressWarnings("unused")
    // public void copy(QuantumBoard board){

    //     //copies the simple variables
    //     moveCount = board.getMoveCount();
    //     //available = board.getAvailableInt();
    //     result = 0;

    //     //copies the quantum cache for each tile. This cache consists of quantumMove objects
    //     for(int i = 0; i < 9; i++){
    //         quantumCacheList[i].clear();
    //         for(int j = 0; j < board.getQuantumCacheList()[i].size(); j++){
    //             Move loadMove = board.getQuantumCacheList()[i].get(j);
    //             quantumCacheList[i].add(loadMove);
    //         }
    //         boardState[i] = board.getBoardStatelist()[i];
    //         boardTurn[i] = board.getBoardTurnlist()[i];
    //     }
    // }

    public State[] getBoardStatelist(){
        return boardState;
    }

    public int[] getBoardTurnlist(){
        return boardTurn;
    }

    // public ArrayList<Integer> listActiveTiles(){
    //     ArrayList<Integer> list = new ArrayList<>();

    //     for(int i = 0; i < 3; i++){
    //         for(int j = 0; j < 3; j++){
    //             if(boardArray[i][j].getState() == State.Blank){
    //                 list.add(i + (j * 3));
    //             }
    //         }
    //     }

    //     return list;
    // }

    public ArrayList<Move> getAvailable(){
        ArrayList<Move> list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            if(boardState[i] != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 9; j++){
                if(boardState[j]  != State.Blank){
                    continue;
                }

                //This should NOT be needed, but here we are
                if(i == j){
                    continue;
                }

                Move move = new Move(i, j);
                move.setTurn(getMoveCount());
                list.add(move);
            }
        }

        return list;
    }

    // public void clear(){
    //     for(int i = 0; i < 9; i++){
    //         if(boardArray[i % 3][i / 3].getState() != State.Blank){
    //             boardArray[i % 3][i / 3].getMovesList().clear();
    //         }
    //     }
    // }

    //What is this
    public QuantumMove checkRows(boolean turn){
        QuantumMove move = new QuantumMove(-1, -1);

        return move;
    }

    public ArrayList<Move>[] getQuantumCacheList(){
        return quantumCacheList;
    }
}


