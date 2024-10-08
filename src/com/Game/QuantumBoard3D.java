package com.Game;

import java.util.ArrayList;

public class QuantumBoard3D {

    private int result = 0;
    private int moveCount = 1;
    private ArrayList<QuantumMove>[] quantumCacheList;
    private State[] boardState;
    private int[] boardTurn;
    private ArrayList<QuantumMove> quantumSkipList;

    //stores the availability of each tile as a byte
    private int available = Integer.MAX_VALUE;

    @SuppressWarnings("unchecked")
    public QuantumBoard3D(){
        boardState = new State[27];
        boardTurn = new int[27];
        quantumSkipList = new ArrayList<>();
        quantumCacheList = new ArrayList[27];
        for(int i = 0; i < 27; i++){
            quantumCacheList[i] = new ArrayList<>();
            boardTurn[i] = 0;
            boardState[i] = State.Blank;
        }

        state = State.Blank;
    }

    public int checkLoopsUsingQuantumDoohickery(QuantumMove move){
        result = 0;

        quantumSkipList.clear();
        quantumSkipList.add(move);

        int start = move.getMove1();
        int next = move.getMove2();

        doohickeryIterativeSearch(start, next);

        quantumSkipList.clear();

        return result;
    }

    public void doohickeryIterativeSearch(int start, int next){
        if(next == start){
            result = 1;
        }
        if(result == 1){
            return;
        }
        //System.out.println("Tile cache " + next + " has " + quantumCacheList[next].size() + " moves");
        mainloop:
        for(int i = 0; i < quantumCacheList[next].size(); i++){
            for(int j = 0; j < quantumSkipList.size(); j++){
                if(quantumSkipList.get(j) == quantumCacheList[next].get(i)){
                    continue mainloop;
                }
            }
            quantumSkipList.add(quantumCacheList[next].get(i));
            if(quantumCacheList[next].get(i).getMove1() == next){
                doohickeryIterativeSearch(start, quantumCacheList[next].get(i).getMove2());
            }else{
                doohickeryIterativeSearch(start, quantumCacheList[next].get(i).getMove1());
            }
        }

        return;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setBoardTurn(int x, int turn){
        boardTurn[x] = turn;
    }

    public State[] getBoardStatelist(){
        return boardState;
    }

    public int[] getBoardTurnlist(){
        return boardTurn;
    }


    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        for(int i = 0; i < 9; i++){
            //horizontal x
            if(boardState[i * 3].equals(boardState[i * 3 + 1]) && boardState[i * 3].equals(boardState[i * 3 + 2])){
                if(boardState[i * 3].equals(State.Player1)){
                    //System.out.println("Line 107");
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    //System.out.println("Line 115");
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        for(int i = 0; i < 3; i++){
            //horizontal y
            if(boardState[i].equals(boardState[i + 3]) && boardState[i].equals(boardState[i + 6])){
                if(boardState[i].equals(State.Player1)){
                    //System.out.println("Line 129");
                    player1++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    //System.out.println("Line 137");
                    player2++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            if(boardState[i + 9].equals(boardState[i + 12]) && boardState[i + 9].equals(boardState[i + 15])){
                if(boardState[i + 9].equals(State.Player1)){
                    //System.out.println("Line 147");
                    player1++;
                    int highest = Math.max(boardTurn[i + 9],  Math.max(boardTurn[i + 12], boardTurn[i + 15]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i + 9].equals(State.Player2)){
                    //System.out.println("Line 155");
                    player2++;
                    int highest = Math.max(boardTurn[i + 9],  Math.max(boardTurn[i + 12], boardTurn[i + 15]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            if(boardState[i + 18].equals(boardState[i + 21]) && boardState[i + 18].equals(boardState[i + 24])){
                if(boardState[i + 18].equals(State.Player1)){
                    //System.out.println("Line 165");
                    player1++;
                    int highest = Math.max(boardTurn[i + 18],  Math.max(boardTurn[i + 21], boardTurn[i + 24]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i + 18].equals(State.Player2)){
                    //System.out.println("Line 173");
                    player2++;
                    int highest = Math.max(boardTurn[i + 18],  Math.max(boardTurn[i + 21], boardTurn[i + 24]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        for(int i = 0; i < 9; i++){
            //horizontal z
            if(boardState[i].equals(boardState[i + 9]) && boardState[i].equals(boardState[i + 18])){
                if(boardState[i].equals(State.Player1)){
                    //System.out.println("Line 187");
                    player1++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 9], boardTurn[i + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    //System.out.println("Line 195");
                    player2++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 9], boardTurn[i + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //diagonals
        for(int i = 0; i < 3; i++){
            //x ascending
            if(boardState[i * 3].equals(boardState[(i * 3) + 10]) && boardState[i * 3].equals(boardState[(i * 3) + 20])){
                if(boardState[i * 3].equals(State.Player1)){
                    //System.out.println("Line 210");
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 20]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    //System.out.println("Line 218");
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 20]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //x descending
            if(boardState[(i * 3) + 2].equals(boardState[(i * 3) + 10]) && boardState[(i * 3) + 2].equals(boardState[(i * 3) + 18])){
                if(boardState[(i * 3) + 2].equals(State.Player1)){
                    //System.out.println("Line 230");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 3) + 2],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 18]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 3) + 2].equals(State.Player2)){
                    //System.out.println("Line 238");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 3) + 2],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            
            //z ascending
            if(boardState[(i * 9) + 6].equals(boardState[(i * 9) + 4]) && boardState[(i * 9) + 6].equals(boardState[(i * 9) + 2])){
                if(boardState[(i * 9) + 6].equals(State.Player1)){
                    //System.out.println("Line 250");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 9) + 6],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 9) + 6].equals(State.Player2)){
                    //System.out.println("Line 258");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 9) + 6],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //z descending
            if(boardState[(i * 9)].equals(boardState[(i * 9) + 4]) && boardState[(i * 9)].equals(boardState[(i * 9) + 8])){
                if(boardState[(i * 9)].equals(State.Player1)){
                    //System.out.println("Line 270");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 9)],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 8]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 9)].equals(State.Player2)){
                    //System.out.println("Line 278");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 9)],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 8]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //y ascending
            if(boardState[(i) + 6].equals(boardState[(i) + 12]) && boardState[i + 6].equals(boardState[(i) + 18])){
                if(boardState[(i) + 6].equals(State.Player1)){
                    //System.out.println("Line 290");
                    player1++;
                    int highest = Math.max(boardTurn[(i) + 6],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 18]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i) + 6].equals(State.Player2)){
                    //System.out.println("Line 298");
                    player2++;
                    int highest = Math.max(boardTurn[(i) + 6],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            
            //y descending
            if(boardState[(i)].equals(boardState[(i) + 12]) && boardState[i].equals(boardState[(i) + 24])){
                if(boardState[(i)].equals(State.Player1)){
                    //System.out.println("Line 310");
                    player1++;
                    int highest = Math.max(boardTurn[(i)],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 24]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i)].equals(State.Player2)){
                    //System.out.println("Line 318");
                    player2++;
                    int highest = Math.max(boardTurn[(i)],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 24]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //true diagonals
        if(boardState[0].equals(boardState[13]) && boardState[0].equals(boardState[26])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 331");
                player1++;
                int highest = Math.max(boardTurn[(0)],  Math.max(boardTurn[13], boardTurn[26]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 339");
                player2++;
                int highest = Math.max(boardTurn[(0)],  Math.max(boardTurn[13], boardTurn[26]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[18].equals(boardState[13]) && boardState[18].equals(boardState[8])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 349");
                player1++;
                int highest = Math.max(boardTurn[(18)],  Math.max(boardTurn[13], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 357");
                player2++;
                int highest = Math.max(boardTurn[(18)],  Math.max(boardTurn[13], boardTurn[8]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[6].equals(boardState[13]) && boardState[6].equals(boardState[20])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 367");
                player1++;
                int highest = Math.max(boardTurn[(6)],  Math.max(boardTurn[13], boardTurn[20]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 375");
                player2++;
                int highest = Math.max(boardTurn[(6)],  Math.max(boardTurn[13], boardTurn[20]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[2].equals(boardState[13]) && boardState[2].equals(boardState[24])){
            if(boardState[2].equals(State.Player1)){
                //System.out.println("Line 385");
                player1++;
                int highest = Math.max(boardTurn[(2)],  Math.max(boardTurn[13], boardTurn[24]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[2].equals(State.Player2)){
                //System.out.println("Line 393");
                player2++;
                int highest = Math.max(boardTurn[(2)],  Math.max(boardTurn[13], boardTurn[24]));
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

    public void collapseTile(int loc, int turn){

        if(boardState[loc] != State.Blank){
            return;
        }

        boardTurn[loc] = turn;

        if(turn % 2 != 0){
            boardState[loc] =  State.Player1;
        }else{
            boardState[loc] =  State.Player2;
        }

        available -= (Math.pow(2, loc));

        while(quantumCacheList[loc].size() != 0){
            int m1 = quantumCacheList[loc].get(0).getMove1();
            int m2 = quantumCacheList[loc].get(0).getMove2();
            int t = quantumCacheList[loc].get(0).getTurn();
            quantumCacheList[loc].remove(0);
            if(m1 == loc){
                collapseTile(m2, t);
            }else{
                collapseTile(m1, t);
            }
        }

    }

    public QuantumBoard getQuantumBoard(int slice){
        QuantumBoard board = new QuantumBoard(this, slice);
        return board;
    }

    public void incrementMoveCount(){
        moveCount++;
    }

    @SuppressWarnings("unused")
    public void copy(QuantumBoard3D board){
        long t0 = System.nanoTime();

        //copies the simple variables
        moveCount = board.getMoveCount();
        available = board.getAvailable();
        result = 0;
        long t1 = System.nanoTime();

        //board.print2();

        //copies the quantum cache for each tile. This cache consists of quantumMove objects
        for(int i = 0; i < 27; i++){
            quantumCacheList[i].clear();
            for(int j = 0; j < board.getQuantumCacheList()[i].size(); j++){
                QuantumMove loadMove = board.getQuantumCacheList()[i].get(j);
                quantumCacheList[i].add(loadMove);
            }
            boardState[i] = board.getBoardStatelist()[i];
            boardTurn[i] = board.getBoardTurnlist()[i];
        }

        //print2();

        long t3 = System.nanoTime();
        //System.out.println("copying quantum took: " + (t1 - t0) + " ns, copying cache took " + (t2 - t1) + ", and copying tiles took " + (t3 - t2));
    }

    public int getAvailable(){
        return available;
    }

    public ArrayList<QuantumMove>[] getQuantumCacheList(){
        return quantumCacheList;
    }

    public int getMoveCount(){
        return moveCount;
    }

    public void move(QuantumMove move){
        //move.setTurn(moveCount);
        quantumCacheList[move.getMove1()].add(move);
        quantumCacheList[move.getMove2()].add(move);
        moveCount++;
    }

    public ArrayList<Integer> listActiveTiles(){
        ArrayList<Integer> list = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            if(boardState[i] == State.Blank){
                list.add(i);
            }
        }

        return list;
    }

    public void print(){
        System.out.println("--------------");
        for(int z = 0; z < 3; z++){
            for(int y = 0; y < 3; y++){
                for(int x = 0; x < 3; x++){
                    System.out.print(quantumCacheList[x + (y * 3) + (z * 9)].size() + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
        System.out.println("End of board data");
    }

    public void print2(){
        System.out.println("--------------");
        System.out.print(quantumCacheList[0].size() + " " + quantumCacheList[1].size() + " " + quantumCacheList[2].size() + "   ");
        System.out.print(quantumCacheList[9].size() + " " + quantumCacheList[10].size() + " " + quantumCacheList[11].size() + "   ");
        System.out.print(quantumCacheList[18].size() + " " + quantumCacheList[19].size() + " " + quantumCacheList[20].size() + "   ");

        System.out.println();

        System.out.print(quantumCacheList[3].size() + " " + quantumCacheList[4].size() + " " + quantumCacheList[5].size() + "   ");
        System.out.print(quantumCacheList[12].size() + " " + quantumCacheList[13].size() + " " + quantumCacheList[14].size() + "   ");
        System.out.print(quantumCacheList[21].size() + " " + quantumCacheList[22].size() + " " + quantumCacheList[23].size() + "   ");

        System.out.println();

        System.out.print(quantumCacheList[6].size() + " " + quantumCacheList[7].size() + " " + quantumCacheList[8].size() + "   ");
        System.out.print(quantumCacheList[15].size() + " " + quantumCacheList[16].size() + " " + quantumCacheList[17].size() + "   ");
        System.out.print(quantumCacheList[24].size() + " " + quantumCacheList[25].size() + " " + quantumCacheList[26].size() + "   ");

        System.out.println("\n");
    }

}