package com.Game;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class mAI {

    int gameType = 0;
    int difficulty = 0;
    int minimax = 0;
    boolean randomsearch = false;
    int branchingFactor = 9;
    long time = 200000000; //200 million nanoseconds, or 200 milliseconds
    Board board;
    //Board backup;
    Move bestMove;
    SplittableRandom r = new SplittableRandom();

    public mAI(int gameType, int difficulty){
        this.gameType = gameType;
        this.difficulty = difficulty;
        time *= difficulty;
        //System.out.println("Time var: " + time);

        //I don't think I actually use the branching factor, but it could come up in theory
        if(gameType == 4){
            branchingFactor = 36;
        }else if(gameType == 5){
            branchingFactor = 351;
        }
    }

    public Move check(Board board, int minimax, boolean randomsearch){
        this.board = board;
        this.minimax = minimax;
        this.randomsearch = randomsearch;

        iterativeSearch(1, board.getTurn(), Integer.MIN_VALUE, Integer.MAX_VALUE);

        board.move(bestMove);

        return bestMove;
    }

    public double iterativeSearch(int depth, int turn, double alpha, double beta){
        double bestScore;

        //value for alpha beta pruning to check against
        if(turn % 2 == 0){
            bestScore = Integer.MIN_VALUE;
        }else{
            bestScore = Integer.MAX_VALUE;
        }

        int state = board.checkEntireBoard();

        //make it stop if it hits immediate win
        if(state == 1){
            return (-100000 / depth);
        }else if(state == 2){
            return (100000 / depth);
        }

        ArrayList<Move> moveList = turnMoves(board.getAvailable(), turn);

        for(int i = 0; i < moveList.size(); i++){
            Move move = moveList.get(i);
            board.move(move);

            double score = 0;

            if(gameType == 4 || gameType == 5){
                int result = board.checkLoops(move);
                if(result == 1){
                    //System.out.println("Loop found");
                    if(r.nextBoolean()){
                        board.collapseTile(move.loc, turn);
                    }else{
                        board.collapseTile(move.loc2, turn);
                    }
                    result = board.checkEntireBoard();
                    if(result == 1){
                        move.decrementWins();
                        move.incrementTotal();
                        score = -1;
                    }else if(result == 2){
                        move.incrementWins();
                        move.incrementTotal();
                        score = 1;
                    }
                }
            }else{
                int result = board.checkEntireBoard();
                if(result == 1){
                    move.decrementWins();
                    move.incrementTotal();
                    score = -1;
                }else if(result == 2){
                    move.incrementWins();
                    move.incrementTotal();
                    score = 1;
                }
            }

            //skip the stuff if already victory
            if(score == 0){
                if(depth == minimax){
                    if(randomsearch){
                        score = randomSearchManager(turn + 1, depth);
                    }else{
                        score = board.score();
                    }
                }else{
                    score = iterativeSearch(depth + 1, turn + 1, alpha, beta);
                }
            }

            board.unmove(move);

            //scoremaxing
            if(turn % 2 == 0){
                if(score > bestScore){
                    bestScore = score;
                    if(depth == 1){
                        bestMove = move;
                    }
                }
                alpha = Math.max(alpha, score);
                if(beta <= alpha){
                    break;
                }
            }else{
                if(score < bestScore){
                    bestScore = score;
                }
                alpha = Math.min(alpha, score);
                if(beta <= alpha){
                    break;
                }
            }
        }

        return bestScore;
    }

    public double randomSearchManager(int turn, int depth){
        long start = System.nanoTime();
        //divides the time per search by how many branches there are (approximately)
        long timeperthing = (long)(time / (Math.pow(branchingFactor, depth)));
        //System.out.println("Time per branch: " + timeperthing);
        long end = start + timeperthing;
        ArrayList<Move> available = turnMoves(board.getAvailable(), turn);
        int searches = 0;
        while(System.nanoTime() < end){
            for(int i = 0; i < available.size(); i++){
                board.move(available.get(i));
                randomSearch(available.get(i), turn + 1);
                board.unmove(available.get(i));
            }
            searches++; 
        }

        System.out.println(available.size() + " moves were each searched " + searches + " times");

        //provides the score (between -1 and 1)
        //maximizes based on which player is looking
        double wins = scoreRandomList(available);

        return wins;
    }

    public void randomSearch(Move move, int turn){
        //stores all moves so that the board state can be reverted at the end of a search
        ArrayList<Move> pastMoveList = new ArrayList<>();
        //int totalOptions = 0;
        while(true){
            long t0 = System.nanoTime();
            //ArrayList<Move> available = turnMoves(board.getAvailable(), turn);
            ArrayList<Integer> open = new ArrayList<>();
            for(int i = 0; i < board.getTurnArray().length; i++){
                if(board.getTurnArray()[i] == 0){
                    open.add(i);
                }
            }
            long t1 = System.nanoTime();

            //does some preliminary checks based on number of available moves
            if(open.size() == 1 && gameType > 3){
                move.incrementTotal();
                //System.out.println("Added stalemate");
                break;
            }else if(open.size() == 0){
                int result = board.checkEntireBoard();
                if(result == 1){
                    move.decrementWins();
                    move.incrementTotal();
                    //System.out.println("Added p1 victory");
                    break;
                }else if(result == 2){
                    move.incrementWins();
                    move.incrementTotal();
                    //System.out.println("Added p2 victory");
                    break;
                }else{
                    move.incrementTotal();
                    break;
                }
            }

            int move1 = r.nextInt(0, open.size());
            int move2;
            while(true){
                move2 = r.nextInt(0, open.size());
                if(move2 != move1){
                    break;
                }
            }

            move1 = open.get(move1);
            move2 = open.get(move2);

            Move choice = new Move(move1, move2);
            choice.setTurn(turn);
            turn += 1;

            long t2 = System.nanoTime();
            board.move(choice);
            pastMoveList.add(choice);
            long t3 = System.nanoTime();
            if(gameType > 3){
                int result = checkAndCollapse(board, choice, move);
                if(result > 0){
                    break;
                }
            }else{
                int result = board.checkEntireBoard();
                if(result == 1){
                    move.decrementWins();
                    move.incrementTotal();
                    break;
                }else if(result == 2){
                    move.incrementWins();
                    move.incrementTotal();
                    break;
                }
            }

            long t4 = System.nanoTime();
            //System.out.println("getting options took " + (t1 - t0) + " ns, checking stuff took " + (t2 - t1) + ", moving took " + (t3 - t2) + ", evaluating board took " + (t4 - t3));
        }
        //System.out.println(pastMoveList.size() + " moves till the end, " + totalOptions / pastMoveList.size() + "options per move");
        //I'm not quite sure if making this go in reverse fixed the problems, but probably this is good
        for(int i = pastMoveList.size() - 1; i >= 0 ; i--){
            board.unmove(pastMoveList.get(i));
        }
    }

    public int checkAndCollapse(Board board, Move move, Move statmove){
        int result = 0;
        int loop = board.checkLoops(move);
        if(loop != 0){
            if(r.nextBoolean()){
                board.collapseTile(move.loc, board.getTurn());
                //System.out.println("Collapse on " + move.loc);
            }else{
                board.collapseTile(move.loc2, board.getTurn());
                //System.out.println("Collapse on " + move.loc2);
            }
            result = board.checkEntireBoard();
            if(result == 1){
                statmove.decrementWins();
                statmove.incrementTotal();
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }else if(result == 2){
                statmove.incrementWins();
                statmove.incrementTotal();
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }
        }
        return result;
    }

    public ArrayList<Move> turnMoves(ArrayList<Move> list, int turn){
        for(int i = 0; i < list.size(); i++){
            list.get(i).setTurn(turn);
        }
        return list;
    }

    public void chooseCollapse(Board board, int option1, int option2){
        this.board = board;

        long start = System.nanoTime();
        long end = start + time;

        Move move1 = new Move(option1);
        Move move2 = new Move(option2);

        while(System.nanoTime() < end){
            board.collapseTile(option1, board.getTurn() - 1);
            int result = board.checkEntireBoard();
            if(result == 1){
                move1.decrementWins();
                move1.incrementTotal();
            }else if(result == 2){
                move1.incrementWins();
                move1.incrementTotal();
            }else{
                randomSearch(move1, board.getTurn());
            }
            board.uncollapseTile(option1);

            board.collapseTile(option2, board.getTurn() - 1);
            result = board.checkEntireBoard();
            if(result == 1){
                move2.decrementWins();
                move2.incrementTotal();
            }else if(result == 2){
                move2.incrementWins();
                move2.incrementTotal();
            }else{
                randomSearch(move2, board.getTurn());
            }
            board.uncollapseTile(option2);
        }
        double score1 = move1.wins / move1.total;
        double score2 = move2.wins / move2.total;
        if(score1 > score2){
            board.collapseTile(option1, board.getTurn() - 1);
        }else{
            board.collapseTile(option2, board.getTurn() - 1);
        }
    }

    public double scoreRandomList(ArrayList<Move> list){
        if(list.size() == 0){
            return 0;
        }
        double wins = 0;
        //not do another minimax layer if too few options
        if(list.get(0).total < 5){
            for(int i = 0; i < list.size(); i++){
                wins += list.get(i).wins;
            }
        }else{
            if(list.get(0).turn % 2 == 0){
                wins = -1;
                for(int i = 0; i < list.size(); i++){
                    //System.out.println(available.get(i).wins + " / " + available.get(i).total);
                    if(list.get(i).wins / list.get(i).total > wins){
                        wins = list.get(i).wins / list.get(i).total;
                    }
                }
            }else{
                wins = 1;
                for(int i = 0; i < list.size(); i++){
                    //System.out.println(available.get(i).wins + " / " + available.get(i).total);
                    if(list.get(i).wins / list.get(i).total < wins){
                        wins = list.get(i).wins / list.get(i).total;
                    }
                }
            }
        }
        return wins;
    }
}
