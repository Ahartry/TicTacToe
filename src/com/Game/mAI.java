package com.Game;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class mAI {

    int gameType = 0;
    int difficulty = 0;
    int minimax = 0;
    boolean randomsearch = false;
    long time = 200000000; //200 million nanoseconds, or 200 milliseconds
    Board board;
    Board backup;
    Move bestMove;

    public mAI(int gameType, int difficulty){
        this.gameType = gameType;
        this.difficulty = difficulty;
        time *= difficulty;
    }

    public Move check(Board board, int minimax, boolean randomsearch){
        this.board = board;
        this.backup = board;
        this.minimax = minimax;
        this.randomsearch = randomsearch;

        iterativeSearch(1, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        board.move(bestMove);

        return bestMove;
    }

    public double iterativeSearch(int depth, boolean turn, double alpha, double beta){
        double bestScore;

        //value for alpha beta pruning to check against
        if(turn){
            bestScore = Integer.MIN_VALUE;
        }else{
            bestScore = Integer.MAX_VALUE;
        }

        int state = board.checkEntireBoard();

        //make it stop if it hits immediate win
        //TODO figure out if the /depth is needed. If it is, make usage consistent
        if(state == 1){
            return (-100000 / depth);
        }else if(state == 2){
            return (100000 / depth);
        }

        ArrayList<Move> moveList = board.getAvailable();

        for(int i = 0; i < moveList.size(); i++){
            Move move = moveList.get(i);
            move.setTurn(turn);
            board.move(move);

            // try {
            //     Thread.sleep(100);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }

            double score = 0;

            if(gameType == 4 || gameType == 5){
                int result = board.checkLoops(move);
                if(result == 1){
                    System.out.println("Loop found");
                    SplittableRandom r = new SplittableRandom();
                    if(r.nextBoolean()){
                        board.collapseTile(move.loc, board.getMoveCount());
                    }else{
                        board.collapseTile(move.loc2, board.getMoveCount());
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
                        //TODO this is for working on
                        //score = randomSearchManager();
                    }else{
                        score = board.score();
                        //not sure if this is really necessary
                        //score /= depth;
                    }
                }else{
                    score = iterativeSearch(depth + 1, !turn, alpha, beta);
                }
            }else{
                System.out.println("Victory of sorts detected");
            }

            //System.out.println("Move " + move + " has score of " + score);

            board.unmove(move);

            //scoremaxing
            if(turn){
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

    public double randomSearchManager(){
        long start = System.nanoTime();
        long end = start + time;
        ArrayList<Move> available = board.getAvailable();
        while(System.nanoTime() < end){
            for(int i = 0; i < available.size(); i++){
                board.move(available.get(i));
                randomSearch(available.get(i));
                board.unmove(available.get(i));
            }
        }

        double wins = -1;
        for(int i = 0; i < available.size(); i++){
            if(available.get(i).wins / available.get(i).total > wins){
                wins = available.get(i).wins / available.get(i).total;
            }
        }
        return wins;
    }

    public void randomSearch(Move move){
        while(true){
            long t0 = System.nanoTime();
            ArrayList<Move> available = board.getAvailable();
            System.out.println(available.size());
            long t1 = System.nanoTime();
            if(available.size() == 1 && gameType > 3){
                move.incrementTotal();
                //System.out.println("Added stalemate");
                break;
            }else if(available.size() == 0){
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
            SplittableRandom r = new SplittableRandom();
            int move1 = r.nextInt(0, available.size());
            int move2;

            //gets two different random numbers
            while(true){
                move2 = r.nextInt(0, available.size());
                if(move2 != move1){
                    break;
                }
            }

            Move choice;
            if(gameType > 3){
                choice = new Move(move1, move2);
                choice.setTurn(board.getMoveCount());
            }else{
                choice = new Move(move1);
            }

            long t2 = System.nanoTime();
            board.move(choice);
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
            System.out.println("getting options took " + (t1 - t0) + " ns, checking stuff took " + (t2 - t1) + ", moving took " + (t3 - t2) + ", evaluating board took " + (t4 - t3));
        }
    }

    public int checkAndCollapse(Board board, Move move, Move statmove){
        int bresult = 0;
        int loop = board.checkLoops(move);
        if(loop != 0){
            if(move == statmove){
                //System.out.println("Loop found on second move");
            }
            SplittableRandom r = new SplittableRandom();
            if(r.nextBoolean()){
                board.collapseTile(move.loc, board.getMoveCount());
            }else{
                board.collapseTile(move.loc2, board.getMoveCount());
            }
            bresult = board.checkEntireBoard();
            if(bresult == 1){
                statmove.decrementWins();
                statmove.incrementTotal();
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }else if(bresult == 2){
                statmove.incrementWins();
                statmove.incrementTotal();
                //System.out.println("Starting enemy move has win " + move.getMove1() + " " + move.getMove2());
            }
        }
        return bresult;
    }

}
