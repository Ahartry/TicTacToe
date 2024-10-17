package com.Game;

import java.util.ArrayList;

public class mAI {

    int gameType = 0;
    int difficulty = 0;
    int minimax = 0;
    boolean randomsearch = false;
    Board board;
    Board backup;
    Move bestMove;

    public mAI(int gameType, int difficulty){
        this.gameType = gameType;
        this.difficulty = difficulty;
    }

    public void check(Board board, int minimax, boolean randomsearch){
        this.board = board;
        this.backup = board;
        this.minimax = minimax;
        this.randomsearch = randomsearch;
    }

    public int iterativeSearch(int depth, boolean turn, int alpha, int beta){
        int bestScore;

        if(turn){
            bestScore = Integer.MIN_VALUE;
        }else{
            bestScore = Integer.MAX_VALUE;
        }

        int state = board.checkEntireBoard();

        //make it do less
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

            int score;

            if(depth == minimax){
                if(randomsearch){

                }else{
                    score = board.score();
                    score /= depth;
                }
            }else{
                score = iterativeSearch(depth + 1, !turn, alpha, beta);
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

}
