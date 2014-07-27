import java.awt.Font;
import java.util.Random;



public class Game3{
    
    final static Font font1 = new Font("微软雅黑", Font.PLAIN, 32);
    final static Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
    
    private Random r;
    private int[][] board;
    private int count;
    int moreStep;
    private CatA cat1;
    private CatB cat2;
    
    public Game3() {
        r = new Random();
        board = new int[9][9];
        cat1 = new CatA();
        cat2 = new CatB();
        cat1.otherCat(cat2);
        cat2.otherCat(cat1);
        cat1.setPos(39);
        cat2.setPos(41);
        count = 0;
        moreStep = 7;
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
        initBoard();
    }
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + 3;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            if (!tryClose(n / 9, n % 9)) initCloseNum++;
        }
    }
    
    protected boolean tryClose(int i, int j) {
        if (i * 9 + j == cat1.getPos() || i * 9 + j == cat2.getPos()) return false;
        if (board[i][j] == 0) {
            board[i][j] = 10;
            cat1.close(i, j);
            cat2.close(i, j);
            return true;
        }
        return false;
    }
    
    public void show(int t) {
        StdDraw.clear();
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                if (board[i][j] == 0) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                else StdDraw.setPenColor(StdDraw.ORANGE);
                StdDraw.filledCircle(getX(i, j), getY(i), 10);
            }
        }
        drawCat(getX(cat1.getPos() / 9, cat1.getPos() % 9), getY(cat1.getPos() / 9));
        drawCat(getX(cat2.getPos() / 9, cat2.getPos() % 9), getY(cat2.getPos() / 9));
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(175, 195, "已用：" + count + " 步");
        StdDraw.text(175, 185, "剩余连击 " + moreStep + " 次");
        StdDraw.show(t);
    }
    
    public void show() {
        show(0);
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
    
    private double getX(int i, int j) {
        double offset = (i % 2 == 0) ? 0 : 10;
        return j * 20 + 15 + offset;
    }
    
    private double getY(int i) {
        return 155 - i * 17.5;
    }
    
    // 记录鼠标松开时的位置，计算其位于哪一个格子内，返回其序号（一个整数n）
    // 若鼠标并未位于任何格子内，重复直到输入有效
    private int input() {
        boolean isPressing = false;
        while (true) {
            StdDraw.show(40); // 每40毫秒扫描一次鼠标状况
            if (StdDraw.mousePressed() && !isPressing) { // 第一次按下鼠标
                isPressing = true;
                continue;
            }
            if (!StdDraw.mousePressed() && isPressing) { // 第一次放开鼠标
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
    
    // 若坐标点(x, y)位于序号为[i, j]的各自内部，返回true
    private boolean inCircle(double x, double y, int i, int j) {
        return (getX(i, j) - x) * (getX(i, j) - x) + (getY(i) - y) * (getY(i) - y) < 100;
    }
    
    public void inputAndClose() {
        int n = input();
        while (!tryClose(n / 9, n % 9)) { // 若此格已翻开或被猫占据，tryClose返回false
            n = input();
        }
    }
    
    public void endInfo() {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (cat1.escaped() || cat2.escaped()) StdDraw.text(75, 190, "你竟然让猫跑了！");
        else StdDraw.text(75, 190, "你用了 " + count + " 步抓住了猫！");
        StdDraw.show();
    }
    
    // 主程序代码
    public void run() {
        
        show();
        boolean cat1stuck = false;
        boolean cat2stuck = false;
        while (!cat1.escaped() && !cat2.escaped()) {
            inputAndClose();
            if (moreStep > 0) {
                show();
                inputAndClose();
                moreStep--;
            }
            count++;
            show(1000);
            if (cat1.tryMove()) cat1stuck = false;
            else cat1stuck = true;
            if (cat2.tryMove()) cat2stuck = false;
            else cat2stuck = true;
            if (cat1stuck && cat2stuck) break;
            show();
        }
        endInfo();
    }
    
    public static void main(String[] args) {
        Game3 g = new Game3();
        g.run();
    }
    
}
