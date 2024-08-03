package com.Game;

import java.util.ArrayList;
import java.util.Random;

public class qAI {

    QuantumBoard board;
    QuantumBoard backup;
    int bestMove1;
    int bestMove2;
    int bestScore = Integer.MIN_VALUE;
    int movetotal;
    int total;
    int time = 250000000;

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

        //System.out.println("Best collapse is " + bestMove1 + " with a score of " + bestScore + " out of " + total / 2 + " (" + total + " branches checked)");
        board.collapseTile(bestMove1, board.getMoveCount() - 1);

        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 3);
    }

    public int checkQuantumBoard(QuantumBoard board){

        this.board = new QuantumBoard();
        this.backup = new QuantumBoard();

        this.board.copy(board);
        this.backup.copy(board);
        startList = listAvailableMoves();
        long start = System.nanoTime();

        //time I give it
        start += time;

        //System.out.println("Checking " + startList.size() + " available moves");

        while(System.nanoTime() < start){
            for(int i = 0; i < startList.size(); i++){
                board.copy(backup);

                //move count is now incremented inside move2
                //System.out.println("Playing starting move of " + startList.get(i).getMove1() + " " + startList.get(i).getMove2() + " on turn " + board.getMoveCount());
                board.move(startList.get(i));
                exploreRandom(board, startList.get(i));
                total++;
                //System.out.println("iterated");
            }
        }

        for(int i = 0; i < startList.size(); i++){
            if(startList.get(i).getWins() > bestScore){
                bestMove1 = startList.get(i).getMove1();
                bestMove2 = startList.get(i).getMove2();
                bestScore = startList.get(i).getWins();
                movetotal = startList.get(i).getTotal();
            }
        }

        board.copy(backup);

        //System.out.println("Best move is " + bestMove1 + ", " + bestMove2 + " with a score of " + bestScore + " out of " + movetotal + " (" + total + " branches checked)");
        board.move(new QuantumMove(bestMove1, bestMove2));

        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 3);
    }

    public void exploreRandom(QuantumBoard board, QuantumMove start){
        while(true){
            ArrayList<Integer> options = board.listActiveTiles();
            if(options.size() <= 1){
                start.addTotal(1);
                //System.out.println("Added stalemate");
                break;
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

            //System.out.println("Playing random moves " + move1 + " and " + move2);

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
            if(board.getBoardTile(i % 3, (int) Math.floor(i / 3)).getState() != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 9; j++){
                if(board.getBoardTile(j % 3, (int) Math.floor(j / 3)).getState() != State.Blank){
                    continue;
                }
                list.add(new QuantumMove(i, j));
            }
        }

        return list;
    }

}
