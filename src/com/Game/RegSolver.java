package com.Game;

public class RegSolver {

    /*
     * 0 1 2
     * 3 4 5
     * 6 7 8
     */
    
    public static int checkMove(Board b, int loc, int scale){
        int result = 0;

        //if even scale (non 3d)
        if(b.getScale() % 2 == 0){

            int[] vals = new int[8];

            //gets location on smaller board
            int board_loc = loc % 9;
            int row = board_loc / 3;
            int col = board_loc % 3;

            for(int i = 0; i < 3; i++){
                //row
                //loc - board_loc is first square on board, then add offsets to make row
                int val = b.getBoardArrays().get(scale)[loc - board_loc + (row * 3) + i];
                if(val % 2 == 1){
                    vals[0] += 1;
                }else if(val > 0){
                    vals[1] += 1;
                }

                //col
                val = b.getBoardArrays().get(scale)[loc - board_loc + col + (i * 3)];
                if(val % 2 == 1){
                    vals[2] += 1;
                }else if(val > 0){
                    vals[3] += 1;
                }

                //if board location is even, it is eligible for diagonal
                //I'm just going to check both
                if(board_loc % 2 == 0){
                    //forward diagonal (0 4 8)
                    val = b.getBoardArrays().get(scale)[loc - board_loc + (i * 4)];
                    if(val % 2 == 1){
                        vals[4] += 1;
                    }else if(val > 0){
                        vals[5] += 1;
                    }

                    //back diagonal (2 4 6)
                    val = b.getBoardArrays().get(scale)[loc - board_loc + 2 + (i * 2)];
                    if(val % 2 == 1){
                        vals[6] += 1;
                    }else if(val > 0){
                        vals[7] += 1;
                    }
                }
            }

            for(int i = 0; i < 8; i++){
                //if any of the rows has 3 of the same player, it returns that player number
                //index 0 is player 1 (does not align with modulo)
                if(vals[i] == 3){
                    if(i % 2 == 0){
                        return 1;
                    }else{
                        return 2;
                    }
                }
            }
        //TODO 3d
        }else{
            //13 lines, so 26 counts
            int[] vals = new int[26];

            //gets location on smaller board
            int board_loc = loc % 27;
            int board = board_loc / 9;
            int row = (board_loc - board * 9) / 3;
            int col = (board_loc - board * 9) % 3;

            for(int i = 0; i < 3; i++){
                //row
                //loc - board_loc is first square on board, then add offsets to make row
                int val = b.getBoardArrays().get(0)[loc - board_loc + (board * 9) + (row * 3) + i];
                if(val % 2 == 1){
                    vals[0] += 1;
                }else if(val > 0){
                    vals[1] += 1;
                }

                //col
                val = b.getBoardArrays().get(0)[loc - board_loc + (board * 9) + col + (i * 3)];
                if(val % 2 == 1){
                    vals[2] += 1;
                }else if(val > 0){
                    vals[3] += 1;
                }

                //vertical
                val = b.getBoardArrays().get(0)[loc - board_loc + (i * 9) + (row * 3) + col];
                if(val % 2 == 1){
                    vals[4] += 1;
                }else if(val > 0){
                    vals[5] += 1;
                }

                //if board location is even, it is eligible for diagonal
                //I'm just going to check both
                if(board_loc % 2 == 0){
                    //forward diagonal (0 4 8)
                    val = b.getBoardArrays().get(0)[loc - board_loc + (i * 4)];
                    if(val % 2 == 1){
                        vals[4] += 1;
                    }else if(val > 0){
                        vals[5] += 1;
                    }

                    //back diagonal (2 4 6)
                    val = b.getBoardArrays().get(0)[loc - board_loc + 2 + (i * 2)];
                    if(val % 2 == 1){
                        vals[6] += 1;
                    }else if(val > 0){
                        vals[7] += 1;
                    }
                }
            }

            for(int i = 0; i < 26; i++){
                //if any of the rows has 3 of the same player, it returns that player number
                //index 0 is player 1 (does not align with modulo)
                if(vals[i] == 3){
                    if(i % 2 == 0){
                        return 1;
                    }else{
                        return 2;
                    }
                }
            }
        }

        return result;
    }
}
