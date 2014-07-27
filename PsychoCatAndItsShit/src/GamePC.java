import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

public class GamePC{
    
    final static Font font1 = new Font("微软雅黑", Font.PLAIN, 32);
    final static Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
    
    private Random r;
    protected int[][] board;
    private Cat cat;
    protected int count;
    boolean getShit;

    public GamePC() {
        r = new Random();
        board = new int[9][9];
        cat = new Cat();
        count = 0;
        getShit = false;
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
        initBoard();
    }
    
    private double getX(int i, int j) {
        double offset = (i % 2 == 0) ? 0 : 10;
        return j * 20 + 15 + offset;
    }
    
    private double getY(int i) {
        return 155 - i * 17.5;
    }
    
    public void show(int t) {
        StdDraw.clear();
        StdDraw.setFont(font1);
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                if (board[i][j] <= 0) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                else StdDraw.setPenColor(StdDraw.ORANGE);
                StdDraw.filledCircle(getX(i, j), getY(i), 10);
                if (board[i][j] == 20) drawShit(getX(i, j), getY(i));
                else if (board[i][j] > 0 && board[i][j] < 10) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.text(getX(i, j), getY(i) - 2, String.valueOf(board[i][j]));
                }
            }
        }
        drawCat(getX(cat.getPos() / 9, cat.getPos() % 9), getY(cat.getPos() / 9));
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(175, 195, "已用：" + count + " 步");
        StdDraw.show(t);
    }
    
    public void show() {
        show(0);
    }
    
    private int input() {
        boolean isPressing = false;
        while (true) {
            StdDraw.show(40);
            if (StdDraw.mousePressed() && !isPressing) {
                isPressing = true;
                continue;
            }
            if (!StdDraw.mousePressed() && isPressing) {
                isPressing = false;
                double x = StdDraw.mouseX(), y = StdDraw.mouseY();
                if (y <= 5 || y >= 165 || x <= 5 || x >= 195) continue;
                int i, j;
                if (y >= 155) i = 0;
                else i = (int) ((155 - y) / 17.5);
                if (x <= 15) j = 0;
                else j = (int) ((x - 15) / 20);
                if (inCircle(x, y, i, j)) return i * 9 + j;
                if (i != 8 && inCircle(x, y, i + 1, j)) return (i + 1) * 9 + j;
                if (j != 8 && inCircle(x, y, i, j + 1)) return i * 9 + j + 1;
                if (i != 8 && j != 8 && inCircle(x, y, i + 1, j + 1)) return (i + 1) * 9 + j + 1;
                continue;
            }
        }
    }
    
    private boolean inCircle(double x, double y, int i, int j) {
        return (getX(i, j) - x) * (getX(i, j) - x) + (getY(i) - y) * (getY(i) - y) < 100;
    }
    
    public void inputAndClose() {
        int n = input();
        while (!tryClose(n / 9, n % 9)) {
            n = input();
        }
        if (board[n / 9][n % 9] == 20) getShit = true;
        count++;
    }
    
    public void endInfo() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == -20) board[i][j] = 20;
            }
        }
        show();
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (getShit) StdDraw.text(100, 182, "你踩到了猫屎！");
        else if (cat.escaped()) StdDraw.text(100, 182, "你竟然让猫跑了！");
        else StdDraw.text(100, 182, "你用了 " + count + " 步抓住了猫！");
        StdDraw.show();
    }
    
    private void drawCat(double x, double y) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledPolygon(new double[]{x,     x - 2.5, x + 2.5}, 
                              new double[]{y + 3, y - 9,   y - 9});
        StdDraw.filledCircle(x, y + 5, 2.5);
        StdDraw.filledPolygon(new double[]{x,     x - 2.5, x - 2.5}, 
                              new double[]{y + 5, y + 5,   y + 10});
        StdDraw.filledPolygon(new double[]{x,     x + 2.5, x + 2.5}, 
                              new double[]{y + 5, y + 5,   y + 10});
        StdDraw.setPenRadius(0.005);
        StdDraw.line(x - 2, y - 8, x - 5, y);
    }
    
    private void drawShit(double x, double y) {
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.filledEllipse(x + 2, y + 2, 1, 6);
        StdDraw.filledEllipse(x, y, 1, 6);
        StdDraw.filledEllipse(x - 2, y - 2, 1, 6);
    }
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + r.nextInt(5) + 3;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            tryShit(n / 9, n % 9);
        }
        initCloseNum = r.nextInt(5) + 3;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            if (board[n / 9][n % 9] != -20)
                if (!tryClose(n / 9, n % 9)) initCloseNum++;
        }
    }
    
    private boolean tryClose(int i, int j) {
        if (i * 9 + j == cat.getPos()) return false;
        if (board[i][j] <= 0) {
            if (board[i][j] == 0) board[i][j] = 10;
            else board[i][j] = -board[i][j];
            cat.close(i, j);
            return true;
            }
        return false;
    }
    
    private void tryShit(int i, int j) {
        if (i * 9 + j == cat.getPos()) return;
        if (board[i][j] != -20) {
            board[i][j] = -20;
            ArrayList<Integer> around = cat.around(i * 9 + j);
            for (int w : around) {
                board[w / 9][w % 9]--;
            }
            cat.close(i, j);
        }
    }
    
    public void run() {
        show();
        while (!cat.escaped()) {
            inputAndClose();
            show();
            if (getShit) break;
            show(1000);
            if (!cat.tryMove()) break;
            show();
        }
        endInfo();
    }
    
    
}
