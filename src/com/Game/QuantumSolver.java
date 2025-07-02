package com.Game;

public class QuantumSolver {

    static int result;
    static boolean[] skipList;

    public static int checkLoops(Board b, int move1, int move2){

        skipList = new boolean[(int) Math.pow(3, b.getScale())];

        int start = move1;
        int next = move2;

        result = 0;

        skipList[start] = true;
        iterativeSearch(b, start, next);

        return result;
    }

    public static void iterativeSearch(Board b, int start, int loc){

        if(loc == start){
            result = 1;
        }

        if(result == 1){
            return;
        }

        for(int i = 0; i < b.getQCount()[loc]; i++){
            int next = b.getQMList()[loc][i];

            if(!skipList[next]){
                skipList[next] = true;
                iterativeSearch(b, start, next);
            }
        }

    }

    public static void collapseTile(Board b, int loc, int t){

        if(b.getBoardArrays().get(0)[loc] != 0){
            System.out.println("Cannot collapse tile " + loc + ", already occupied");
            return;
        }

        b.getBoardArrays().get(0)[loc] = t;

        //does the chain reaction to collapse tiles
        for(int i = 0; i < b.getQCount()[loc]; i++){
            int next = b.getQMList()[loc][i];
            int turn = b.getQTList()[loc][i];

            if(b.getBoardArrays().get(0)[next] == 0){
                collapseTile(b, next, turn);
            }
        }

    }

    public static void uncollapseTile(Board b, int loc){

        if(b.getBoardArrays().get(0)[loc] == 0){
            return;
        }

        b.getBoardArrays().get(0)[loc] = 0;

        for(int i = 0; i < b.getQCount()[loc]; i++){
            int next = b.getQMList()[loc][i];

            if(!(b.getBoardArrays().get(0)[next] == 0)){
                uncollapseTile(b, next);
            }
        }

    }
}
