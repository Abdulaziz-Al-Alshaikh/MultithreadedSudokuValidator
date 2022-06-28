/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SudokuValidator;

/**
 *
 * @author Abdulaziz Al-Alshaikh
 */

import java.util.*;
public class SudokuValidation {
    
    
    public static void main(String[] args) throws InterruptedException {
        int [][] board = 
        {
            {7, 9, 2, 1, 5, 4, 3, 8, 6}, 
            {6, 4, 3, 8, 2, 7, 1, 5, 9},
            {8, 5, 1, 3, 9, 6, 7, 2, 4},
            {2, 6, 5, 9, 7, 3, 8, 4, 1},
            {4, 8, 9, 5, 6, 1, 2, 7, 3},
            {3, 1, 7, 4, 8, 2, 9, 6, 5},
            {1, 3, 6, 7, 4, 8, 5, 9, 2},
            {9, 7, 4, 2, 1, 5, 6, 3, 8},
            {5, 2, 8, 6, 3, 9, 4, 1, 7}
        };
        
        long startTime = System.currentTimeMillis();
        boolean result = isValidMultiThreaded(board);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(String.valueOf(duration)+"ms");
    }
    
    

    static boolean isValidMultiThreaded(int [][] board) throws InterruptedException{
        
        List<Thread> threads = new ArrayList<>();
        /* Creating a new thread responsible for checking rows. */
        threads.add(new RowValidator(board));
        
        /* Creating a new thread responsible for checking columns.*/
        threads.add(new ColValidator(board));
        
        /* Creating additional 9 threads: each is allocated to a 3x3 sub-box.*/
        for(int row = 0; row < 9; row += 3) {
            for(int col = 0; col < 9; col += 3){
                threads.add(new SubBoxValidator(board, row, col));
            }
        }
        
        /* Starting executing all threads */
        for(Thread thread : threads)
            thread.start();
        
        
        for(Thread thread : threads){
        /*Waiting till 'thread' finsihes execution */
            thread.join(); 
        }
        
        /* Now we are sure that all threads terminated. Therefore, can gather the results */
        boolean result = true;
        for(Thread thread : threads){
            Validator validator = (Validator)thread; /* Validator class extends thread class */
            result = result && validator.isValid;
        }
        return result;
    }


    static class Validator extends Thread{
        int [][] board;
        boolean isValid;
        public Validator(int [][] board){
            this.board = board;
            this.isValid = true;
        }
    }
    
    static class RowValidator extends Validator{
        public RowValidator(int [][] board){
            super(board);
        }
        @Override
        public void run(){
            int row = 0;
            while(isValid && row < 9){
                isValid = checkRow(row);
                row++;
            }
        }
        
        boolean checkRow(int row){
            Set<Integer> uniqueVals = new HashSet<>();
            int countVals = 0;
            for(int i = 0; i < 9; i++){
                if(board[row][i] != 0){
                    countVals++;
                    uniqueVals.add(board[row][i]);
                }
            }
            return countVals - uniqueVals.size() == 0;
        }

    }
    static class ColValidator extends Validator{
        public ColValidator(int [][] board){
            super(board);
        }
        
        @Override
        public void run(){
            int col = 0;
            while(isValid && col < 9){
                isValid = checkCol(col);
                col++;
            }
        }
        
        boolean checkCol(int col){
            Set<Integer> uniqueVals = new HashSet<>();
            int countVals = 0;
            for(int i = 0; i < 9; i++){
                if(board[i][col] != 0){
                    countVals++;
                    uniqueVals.add(board[i][col]);
                }
            }
            return countVals - uniqueVals.size() == 0;
        }

    }
    
    static class SubBoxValidator extends Validator{
        int row, col;
        public SubBoxValidator(int [][] board, int row, int col){
            super(board);
            this.row = row;
            this.col = col;
        }
        
        @Override
        public void run(){
            this.isValid = checkSubBox();
        }
        
        boolean checkSubBox(){
            Set<Integer> uniqueVals = new HashSet<>();
            int countVals = 0;
            for(int i = row; i < row + 3; i++){
                for(int j = col; j < col + 3; j++){
                    if(board[i][j] != 0){
                        countVals++;
                        uniqueVals.add(board[i][j]);
                    }
                }
            }
            return countVals - uniqueVals.size() == 0;
        }
    }
    

}
