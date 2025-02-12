package com.Game;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class qAI3D {

    QuantumBoard3D board;
    QuantumBoard3D backup;
    int bestMove1;
    int bestMove2;
    double bestScore = Integer.MIN_VALUE;
    double bestEnemyScore = Integer.MAX_VALUE;
    int movetotal = 0;
    int total = 0;
    int thistotal = 0;
    int time = 100000000;
    int heuristicTileCount = 0;
    Integer[] optionArray;

    ArrayList<QuantumMove> startList;

    public qAI3D(){
        optionArray = new Integer[27];
    }

    public void setDifficulty(int x){
        time *= x;
    }

    public int checkCollapse(QuantumBoard3D mboard, int option1, int option2){
        board = new QuantumBoard3D();

        board.copy(mboard);
        long start = System.nanoTime();

        //System.out.println("Starting at " + start);

        //time I give it
        start += time;

        QuantumMove one = new QuantumMove(option1);
        QuantumMove two = new QuantumMove(option2);

        while(System.nanoTime() < start){
            board.copy(mboard);
            board.collapseTile(option1, board.getMoveCount() - 1);
            int result = board.checkEntireBoard();
            if(result == 1){
                one.addWin(-1);
                one.addTotal(1);
            }else if(result == 2){
                one.addWin(1);
                one.addTotal(1);
            }else{
                exploreRandom(board, one);
            }
            total++;

            board.copy(mboard);
            board.collapseTile(option2, board.getMoveCount() - 1);
            result = board.checkEntireBoard();
            if(result == 1){
                two.addWin(-1);
                two.addTotal(1);
            }else if(result == 2){
                two.addWin(1);
                two.addTotal(1);
            }else{
                exploreRandom(board, two);
            }
            total++;

        }

        double otherScore = 0;
        //System.out.println("Ended at " + System.nanoTime());

        if(one.getWins() > two.getWins()){
            bestScore = (double) one.getWins() / (double) one.getTotal();
            otherScore = (double) two.getWins() / (double) two.getTotal();
            bestMove1 = option1;
        }else{
            bestScore = (double) two.getWins() / (double) two.getTotal();
            otherScore = (double) one.getWins() / (double) one.getTotal();
            bestMove1 = option2;
        }

        System.out.println("Best collapse is " + bestMove1 + " with a score of " + bestScore + " (" + total + " branches checked). Other choice was " + otherScore);
        //board.collapseTile(bestMove1, board.getMoveCount() - 1);
        //System.exit(0);

        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1;
    }

    @SuppressWarnings("unused")
    public QuantumMove checkQuantumBoard3D(QuantumBoard3D RealBoard){

        board = new QuantumBoard3D();
        backup = new QuantumBoard3D();
        //board.copy(RealBoard);
        backup.copy(RealBoard);

        movetotal = 0;

        startList = listAvailableMoves2(RealBoard);

        int empty = board.listActiveTiles().size();
        int timeThing2 = (empty - 1) * (empty) / 2;
        
        long timePerBranch = time / (startList.size() * timeThing2);
        //System.out.println("First move has " + startList.size() + " options");
        //timePerBranch *= 10;
        long start = System.nanoTime() + timePerBranch;

        //iterates through the ply 1 list
        loop1:
        for(int i = 0; i < startList.size(); i++){
            //resets the stuff and prepares for the next search
            backup.copy(RealBoard);
            backup.move(startList.get(i));
            ArrayList<QuantumMove> nextList = new ArrayList<>(listAvailableMoves(backup));
            bestEnemyScore = Integer.MAX_VALUE;
            int skiptally = 0;
            thistotal = 0;
            
            //this checks to see if the initial move is a win
            int prelimresult = checkAndCollapse(board, startList.get(i), startList.get(i));
            if(prelimresult == 1){
                //System.out.println("boo1");
                bestEnemyScore = -100;
                continue;
            }else if(prelimresult == 2){
                //System.out.println("boo1");
                break;
            }
            //iterates through the ply 2 list
            loop2:
            for(int j = 0; j < nextList.size(); j++){
                start += timePerBranch;

                //repeatedly searches from this point
                while(System.nanoTime() < (start + timePerBranch)){
                    long t0 = System.nanoTime();
                    //RealBoard.print2();
                    //backup.print2();
                    board.copy(backup);
                    long t1 = System.nanoTime();
                    //board.print2();
                    board.move(nextList.get(j));
                    //board.print2();

                    //this checks to see if the initial move is a win
                    int prelimresult2 = checkAndCollapse(board, nextList.get(j), nextList.get(j));
                    if(prelimresult2 == 1){
                        //System.out.println("boo2");
                        bestEnemyScore = -100;
                        thistotal = nextList.get(j).getTotal();
                        break loop2;
                    }else if(prelimresult2 == 2){
                        //System.out.println("boo2");
                        continue;
                    }
                    
                    long t2 = System.nanoTime();
                    exploreRandom(board, nextList.get(j));
                    long t3 = System.nanoTime();
                    //System.out.println("Resetting board took " + (t1 - t0) + " ns, moving took " + (t2 - t1) + ", and exploring took " + (t3 - t2));
                    total++;
                }
                if(nextList.get(j).getTotal() == 0){
                    skiptally++;
                    continue;
                }
                if(nextList.get(j).getScore() < bestEnemyScore){
                    bestEnemyScore = nextList.get(j).getScore();
                    //System.out.println("Score " + bestEnemyScore);
                    thistotal = nextList.get(j).getTotal();
                }

            }
            //make sure that it doesn't choose a move just because it hasn't been able to search it
            if(bestEnemyScore == Integer.MAX_VALUE){
                bestEnemyScore = Integer.MIN_VALUE;
            }
            //System.out.println("Skipped " + skiptally + " on that branch");
            //System.out.println(startList.get(i).getMove1() + " " + startList.get(i).getMove2() + " has a score of " + bestEnemyScore);
            startList.get(i).addWind(bestEnemyScore);
            startList.get(i).addTotal(thistotal);

        }

        QuantumMove choice = null;

        for(int i = 0; i < startList.size(); i++){
            if(startList.get(i).getWinsd() > bestScore){
                bestMove1 = startList.get(i).getMove1();
                bestMove2 = startList.get(i).getMove2();
                bestScore = startList.get(i).getWinsd();
                movetotal = startList.get(i).getTotal();
                choice = startList.get(i);
            }
        }

        System.out.println("Best move is " + bestMove1 + ", " + bestMove2 + " with a score of " + bestScore + " out of " + movetotal + " (" + total + " branches checked)");
        //QuantumMove move = new QuantumMove(bestMove1, bestMove2);
        //move.setTurn(RealBoard.getMoveCount());
        RealBoard.move(choice);

        movetotal = 0;
        total = 0;
        bestScore = Integer.MIN_VALUE;

        return choice;
    }

    @SuppressWarnings("unused")
    public void exploreRandom(QuantumBoard3D board, QuantumMove start){
        while(true){
            long t0 = System.nanoTime();
            int available = board.getAvailableInt();
            int moveoptions = 0;
            long t1 = System.nanoTime();
            for(int i = 0; i < 27; i++){
                //does fancy bit stuff, returns true if the ith digit is 1
                if(((available << ~i) < 0)){
                    //options.add(i);
                    optionArray[moveoptions] = i;
                    moveoptions++;
                }
            }
            long t2 = System.nanoTime();
            if(moveoptions == 1){
                start.addTotal(1);
                //System.out.println("Added stalemate");
                break;
            }else if(moveoptions == 0){
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
            SplittableRandom r = new SplittableRandom();
            int move1 = r.nextInt(0, moveoptions);
            int move2;

            //gets two different random numbers
            while(true){
                move2 = r.nextInt(0, moveoptions);
                if(move2 != move1){
                    break;
                }
            }

            //gets the actual option
            move1 = optionArray[move1];
            move2 = optionArray[move2];

            QuantumMove move = new QuantumMove(move1, move2);
            move.setTurn(board.getMoveCount());

            board.move(move);
            long t3 = System.nanoTime();
            checkAndCollapse(board, move, start);
            long t4 = System.nanoTime();
            //System.out.println("getting options took " + (t1 - t0) + " ns, loading them took " + (t2 - t1) + ", moving took " + (t3 - t2) + ", checking loop took " + (t4 - t3));
        }
    }

    public ArrayList<QuantumMove> listAvailableMoves(QuantumBoard3D board){
        ArrayList<QuantumMove> list = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            if(board.getBoardStatelist()[i] != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 27; j++){
                if(board.getBoardStatelist()[j] != State.Blank){
                    continue;
                }

                //This should NOT be needed, be here we are
                if(i == j){
                    continue;
                }
                QuantumMove move = new QuantumMove(i, j);
                move.setTurn(board.getMoveCount());
                list.add(move);
            }
        }

        return list;
    }

    public ArrayList<QuantumMove> listAvailableMoves2(QuantumBoard3D board){
        ArrayList<QuantumMove> list = new ArrayList<>();

        heuristicTileCount = 0;

        for(int i = 0; i < 27; i++){
            if(board.getBoardStatelist()[i] != State.Blank){
                continue;
            }
            if(board.getQuantumCacheList()[i].size() == 0){
                continue;
            }
            heuristicTileCount++;
            for(int j = 0; j < 27; j++){
                if(board.getBoardStatelist()[j] != State.Blank){
                    continue;
                }

                //This should NOT be needed, yet here we are
                if(i == j){
                    continue;
                }
                QuantumMove move = new QuantumMove(i, j);
                move.setTurn(board.getMoveCount());
                //System.out.println("Found move with turn " + board.getMoveCount());
                list.add(move);

            }
        }

        if(list.size() == 0){
            System.out.println("Heuristic thing returned no moves, using backup");
            list = listAvailableMoves(board);
            heuristicTileCount = list.size();
        }else{
            heuristicTileCount *= 26;
        }

        return list;
    }

    public int checkAndCollapse(QuantumBoard3D board, QuantumMove move, QuantumMove statmove){
        int bresult = 0;
        int loop = board.checkLoopsUsingQuantumDoohickery(move);
        if(loop != 0){
            if(move == statmove){
                //System.out.println("Loop found on second move");
            }
            SplittableRandom r = new SplittableRandom();
            if(r.nextBoolean()){
                board.collapseTile(move.getMove1(), board.getMoveCount());
            }else{
                board.collapseTile(move.getMove2(), board.getMoveCount());
            }
            bresult = board.checkEntireBoard();
            if(bresult == 1){
                statmove.addWin(-1);
                statmove.addTotal(1);
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }else if(bresult == 2){
                statmove.addWin(1);
                statmove.addTotal(1);
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }
        }
        return bresult;
    }
    

}
