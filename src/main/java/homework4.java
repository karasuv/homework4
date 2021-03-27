import javax.swing.plaf.basic.BasicColorChooserUI;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class homework4 {
    private static final Random RANDOM = new Random();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static char[][] field;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '.';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static String playerOnename="";
    private static int v;//   победа при размерности ( разм 3 <----> v = 3) (разм 5 <-----> v=4
    private static int n;
    private static  int attackCount=0;
    private static  int attackMaxCount=0;
    private  static int scoreHuman=0;
    private static int scoreAI=0;

    public static void main(String[] args) {
        System.out.print("Представтесь пожалуйста >>>>>");
        playerOnename = SCANNER.nextLine();



        while (true) {
            System.out.println("введите размерность квадтратного поля для игры (>2)and(<6)>>>>>");
            do {

                n= SCANNER.nextInt();
            } while (n<=2||n>=6);
            fieldSizeX=n;
            fieldSizeY=n;
            if (n==3) v=3;

            else v=n-1;

            initField();
            printField();
            while (true) {


                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, String.format("Поздравляем, %s выйграл",playerOnename))) {
                    break;
                }

                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Компьютер победил")) {
                    break;
                }


            }
            System.out.printf("SCORE IS: \n%s: %d || AI: %d\n",playerOnename,scoreHuman,scoreAI);
            System.out.println("Желаете повторить игру? >>>>> Y or N >>>>");

            if(!SCANNER.next().toLowerCase().equals("y")) break;
        }
    }

    private static boolean gameCheck(char dot, String s) {
        if (checkWin(dot)) {
            if (dot == DOT_HUMAN)
                scoreHuman++;
            else  scoreAI++;
            System.out.println(s);
            return true;
        }
        if(checkDraw()) {
            System.out.println("Draw!!!");
            return true;
        }
        return false;
    }

    private static boolean checkDraw(){
        for(int y=0;y<fieldSizeY;y++) {
            for (int x=0;x<fieldSizeX;x++) {
                if (isCellEmpty(x,y)) return false;
            }
        }
        return true;
    }

    private static boolean checkWin(char c) {
        int k=1;
        for (int i=0;i<n;i++) {
                for (int j=0;j<n;j++) {
                    if (field[i][j]==c) {k=1;
                        while(k<v&&i<=n-v&&j<=n-v)                    // проверка диагональ
                            if (field[i+k][j+k] == c) {
                                k++;
                                if (k==v) return true;
                            }
                            else {
                                k = 1;
                                break; }

                        while (k<v&&j-v+1>=0&&i<=n-v)           //проверка обратной диагонали
                            if (field[i+k][j-k]==c)  {
                                k++;
                                if (k==v) return  true;
                            }
                            else {k=1;
                                break;
                                    }

                        while (k<v&&i<=n-v)             //проверка верникальных
                            if (field[i+k][j]==c) {
                                k++;
                                if (k==v) return  true;
                            }
                            else {k=1;
                            break;
                            }
                        while (k<v&&j<=n-v)              //проверка горизонтальных
                            if (field[i][j+k]==c) {
                                k++;
                                if (k==v)  return true;
                            }
                            else {k=1;
                            break;
                            }

                    }
                }
        }
return false;
    }

    private static void aiTurn() {
        int x = 0;
        int y = 0;
        int x_getshot_on_win=0;
        int y_getshot_on_win=0;

        System.out.println("ход компьютера");
        int kmax = 0;
        int k = 1;
        int kw= 1;
        int line_without_one_empty = 0;
        boolean danger=false;
        boolean isPreWin=false;
        int[] rezf= new int[2];

        attackMaxCount=0;
        attackCount=0;


            // ищем предвыйгрышную ситуацию для компьютера  -  гадшот на  нулях
            for (int i = 0; i < n; i++) {  if (isPreWin) break;
                for (int j = 0; j < n; j++) {
                    if(isPreWin) break;

                    if (field[i][j] == DOT_AI || field[i][j] == DOT_EMPTY) {//&&!isPreWin

                        rezf=checkDiagonalLineForPreWinSituation(DOT_AI,i,j);
                        if (rezf!=null) {
                            x=rezf[0];
                            y=rezf[1];
                            isPreWin=true;
                        }


                        rezf=checkRevDiagonalLineForPreWinSituation(DOT_AI,i,j);
                        if (rezf!=null) {
                            x=rezf[0];
                            y=rezf[1];
                            isPreWin=true;
                        }



                        rezf=checkVerticalLineForPreWinSituation(DOT_AI,i,j);
                        if (rezf!=null) {
                            x=rezf[0];
                            y=rezf[1];
                            isPreWin=true;
                        }

                        rezf=checkHorizontalLineForPreWinSituation(DOT_AI,i,j);
                        if (rezf!=null) {
                            x=rezf[0];
                            y=rezf[1];
                            isPreWin=true;
                        }


                    }


                }
            }




        //проверка на предвыйгрышную ситуацию у соперника

        if (!isPreWin) {
            for (int i = 0; i < n; i++) {   if (isPreWin) break;
                for (int j = 0; j < n; j++) { if (isPreWin) break;
                    if (field[i][j] == DOT_HUMAN || field[i][j] == DOT_EMPTY) {

                        rezf = checkDiagonalLineForPreWinSituation(DOT_HUMAN, i, j);
                        if (rezf != null) {
                            x = rezf[0];
                            y = rezf[1];
                            isPreWin = true;
                        }


                        rezf = checkRevDiagonalLineForPreWinSituation(DOT_HUMAN, i, j);
                        if (rezf != null) {
                            x = rezf[0];
                            y = rezf[1];
                            isPreWin = true;
                        }
//

                        rezf = checkVerticalLineForPreWinSituation(DOT_HUMAN, i, j);
                        if (rezf != null) {
                            x = rezf[0];
                            y = rezf[1];
                            isPreWin = true;
                        }
//


                        rezf = checkHorizontalLineForPreWinSituation(DOT_HUMAN, i, j);
                        if (rezf != null) {
                            x = rezf[0];
                            y = rezf[1];
                            isPreWin = true;
                        }

//


                    }
                }


            }
        }

        if (!isPreWin) {//!danger&&
            // когда нет ситуаций для защиты...нужно идти в атаку
            //ищем ячейку которая включена в наибольшее кол-во линий размером в v в которых нет DOT_HUMAN
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {attackCount=0;
                    if (field[i][j] == DOT_AI || field[i][j] == DOT_EMPTY) {


                                if (checkLineWithoutRivalFieldDiagonal(DOT_AI,i,j))
                                                    attackCount++;


                            if (checkLineWithoutRivalFieldDiagonalReverse(DOT_AI,i,j))
                                                    attackCount++;

                           if  (checkLineWithoutRivalFieldVertical(DOT_AI,i,j))
                                                            attackCount++;

                             if  (checkLineWithoutRivalFieldHorizontal(DOT_AI,i,j))
                                                            attackCount++;


                    }

                if (attackCount>attackMaxCount&&field[i][j] == DOT_EMPTY) {
                    attackMaxCount = attackCount;
                    x=i;
                    y=j;
                }


                }
            }




//        do {
//
//                x = RANDOM.nextInt(fieldSizeX);
//                y = RANDOM.nextInt(fieldSizeY);
//            }
//        while (!isCellEmpty(x, y));

        }
            field[x][y] = DOT_AI;





    }

    private static void humanTurn() {
        int x,y;
        do {
            System.out.print("ввести координаты x и y  через пробел>>>>");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x,y)||!isCellEmpty(x,y));

            field[x][y] = DOT_HUMAN;

    }

    private static boolean isCellValid(int x, int y) {
        return x>=0 && y>=0 && x < fieldSizeX && y < fieldSizeY;
    }

    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;

    }


    private static void printField() {
        //System.out.println();
        for (int i=0;i<fieldSizeY;i++) {
            System.out.print("|");
            for (int j=0;j<fieldSizeX;j++) {

                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }

    }

    private static void initField() {
        field = new char[fieldSizeY][fieldSizeX];
        for (int i=0;i<fieldSizeY;i++) {
            for (int j=0;j<fieldSizeX;j++) {
                field[i][j]=DOT_EMPTY;
            }
        }

    }

    private static boolean checkLineWithoutRivalFieldDiagonal(char c,int x,int y){
            int k=0;
        if (x+v<=n&&y+v<=n&& x == y) {          //прямые диагонали//
//                            k = 1;

            while (k < v) {             //

                if (field[x+k][y+k] == c || field[x + k][y + k] == DOT_EMPTY) {
                    k++;
                    if (k == v)
                                return true;
                } else {
                    k = 1;

                    break;
                }
            }


        }
      return false;
    }

    private static boolean checkLineWithoutRivalFieldDiagonalReverse(char c,int x,int y) {
            int k=0;
        if ( x + y == n - 1) { //проверка обратной диагонали//(x+v <= n && y-v+1 >= 0  &&

            k = 0;

            while (k < v) {       //  &&i<=n-v&&j>=v-1&&j<=n-1

                if ((field[k][n- 1 - k] == DOT_AI) || (field[k][n-1-k] == DOT_EMPTY)) {
                    k++;
                    if (k == v)
                                return true;
                } else {
                    k = 1;
                    break;
                }

            }
        }

        return false;
    }

    private static boolean checkLineWithoutRivalFieldVertical(char c,int x,int y) {
       int k;
        if (x<=v) { //проверка вертикальных//&&x+v<=n
            k = 0;

            while (k < v) {        //  &&i<=n-v&&j>=v-1&&j<=n-1

                if ((field[k][y] == c) || (field[k][y] == DOT_EMPTY)) {
                    k++;
                    if (k == v)
                            return true;
                } else {
                    k = 1;

                    break;
                }

            }

        }

        return false;
    }
    private static boolean checkLineWithoutRivalFieldHorizontal(char c,int x,int y) {
        int k;
        if (y<=v) {         //&&y+v<=n                         //проверка горизонтальных
            k = 0;

            while (k < v) {         //  &&i<=n-v&&j>=v-1&&j<=n-1

                if ((field[x][k] == c) || (field[x][k] == DOT_EMPTY)) {
                    k++;
                    if (k == v)
                        return true;
                } else {
                    k = 1;
                    break;
                }

            }
        }

return false;
    }

    private static int[] checkDiagonalLineForPreWinSituation (char c,int ii,int jj) {
int line_without_one_empty=0;
int k=0;
int xx=0;
int yy=0;
int[] f = new int[2];
        if ((ii <= n - v && jj <= n - v )) { //проверка диагональ прямая&& ii == jj

            k = 0;


            while (k < v)
                if ((field[ii + k][jj + k] == c) || ((field[ii + k][jj + k] == DOT_EMPTY) && (line_without_one_empty == 0))) {

                    if (field[ii + k][jj + k] == DOT_EMPTY) {
                        line_without_one_empty = 1;
                        // kmax = k;
                        xx = ii + k;
                        f[0] = xx;
                        yy = jj + k;
                        f[1] = yy;
                    }
                    k++;

                } else {
                    k = 1;
                    line_without_one_empty = 0;
                    break;
                }

      //  }
            if (k == v && line_without_one_empty == 1) return f;
        }
        return null;
    }

private static int[] checkRevDiagonalLineForPreWinSituation(char c,int ii,int jj) {
    int k=0;
    int line_without_one_empty=0;
    int xx=0;
    int yy=0;
    int[] f = new int[2];
    if ((ii <= n - v)&&(jj-v+1>=0)) {

        k = 0;


        while (k < v) {       //  &&i<=n-v&&j>=v-1&&j<=n-1
            if ((field[ii + k][jj - k] == c) || ((field[ii + k][jj - k] == DOT_EMPTY) && (line_without_one_empty == 0))) {

                if (field[ii + k][jj - k] == DOT_EMPTY) {

                    line_without_one_empty = 1;
                    xx = ii + k;f[0]=xx;
                    yy = jj - k;f[1]=yy;
                }
                k++;
            } else {
                k = 1;
                break;
            }
            if (k == v && line_without_one_empty == 1)
                return f;
        }
    }
     return null;
}

    private static int[] checkVerticalLineForPreWinSituation(char c,int ii,int jj) {
        int k = 0;
        int line_without_one_empty=0;
        int xx = 0;
        int yy = 0;
        int[] f = new int[2];

        if ( ii <= n - v) { //проверка вертикальных
            k = 0;


            while (k < v) {

                if ((field[ii + k][jj] == c) || ((field[ii + k][jj] == DOT_EMPTY) && (line_without_one_empty == 0))) {

                    if (field[ii + k][jj] == DOT_EMPTY) { //kmax < k &&
                        //kmax = k;
                        line_without_one_empty = 1;
                        xx = ii + k; f[0]=xx;
                        yy = jj;   f[1]=yy;
                    }

                    k++;
                } else {
                    k = 1;
                    break;
                }

                if (k == v && line_without_one_empty == 1) {
                                return f;
                }
            }
        }

        return null;
    }

    private static int[] checkHorizontalLineForPreWinSituation(char c,int ii,int jj) {
        int k = 0;
        int line_without_one_empty=0;
        int xx = 0;
        int yy = 0;
        int[] f = new int[2];

        if ((jj <= n - v)) {                                  //проверка горизонтальных
            k = 0;


            while (k < v) {

                if ((field[ii][jj + k] == c) || ((field[ii][jj + k] == DOT_EMPTY) && (line_without_one_empty == 0))) {

                    if (field[ii][jj + k] == DOT_EMPTY) { //kmax < k &&
                        //kmax = k;
                        line_without_one_empty = 1;
                        xx = ii;f[0]=xx;
                        yy = jj + k;f[1]=yy;
                    }
                    k++;
                } else {
                    k = 1;
                    break;
                }

                if (k == v && line_without_one_empty == 1) {
                    return f;
                }
            }
        }

        return null;
    }

}
