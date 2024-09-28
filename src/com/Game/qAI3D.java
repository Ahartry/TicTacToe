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
    int movetotal;
    int total;
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

    public int checkCollapse(QuantumBoard3D board, int option1, int option2){
        this.board = new QuantumBoard3D();
        this.backup = new QuantumBoard3D();

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

            board.copy(backup);
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

        int otherScore = 0;
        //System.out.println("Ended at " + System.nanoTime());

        if(one.getWins() > two.getWins()){
            bestScore = one.getWins();
            otherScore = two.getWins();
            bestMove1 = option1;
        }else{
            bestScore = two.getWins();
            otherScore = one.getWins();
            bestMove1 = option2;
        }

        board.copy(backup);

        System.out.println("Best collapse is " + bestMove1 + " with a score of " + bestScore + " out of " + total / 2 + " (" + total + " branches checked). Other choice was " + otherScore);
        board.collapseTile(bestMove1, board.getMoveCount() - 1);
        //System.exit(0);

        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 3);
    }

    @SuppressWarnings("unused")
    public int checkQuantumBoard3D(QuantumBoard3D RealBoard){

        board = new QuantumBoard3D();
        backup = new QuantumBoard3D();
        board.copy(RealBoard);
        backup.copy(RealBoard);

        thistotal = 0;

        startList = listAvailableMoves2(board);

        int empty = RealBoard.listActiveTiles().size();
        //number of combinations (moves)
        int timeThing = heuristicTileCount * (26);
        int timeThing2 = (empty - 1) * (empty) / 2;
        System.out.println("First move has " + timeThing + " options, second has " + timeThing2);
        //System.out.println(heuristicTileCount);
        //System.exit(0);
        
        long timePerBranch = time / (timeThing * timeThing2);
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
            //iterates through the ply 2 list
            loop2:
            for(int j = 0; j < nextList.size(); j++){
                start += timePerBranch;

                //repeatedly searches from this point
                while(System.nanoTime() < (start + timePerBranch)){
                    long t0 = System.nanoTime();
                    board.copy(backup);
                    long t1 = System.nanoTime();
                    board.move(nextList.get(j));
                    int r = board.checkAndCollapse(nextList.get(j));
                    if(r == 1){
                        bestEnemyScore = Integer.MIN_VALUE;
                        nextList.get(j).addWin(-1);
                        nextList.get(j).addTotal(1);
                        break loop2;
                    }else if(r == 2){
                        nextList.get(j).addWin(1);
                        nextList.get(j).addTotal(1);
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
                double wins = nextList.get(j).getWins();
                double total = nextList.get(j).getTotal();
                //System.out.println(wins + " / " + total);
                if(wins / total < bestEnemyScore){
                    bestEnemyScore = wins / total;
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
            startList.get(i).addWin(bestEnemyScore);
            startList.get(i).addTotal(thistotal);

        }

        for(int i = 0; i < startList.size(); i++){
            if(startList.get(i).getWinsd() > bestScore){
                bestMove1 = startList.get(i).getMove1();
                bestMove2 = startList.get(i).getMove2();
                bestScore = startList.get(i).getWinsd();
                movetotal = startList.get(i).getTotal();
            }
        }

        System.out.println("Best move is " + bestMove1 + ", " + bestMove2 + " with a score of " + bestScore + " out of " + movetotal + " (" + total + " branches checked)");
        RealBoard.move(new QuantumMove(bestMove1, bestMove2));

        movetotal = 0;
        total = 0;
        bestScore = Integer.MIN_VALUE;

        return bestMove1 + (bestMove2 * 27);
    }

    @SuppressWarnings("unused")
    public void exploreRandom(QuantumBoard3D board, QuantumMove start){
        while(true){
            long t0 = System.nanoTime();
            int available = board.getAvailable();
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

            board.move(move);
            long t3 = System.nanoTime();
            int loop = board.checkLoopsUsingQuantumDoohickery(move);
            long t4 = System.nanoTime();
            if(loop == 1){
                //System.out.println("boo");
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
            long t5 = System.nanoTime();
            //System.out.println("getting options took " + (t1 - t0) + " ns, loading them took " + (t2 - t1) + ", moving took " + (t3 - t2) + ", checking loop took " + (t4 - t3) + ", collaping took " + (t5 - t4));
        }
    }

    public ArrayList<QuantumMove> listAvailableMoves(){
        ArrayList<QuantumMove> list = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            if(board.getBoardTile(i % 3, (i - ((i * 9) / 9)) / 3, i / 9).getState() != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 27; j++){
                if(board.getBoardTile(j % 3, (j - ((j * 9) / 9)) / 3, j / 9).getState() != State.Blank){
                    continue;
                }

                //This should NOT be needed, be here we are
                if(i == j){
                    continue;
                }
                list.add(new QuantumMove(i, j));
            }
        }

        return list;
    }

    public ArrayList<QuantumMove> listAvailableMoves(QuantumBoard3D board){
        ArrayList<QuantumMove> list = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            if(board.getBoardTile(i % 3, (i - ((i * 9) / 9)) / 3, i / 9).getState() != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 27; j++){
                if(board.getBoardTile(j % 3, (j - ((j * 9) / 9)) / 3, j / 9).getState() != State.Blank){
                    continue;
                }

                //This should NOT be needed, be here we are
                if(i == j){
                    continue;
                }
                list.add(new QuantumMove(i, j));
            }
        }

        return list;
    }

    public ArrayList<QuantumMove> listAvailableMoves2(QuantumBoard3D board){
        ArrayList<QuantumMove> list = new ArrayList<>();

        heuristicTileCount = 0;

        for(int i = 0; i < 27; i++){
            int z1 = i / 9;
            int x1 = i % 3;
            int y1 = ((i - (z1 * 9)) / 3);
            if(board.getBoardTile(x1, y1, z1).getState() != State.Blank){
                continue;
            }
            if(board.getBoardTile(x1, y1, z1).getMovesList().size() == 0){
                continue;
            }
            heuristicTileCount++;
            for(int j = 0; j < 27; j++){
                if(board.getBoardTile(j % 3, (j - ((j * 9) / 9)) / 3, j / 9).getState() != State.Blank){
                    continue;
                }

                //This should NOT be needed, yet here we are
                if(i == j){
                    continue;
                }
                list.add(new QuantumMove(i, j));
                //System.out.println("i: " + i + ", j: " + j);

            }
        }

        if(list.size() == 0){
            System.out.println("Heuristic thing returned no moves, using backup");
            list = listAvailableMoves(board);
            heuristicTileCount = list.size();
        }

        return list;
    }

}
