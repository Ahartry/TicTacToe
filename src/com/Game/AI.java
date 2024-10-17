package com.Game;

import java.util.ArrayList;

public class AI {

    int gameType;
    int bestMove;
    int bestScore;
    int previous;
    LargeBoard board;
    //LargeBoard backupBoard;

    int depthToSearch = 8;

    public AI(int gameType){
        this.gameType = gameType;
    }

    public void setDepth(int depth){
        depthToSearch = depth;
    }

    public int checkLargeBoard(LargeBoard board, int start){
        System.out.println("Thinking");
        long startTime = System.nanoTime();
        this.board = board;
        previous = start;
        this.board.calculateActive(start);
        bestScore = iterativeSearch(1, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println((System.nanoTime() - startTime) / 1000000 + " ms taken");

        return bestMove;
    }

    public int iterativeSearch(int depth, boolean turn, int alpha, int beta){
        int bestScore;

        if(turn){
            bestScore = Integer.MIN_VALUE;
        }else{
            bestScore = Integer.MAX_VALUE;
        }

        //make it do less
        if(board.getState() == State.Player1){
            return -100000;
        }else if(board.getState() == State.Player2){
            return 100000;
        }

        ArrayList<Integer> activeList = board.listActiveCells();

        for(int i = 0; i < activeList.size(); i++){
            int move = activeList.get(i);
            int score;
            board.move(move, turn);

            if(depth == depthToSearch){
                score = board.score(depth);
            }else{
                board.calculateActive(move);
                score = iterativeSearch(depth + 1, !turn, alpha, beta);
            }

            //stuff for checking earlier board victory
            if(board.getState() == State.Player2){
                score = 100000;
                score *= (1 / depth);
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
                alpha = max(alpha, score);
                if(beta <= alpha){
                    break;
                }
            }else{
                if(score < bestScore){
                    bestScore = score;
                }
                alpha = min(alpha, score);
                if(beta <= alpha){
                    break;
                }
            }
        }

        if(activeList.size() == 0){
            //stuff for checking earlier board victory
            if(board.getState() == State.Player1){
                bestScore = -100000;
                bestScore *= (1 / depth);
            }else if(board.getState() == State.Player2){
                bestScore = 100000;
                bestScore *= (1 / depth);
            }
        }

        return bestScore;
    }

    public int getMaximum(ArrayList<Integer> list){
        int highest;

        highest = -1000000;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) > highest){
                highest = list.get(i);
            }
        }

        return highest;
    }

    public int getMinimum(ArrayList<Integer> list){
        int lowest = 1000000;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) < lowest){
                lowest = list.get(i);
            }
        }

        return lowest;
    }

    public int max(int x, int y){
        if(x > y){
            return x;
        }else{
            return y;
        }
    }

    public int min(int x, int y){
        if(x < y){
            return x;
        }else{
            return y;
        }
    }

}
