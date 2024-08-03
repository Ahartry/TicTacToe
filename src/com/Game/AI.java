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
                score = evalBoard(board, depth);
            }else{
                board.calculateActive(move);
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

        return bestScore;
    }

    public int evalBoard(LargeBoard board, int depth){
        //player two is positive
        int score = 0;

        //values the location of each move
        score += checkAllMoves(board);

        //checks the two in a rows
        score += checkTwos(board);

        //makes it not care about moves if victory, prioritize sooner victories
        if(score > 50000){
            score = 1000000;
            score *= (1 / depth);
        }else if(score < -50000){
            score = -1000000;
            score *= (1 / depth);
        }

        return score;
    }

    public int checkAllMoves(LargeBoard board){
        int score = 0;
        State state;

        for(int i = 0; i < 9; i++){
            state = board.getBoardArray(i % 3, (int) Math.floor(i / 3)).getState();
            if(state == State.Player1){
                if(i % 2 != 0){
                    score += -30;
                }else if(i == 4){
                    score += -50;
                }else{
                    score += -40;
                }
            }else if(state == State.Player2){
                if(i % 2 != 0){
                    score += 30;
                }else if(i == 4){
                    score += 50;
                }else{
                    score += 40;
                }
            }

        }

        for(int i = 0; i < board.getLocSize(); i++){

            if(board.getBoardArray(board.get(i).getBX(), board.get(i).getBY()).getState() == State.Blank)

            score += board.get(i).getMoveScore();
        }

        if(board.getState() == State.Player1){
            score += - 100000;
        }else if(board.getState() == State.Player2){
            score += 100000;
        }

        return score;
    }

    public int checkTwos(LargeBoard board){
        int score = 0;
        int lineScore = 0;

        //check for larger twos
        lineScore = board.getBoardArray(0, 0).toNum() + board.getBoardArray(1, 0).toNum() + board.getBoardArray(2, 0).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(0, 1).toNum() + board.getBoardArray(1, 1).toNum() + board.getBoardArray(2, 1).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(0, 2).toNum() + board.getBoardArray(1, 2).toNum() + board.getBoardArray(2, 2).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(0, 0).toNum() + board.getBoardArray(0, 1).toNum() + board.getBoardArray(0, 2).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(1, 0).toNum() + board.getBoardArray(1, 1).toNum() + board.getBoardArray(1, 2).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(2, 0).toNum() + board.getBoardArray(2, 1).toNum() + board.getBoardArray(2, 2).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(0, 0).toNum() + board.getBoardArray(1, 1).toNum() + board.getBoardArray(2, 2).toNum();
        score += scoreLine(lineScore, 10);
        lineScore = board.getBoardArray(2, 0).toNum() + board.getBoardArray(1, 1).toNum() + board.getBoardArray(0, 2).toNum();
        score += scoreLine(lineScore, 10);


        //checks for inside each board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                lineScore = board.getBoardArray(i, j).getBoardArray(0, 0).toNum() + board.getBoardArray(i, j).getBoardArray(1, 0).toNum() + board.getBoardArray(i, j).getBoardArray(2, 0).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(0, 1).toNum() + board.getBoardArray(i, j).getBoardArray(1, 1).toNum() + board.getBoardArray(i, j).getBoardArray(2, 1).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(0, 2).toNum() + board.getBoardArray(i, j).getBoardArray(1, 2).toNum() + board.getBoardArray(i, j).getBoardArray(2, 2).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(0, 0).toNum() + board.getBoardArray(i, j).getBoardArray(0, 1).toNum() + board.getBoardArray(i, j).getBoardArray(0, 2).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(1, 0).toNum() + board.getBoardArray(i, j).getBoardArray(1, 1).toNum() + board.getBoardArray(i, j).getBoardArray(1, 2).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(2, 0).toNum() + board.getBoardArray(i, j).getBoardArray(2, 1).toNum() + board.getBoardArray(i, j).getBoardArray(2, 2).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(0, 0).toNum() + board.getBoardArray(i, j).getBoardArray(1, 1).toNum() + board.getBoardArray(i, j).getBoardArray(2, 2).toNum();
                score += scoreLine(lineScore, 1);
                lineScore = board.getBoardArray(i, j).getBoardArray(2, 0).toNum() + board.getBoardArray(i, j).getBoardArray(1, 1).toNum() + board.getBoardArray(i, j).getBoardArray(0, 2).toNum();
                score += scoreLine(lineScore, 1);
            }
        }

        return score;
    }

    public int scoreLine(int line, int mult){
        int score = 0;

        if(line == -6){
            score = -5;
        }else if(line == 4){
            score = 5;
        }

        score *= mult;

        return score;
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
