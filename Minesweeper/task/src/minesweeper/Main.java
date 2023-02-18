package minesweeper;

import javax.print.attribute.standard.PrinterName;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("How many mines do you want? >");
        int mines = scanner.nextInt();

        final int n = 9, m = 9;
        Cell[][] field = new Cell[n][m];



        for (int i = 0; i < n; i ++ ){
            for (int j = 0; j < m; j ++ ){
                field[i][j] = new Cell(false);
                field[i][j].label = '.';
            }
        }

        GenerateMines(mines, n, m, field);

        CountMines(field, n, m);

        PrintField(field, n, m);

        boolean first = true;
        do{
            System.out.print("Set/unset mine marks or claim a cell as free: > ");
            int x, y;
            String comm;
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
            comm = scanner.next();



            if(x < 0 || y < 0 ||x >= n || y >= m){
                System.out.println("Wrong coordinates");
                continue;
            }


            if(comm.equals("mine")){
                if(SetMine(field, x, y)) continue;
            }
            else if(comm.equals("free")){
                int state = SetFree(field, x, y, first);
                first = false;
                if(state == 0) continue;
                else if(state == 1) break;
            }


            PrintField(field, n, m);

        }while (!CheckWin(field));


    }

    static boolean SetMine(Cell[][] field, int x , int y){
        if (field[y][x].label == '.'){
            field[y][x].label = '*';
            return false;
        }
        else if (field[y][x].label == '*'){
            field[y][x].label = '.';
            return false;
        }
        else {
            System.out.println("There is a number here!");
            return true;
        }
    }

    static int SetFree(Cell[][] field, int x , int y, boolean first){
        if(field[y][x].label != '.'){
            System.out.println("Wrong field");
            return 0;
        }
        else {
            if (field[y][x].isMine){
                if(first){
                    GenerateMines(1,9,9,field);
                    field[y][x].isMine = false;
                    for(int i = 0; i < 9; i++){
                        for(int j = 0; j < 9; j++){
                            field[i][j].surr = 0;
                        }
                    }
                    CountMines(field, 9, 9);
                    Explore(field, x, y);
                    return 2;
                }
                for (int i = 0; i < 9; i++){
                    for (int j = 0; j < 9; j++){
                        if(field[i][j].isMine) field[i][j].label = 'X';
                    }
                }
                PrintField(field, 9, 9);
                System.out.println("You stepped on a mine and failed!");
                return 1;
            }
            else {
                Explore(field, x, y);
                return 2;
            }
        }
    }

    static void Explore(Cell[][] field, int y, int x){
        if((    field[x][y].label == '.')
                || (field[x][y].label == '*' && !field[x][y].isMine)
        ){
            if(field[x][y].surr > 0){
                field[x][y].label = (char) (field[x][y].surr + '0');
            }
            else {
                field[x][y].label = '/';
                if(x-1 >= 0 ){
                    Explore(field, y, x - 1);
                }
                if(x+1 < 9){
                    Explore(field, y, x + 1);
                }
                if(y+1 < 9){
                    Explore(field, y + 1, x);
                }
                if(y-1 >= 0){
                    Explore(field, y - 1, x);
                }
                if(y-1 >= 0 && x-1 >= 0){
                    Explore(field, y - 1, x - 1);
                }
                if(y-1 >= 0 && x+1 < 9){
                    Explore(field, y - 1, x + 1);
                }
                if(y+1 < 9 && x-1 >= 0){
                    Explore(field, y + 1, x - 1);
                }
                if(y+1 < 9 && x+1 < 9){
                    Explore(field, y + 1, x + 1);
                }
            }
        }
    }

    static void GenerateMines(int count, int n, int m, Cell[][] field) {
        Random random = new Random();

        for(int i = 0; i < count; ){
            int x = random.nextInt(n);
            int y = random.nextInt(m);

            if(!field[x][y].isMine){
                field[x][y].isMine = true;
                field[x][y].label = '.';
                i++;
            }
        }
    }

    static void CountMines(Cell[][] field, int n, int m) {
        for (int i = 0; i < n; i ++){
            for (int j = 0; j < m; j ++){
                if(field[i][j].isMine) continue;
                if(i-1 >= 0 ){
                    if(field[i-1][j].isMine) IncreaseMines(field[i][j]);
                }
                if(i+1 < n){
                    if(field[i+1][j].isMine) IncreaseMines(field[i][j]);
                }
                if(j+1 < m){
                    if(field[i][j+1].isMine) IncreaseMines(field[i][j]);
                }
                if(j-1 >= 0){
                    if(field[i][j-1].isMine) IncreaseMines(field[i][j]);
                }
                if(j-1 >= 0 && i-1 >= 0){
                    if(field[i-1][j-1].isMine) IncreaseMines(field[i][j]);
                }
                if(j-1 >= 0 && i+1 < n){
                    if(field[i+1][j-1].isMine) IncreaseMines(field[i][j]);
                }
                if(j+1 < m && i-1 >= 0){
                    if(field[i-1][j+1].isMine) IncreaseMines(field[i][j]);
                }
                if(j+1 < m && i+1 < n){
                    if(field[i+1][j+1].isMine) IncreaseMines(field[i][j]);
                }

            }
        }
    }

    static void IncreaseMines(Cell cell){
        cell.surr++;
    }

    static void PrintField(Cell[][] field,int n ,int m){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < n; i ++ ){
            System.out.print( i +1 +"|");
            for (int j = 0; j < m; j ++ ){
                System.out.print(field[i][j].label);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    static boolean CheckWin(Cell[][] field){
        boolean win1 = true;
        boolean win2 = true;

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if(field[i][j].isMine && field[i][j].label != '*'){
                    win1 = false;
                }
                if(!field[i][j].isMine && field[i][j].label == '.'){
                    win2 = false;
                }
            }
        }
        if(win1 || win2) {
            System.out.println("Congratulations! You found all the mines!");
            return true;
        }
        else return false;
    }
}



