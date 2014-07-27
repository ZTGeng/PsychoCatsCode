import java.awt.Font;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Game{
    
    final static Font font1 = new Font("微软雅黑", Font.PLAIN, 32);
    final static Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
    
    private Random r;
    private int[][] board;
    private Cat cat;
    private int count;
    
    public Game() {
        r = new Random();
        board = new int[9][9];
        cat = new Cat();
        count = 0;
        initBoard();
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
    }
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + r.nextInt(5) + 8;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            tryClose(n / 9, n % 9);
        }
    }
    
    private boolean tryClose(int i, int j) {
        if (i * 9 + j == cat.getPos()) return false;
        if (board[i][j] == 0) {
            board[i][j] = -1;
            cat.close(i, j);
            return true;
        }
        return false;
    }
    
    private double getX(int i, int j) {
        double offset = (i % 2 == 0) ? 0 : 10;
        return j * 20 + 15 + offset;
    }
    
    private double getY(int i) {
        return 155 - i * 17.5;
    }
    
    public void draw(int t) {
        StdDraw.clear();
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                drawCircle(i, j);
            }
        }
        drawCat(getX(cat.getPos() / 9, cat.getPos() % 9), getY(cat.getPos() / 9));
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(175, 195, "已用：" + count + " 步");
        StdDraw.show(t);
    }
    
    public void draw() {
        draw(0);
    }
    
    private int input() {
        boolean isPressing = false;
        while (!cat.isStuck() && !cat.escaped()) {
            StdDraw.show(30);
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
//                continue;
            }
        }
        return -1;
    }
    
    public void inputAndClose() {
        int n;
        do {
            n = input();
            if (n == -1) return;
        } while (!tryClose(n / 9, n % 9));
        count++;
    }
    
    public void endInfo() {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (cat.escaped()) StdDraw.text(100, 182, "你竟然让猫跑了！");
        else StdDraw.text(100, 182, "你用了 " + count + " 步抓住了猫！");
        StdDraw.show();
    }
    
    private boolean inCircle(double x, double y, int i, int j) {
        return (getX(i, j) - x) * (getX(i, j) - x) + (getY(i) - y) * (getY(i) - y) < 100;
    }
    
    /**
     * 猫由身体（三角形）、头（圆形）、左耳（三角形）、右耳（三角形）构成
     * 已知中心坐标(x, y)
     * 身体三角形：(x, y + 3)、(x - 2.5, y - 9)、(x + 2.5, y - 9)
     * 头：圆心(x, y + 5)，半径2.5
     * 左耳三角形：(x, y + 5)、(x - 2.5, y + 5)、(x - 2.5, y + 10)
     * 右耳三角形：(x, y + 5)、(x + 2.5, y + 5)、(x + 2.5, y + 10)
     */
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
    
    private void drawCircle(int i, int j) {
        if (board[i][j] == 0) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        else StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.filledCircle(getX(i, j), getY(i), 10);
    }
    
    public void run() {
        
        CatRun catRun = new CatRun(cat);
        Timer timer = new Timer();
        
        draw();
        // 开始倒计时提示
        int count = 3;
        StdDraw.setFont(font1);
        while (count > 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(100, 180, "倒计时：" + count--);
            StdDraw.show(1000);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.filledRectangle(100, 180, 50, 15);
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(100, 180, "开始！");
        // 倒计时结束
        timer.schedule(catRun, 1000, 600);
        while (!cat.escaped() && !cat.isStuck()) {
            inputAndClose();
            draw();
        }
        endInfo();
    }
    
    public class CatRun extends TimerTask{

        private Cat cat;
        
        public CatRun (Cat c) {
            cat = c;
        }
        
        @Override
        public void run() {
            if (!cat.isStuck() && !cat.escaped()) {
                int n = cat.getPos();
                drawCircle(n / 9, n % 9);
                
                cat.tryMove();
                
                n = cat.getPos();
                drawCat(getX(n / 9, n % 9), getY(n / 9));
            }
        }
        
    }
    
    public static void main(String[] args) {
        
        Game g = new Game();
        g.run();
    }
    
}
