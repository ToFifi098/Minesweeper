package minesweeper;

public class Cell {
    boolean isMine;
    int surr;
    char label;

    Cell(boolean isMine){
        this.isMine = isMine;
        this.surr = 0;
    }
}
