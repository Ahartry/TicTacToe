package com.Game;

import java.util.ArrayList;
import java.util.Random;

public class oAI {

    MassiveBoard board;
    MassiveMove bestMove;
    int bestScore = Integer.MIN_VALUE;
    int movetotal;
    int total;
    int time = 50000000;

    ArrayList<MassiveMove> optionList;
    ArrayList<MassiveMove> backtrackList;

    public oAI(int x){
        time *= x;
    }

    public oAI(){

    }

    public void setDifficulty(int x){
        time *= x;
    }

    public MassiveMove checkMassiveBoard(MassiveBoard board2){
        board = new MassiveBoard();
        board.copy(board2);

        optionList = new ArrayList<>(board2.listActive(State.Player2));
        backtrackList = new ArrayList<>();

        bestMove = new MassiveMove();
        
        long start = System.nanoTime();
        start += time;

        //System.out.println("Checking " + optionList.size() + " available moves");

        while(System.nanoTime() < start){
            for(int i = 0; i < optionList.size(); i++){
                exploreRandom(optionList.get(i));
                board.backTrack(backtrackList);
                total++;
            }
        }

        for(int i = 0; i < optionList.size(); i++){
            if(optionList.get(i).getWins() > bestScore){
                bestMove = optionList.get(i);
                bestScore = optionList.get(i).getWins();
                movetotal = optionList.get(i).getTotal();
            }
        }
        bestMove.print();
        System.out.println(total + " branches checked, " + bestScore + " wins");
        board2.move(bestMove);

        return bestMove;
    }

    public void exploreRandom(MassiveMove move){
        backtrackList.clear();
        board.move(move);
        backtrackList.add(move);
        boolean turn = true;
        State state = State.Player1;
        while(true){
            if(turn){
                state = State.Player1;
            }else{
                state = State.Player2;
            }
            ArrayList<MassiveMove> list = new ArrayList<>(board.listActive(state));

            if(list.size() == 0){
                move.incrementTotal();
                //no victory
                break;
            }

            int randomMove = new Random().nextInt(0, list.size());

            int result = board.move(list.get(randomMove));
            backtrackList.add(list.get(randomMove));

            if(result == 1){
                MassiveMove backMove = new MassiveMove(list.get(randomMove));
                backMove.clearCell();
                backtrackList.add(backMove);
            }else if(result == 2){
                MassiveMove backMove = new MassiveMove(list.get(randomMove));
                backMove.clearBoard();
                backtrackList.add(backMove);
            }else if(result == 3){
                move.incrementTotal();
                move.changeWins(-1);
                break;
            }else if(result == 4){
                move.incrementTotal();
                move.changeWins(1);
                break;
            }


            turn = !turn;
        }

    }

}
