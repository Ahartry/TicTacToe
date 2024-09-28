package com.Game;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class QuantumBoard3D {

    private QuantumTile[][][] boardArray;
    private int result = 0;
    private int moveCount = 1;
    private ArrayList<Integer> skippedList;
    private ArrayList<QuantumMove>[] quantumCacheList;
    private ArrayList<QuantumMove> quantumSkipList;
    private ArrayList<Integer> list;

    //stores the availability of each tile as a byte
    private int available = Integer.MAX_VALUE;

    @SuppressWarnings("unchecked")
    public QuantumBoard3D(){
        boardArray = new QuantumTile[3][3][3];
        skippedList = new ArrayList<>();
        quantumSkipList = new ArrayList<>();
        list = new ArrayList<>();
        quantumCacheList = new ArrayList[27];
        for(int i = 0; i < 27; i++){
            quantumCacheList[i] = new ArrayList<>();
        }
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

    public int checkLoopsUsingQuantumDoohickery(QuantumMove move){
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

    public int checkLoops(int turn){

        int start = getBoardWithMove(turn);
        int next = getLink(turn, start);

        result = 0;

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
            //System.out.println("Loop found");
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

        if(boardResult == -1){
            //System.out.println("ERROR: Move " + turn + " in square " + start + " has no link");
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

    //not used
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

            //x slices vertically, makes x from side
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

            //y slices vertically, makes x from other side
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

            //z crisscross, makes x from top down
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
        if(boardArray[0][0][2].getState().equals(boardArray[1][1][1].getState()) && boardArray[0][0][2].getState().equals(boardArray[2][2][0].getState())){
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

        for(int i = 0; i < boardArray[tile % 3][(tile % 9) / 3][tile / 9].getMovesList().size(); i++){
            if(boardArray[tile % 3][(tile % 9) / 3][tile / 9].getMovesList().get(i) == turn){
                result = i;
            }
        }

        return result;
    }

    public void collapseTile(int loc, int turn){

        int z = loc / 9;
        int x = loc % 3;
        int y = (loc - (z * 9)) / 3;

        if(boardArray[x][y][z].getState() != State.Blank){
            return;
        }

        if(turn % 2 != 0){
            boardArray[x][y][z].setState(State.Player1);
        }else{
            boardArray[x][y][z].setState(State.Player2);
        }

        boardArray[x][y][z].setTurn(turn);
        boardArray[x][y][z].getMovesList().clear();
        available -= (Math.pow(2, loc));

        //doesn't work currently
        for(int i = 0; i < quantumCacheList[loc].size(); i++){
            int m1 = quantumCacheList[loc].get(i).getMove1();
            int m2 = quantumCacheList[loc].get(i).getMove2();
            int t = quantumCacheList[loc].get(i).getTurn();
            quantumCacheList[loc].remove(i);
            i--;
            if(m1 == loc){
                collapseTile(m2, t);
            }else{
                collapseTile(m1, t);
            }
        }
        // list.clear();
        // for(int i = 0; i < boardArray[x][y][z].getMovesList().size(); i++){
        //     list.add(boardArray[x][y][z].getMovesList().get(i));
        // }

        // boardArray[x][y][z].getMovesList().clear();

        // //System.out.println(list.size());

        // for(int i = 0; i < list.size(); i++){
        //     int link = getLink(list.get(i), loc);
        //     if(link == -1){
        //         continue;
        //     }
        //     collapseTile(link, list.get(i));
        // }

    }

    public int checkAndCollapse(QuantumMove move){
        int result = checkLoopsUsingQuantumDoohickery(move);
        if(result != 0){
            SplittableRandom r = new SplittableRandom();
            if(r.nextBoolean()){
                collapseTile(move.getMove1(), getMoveCount());
            }else{
                collapseTile(move.getMove2(), getMoveCount());
            }
            //System.out.println("Found loop when checking " + move.getMove1() + " " + move.getMove2());
        }
        result = checkEntireBoard();
        if(result == 1){
            move.addWin(-1);
            move.addTotal(1);
            //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
        }else if(result == 2){
            move.addWin(1);
            move.addTotal(1);
            //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
        }
        return result;
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

    @SuppressWarnings("unused")
    public void copy(QuantumBoard3D board){
        //this might work (idk)
        long t0 = System.nanoTime();
        moveCount = board.getMoveCount();
        //availableSet.clear();
        available = board.getAvailable();
        long t1 = System.nanoTime();
        for(int i = 0; i < 27; i++){
            this.quantumCacheList[i].clear();
            if(board.getQuantumCacheList()[i].size() != 0){
                this.quantumCacheList[i] = board.getQuantumCacheList()[i];
            }
        }
        long t2 = System.nanoTime();
        result = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    boardArray[i][j][k].getMovesList().clear();
                    boardArray[i][j][k].copy(board.getBoardTile(i, j, k));
                }
            }
        }
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
        int z1 = move.getMove1() / 9;
        int x1 = move.getMove1() % 3;
        int y1 = (move.getMove1() % 9) / 3;

        int z2 = move.getMove2() / 9;
        int x2 = move.getMove2() % 3;
        int y2 = (move.getMove2() % 9) / 3;

        getBoardTile(x1, y1, z1).addMove(moveCount);
        getBoardTile(x2, y2, z2).addMove(moveCount);
        move.setTurn(moveCount);
        quantumCacheList[move.getMove1()].add(move);
        quantumCacheList[move.getMove2()].add(move);
        incrementMoveCount();
    }

    public ArrayList<Integer> listActiveTiles(){
        ArrayList<Integer> list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    if(boardArray[i][j][k].getState() == State.Blank){
                        list.add(i + (j * 3) + (k * 9));
                    }
                }

            }
        }

        return list;
    }

}


