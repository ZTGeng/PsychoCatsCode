import java.awt.Font;


public class Game1 extends Game{
    
    final static Font font1 = new Font("微软雅黑", Font.PLAIN, 32);
    final static Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
    
    public Game1() {
        super();
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
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
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                if (board[i][j] == 0) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                else StdDraw.setPenColor(StdDraw.ORANGE);
                StdDraw.filledCircle(getX(i, j), getY(i), 10);
            }
        }
        drawCat(getX(getCatPos() / 9, getCatPos() % 9), getY(getCatPos() / 9));
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
    
    public void inputAndClose() {
        int n = input();
        while (!tryClose(n / 9, n % 9)) {
            n = input();
        }
        count++;
    }
    
    public void endInfo() {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (catEscaped()) StdDraw.text(100, 182, "你竟然让猫跑了！");
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
    
    public static void main(String[] args) {
        
    }
    
}
