package com.Game;

import java.util.ArrayList;
import java.util.Random;

public class qAI {

    QuantumBoard board;
    QuantumBoard backup;
    int bestMove1;
    int bestMove2;
    int bestScore = Integer.MIN_VALUE;
    int bestEnemyScore = Integer.MAX_VALUE;
    int movetotal;
    int total;
    int time = 50000000;

    ArrayList<QuantumMove> startList;

    public qAI(){

    }

    public void setDifficulty(int x){
        time *= x;
    }

    public int checkCollapse(QuantumBoard board, int option1, int option2){
        this.board = new QuantumBoard();
        this.backup = new QuantumBoard();

        this.board.copy(board);
        this.backup.copy(board);
        long start = System.nanoTime();

        //System.out.println("Starting at " + start);

        //time I give it
        start += time;

        //System.out.println("Will end at " + start);

        board.collapseTile(option1, board.getMoveCount() - 1);
        startList = listAvailableMoves();
        board.copy(backup);

        //System.out.println(option1 + " " + option2);

        QuantumMove one = new QuantumMove(option1);
        QuantumMove two = new QuantumMove(option2);

        while(System.nanoTime() < start){
            board.copy(backup);
            board.collapseTile(option1, board.getMoveCount() - 1);
            exploreRandom(board, one);
            total++;

            board.copy(backup);
            board.collapseTile(option2, board.getMoveCount() - 1);
            exploreRandom(board, two);
            total++;

        }

        //System.out.println("Ended at " + System.nanoTime());

        if(one.getWins() > two.getWins()){
            bestScore = one.getWins();
            bestMove1 = option1;
        }else{
            bestScore = two.getWins();
            bestMove1 = option2;
        }

        board.copy(backup);

        System.out.println("Best collapse is " + bestMove1 + " with a score of " + bestScore + " out of " + total / 2 + " (" + total + " branches checked)");
        board.collapseTile(bestMove1, board.getMoveCount() - 1);

        movetotal = 0;
        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 3);
    }

    public int checkQuantumBoard(QuantumBoard RealBoard){

        board = new QuantumBoard();
        backup = new QuantumBoard();
        board.copy(RealBoard);
        backup.copy(RealBoard);

        startList = listAvailableMoves();
        long start = System.nanoTime();

        int empty = RealBoard.listActiveTiles().size();
        //number of combinations (moves)
        int timeThing = empty * (empty + 1) / 2;

        //time I give it
        //start += time;
        
        long timePerBranch = time / (timeThing * timeThing);

        //iterates through the ply 1 list
        for(int i = 0; i < startList.size(); i++){
            //resets the stuff and prepares for the next search
            backup.copy(RealBoard);
            backup.move(startList.get(i));
            ArrayList<QuantumMove> nextList = new ArrayList<>(listAvailableMoves(backup));
            bestEnemyScore = Integer.MAX_VALUE;

            //iterates through the ply 2 list
            for(int j = 0; j < nextList.size(); j++){
                //repeatedly searches from this point
                while(System.nanoTime() < (start + timePerBranch)){
                    board.copy(backup);
                    board.move(nextList.get(j));
                    exploreRandom(board, nextList.get(j));
                    total++;
                }
                if(nextList.get(j).getWins() < bestEnemyScore){
                    bestEnemyScore = nextList.get(j).getWins();
                }
                start += timePerBranch;

            }
            startList.get(i).addWin(bestEnemyScore);

        }

        for(int i = 0; i < startList.size(); i++){
            if(startList.get(i).getWins() > bestScore){
                bestMove1 = startList.get(i).getMove1();
                bestMove2 = startList.get(i).getMove2();
                bestScore = startList.get(i).getWins();
                movetotal = startList.get(i).getTotal();
            }
        }

        System.out.println("Best move is " + bestMove1 + ", " + bestMove2 + " with a score of " + bestScore + " out of " + movetotal + " (" + total + " branches checked)");
        RealBoard.move(new QuantumMove(bestMove1, bestMove2));

        movetotal = 0;
        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 9);
    }

    public void exploreRandom(QuantumBoard board, QuantumMove start){
        while(true){
            ArrayList<Integer> options = board.listActiveTiles();
            if(options.size() == 1){
                start.addTotal(1);
                //System.out.println("Added stalemate");
                break;
            }else if(options.size() == 0){
                int result = board.checkEntireBoard();
                if(result == 1){
                    start.addWin(-1);
                    start.addTotal(1);
                    //System.out.println("Added p1 victory");
                    break;
                }else if(result == 2){
                    start.addWin(1);
                    start.addTotal(1);
                    //System.out.println("Added p2 victory");
                    break;
                }else{
                    start.addTotal(1);
                    break;
                }
            }
            Random r = new Random();
            int move1 = r.nextInt(0, options.size());
            int move2;

            //gets two different random numbers
            while(true){
                move2 = r.nextInt(0, options.size());
                if(move2 != move1){
                    break;
                }
            }

            //gets the actual option
            move1 = options.get(move1);
            move2 = options.get(move2);

            board.move(new QuantumMove(move1, move2));
            if(board.checkLoops(board.getMoveCount() - 1) == 1){
                if(r.nextBoolean()){
                    board.collapseTile(move1, board.getMoveCount());
                }else{
                    board.collapseTile(move2, board.getMoveCount());
                }
                int result = board.checkEntireBoard();
                if(result == 1){
                    start.addWin(-1);
                    start.addTotal(1);
                    //System.out.println("Added p1 victory");
                    break;
                }else if(result == 2){
                    start.addWin(1);
                    start.addTotal(1);
                    //System.out.println("Added p2 victory");
                    break;
                }
            }
        }
    }

    public ArrayList<QuantumMove> listAvailableMoves(){
        ArrayList<QuantumMove> list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            if(board.getBoardTile(i % 3, i / 3).getState() != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 9; j++){
                if(board.getBoardTile(j % 3, j / 3).getState() != State.Blank){
                    continue;
                }
                list.add(new QuantumMove(i, j));
            }
        }

        return list;
    }

    public ArrayList<QuantumMove> listAvailableMoves(QuantumBoard board){
        ArrayList<QuantumMove> list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            if(board.getBoardTile(i % 3, i / 3).getState() != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 9; j++){
                if(board.getBoardTile(j % 3, j / 3).getState() != State.Blank){
                    continue;
                }
                list.add(new QuantumMove(i, j));
            }
        }

        return list;
    }

}
