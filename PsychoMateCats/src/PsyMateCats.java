import java.awt.Font;
import java.util.Random;




public class PsyMateCats {
    
    final static Font font1 = new Font("΢���ź�", Font.PLAIN, 32);
    final static Font font2 = new Font("΢���ź�", Font.PLAIN, 16);
    
    private Random r;
    private int[][] board;
    private int count;
    private CatEstrus cat1;
    private CatEstrus cat2;
    
    public PsyMateCats() {
        r = new Random();
        board = new int[9][9];
        count = 0;
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
        cat1 = new CatEstrus(1, 10);
        cat2 = new CatEstrus(0, 70);
        cat1.otherCat(cat2);
        cat2.otherCat(cat1);
    }
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + r.nextInt(5) + 8;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            tryClose(n / 9, n % 9);
        }
    }
    
    private double getX(int i, int j) {
        double offset = (i % 2 == 0) ? 0 : 10;
        return j * 20 + 15 + offset;
    }
    
    private double getY(int i) {
        return 155 - i * 17.5;
    }
    
    private void draw() {
        draw(0);
    }
    
    private void drawCat(int i, int j) {
        double x = getX(i, j), y = getY(i);
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
        if (board[i][j] <= 0) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        else StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.filledCircle(getX(i, j), getY(i), 10);
    }
    
    private int inputAndClose() {
        int n;
        do {
            n = input();
            if (n == -1) return -1;
        } while (!tryClose(n / 9, n % 9));
        count++;
        return n;
    }
    
    private int input() {
        boolean isPressing = false;
        while (true) {
            StdDraw.show(40);
            if (StdDraw.mousePressed() && !isPressing) { // ��һ�ΰ������
                isPressing = true;
                continue;
            }
            if (!StdDraw.mousePressed() && isPressing) { // ��һ�ηſ����
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
            }
        }
    }
    
    private boolean inCircle(double x, double y, int i, int j) {
        return (getX(i, j) - x) * (getX(i, j) - x) + (getY(i) - y) * (getY(i) - y) < 100;
    }
    
    private boolean catsMeet() {
        return cat1.meet() || cat2.meet();
    }
    
    private boolean canMeet() {
        return cat1.canMeet() && cat2.canMeet();
    }
    
    private boolean tryClose(int i, int j) {
        if (i * 9 + j == cat1.getPos() || i * 9 + j == cat2.getPos()) return false;
        if (board[i][j] == 0) {
            board[i][j] = 10;
            cat1.close(i, j);
            cat2.close(i, j);
            return true;
        }
        return false;
    }
    
    private void draw(int t) {
        StdDraw.clear();
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                drawCircle(i, j);
            }
        }
        if (catsMeet())
            drawMateCats(cat1.getPos() /9, cat1.getPos() % 9);
        else {
            drawCat(cat1.getPos() / 9, cat1.getPos() % 9);
            drawCat(cat2.getPos() / 9, cat2.getPos() % 9);
        }
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(175, 195, "���ã�" + count + " ��");
        StdDraw.show(t);
    }
    
    private void drawMateCats(int i, int j) {
        double x = getX(i, j), y = getY(i);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(x, y + 2.5, 2.5);
        StdDraw.filledPolygon(new double[]{x,       x - 2.5, x - 2.5}, 
                              new double[]{y + 2.5, y + 2.5, y + 7.5});
        StdDraw.filledPolygon(new double[]{x,       x + 2.5, x + 2.5}, 
                              new double[]{y + 2.5, y + 2.5, y + 7.5});
        StdDraw.filledCircle(x + 5.2, y, 2.5);
        StdDraw.filledPolygon(new double[]{x + 5.2,   x + 2.7, x + 2.7}, 
                              new double[]{y, y, y + 5});
        StdDraw.filledPolygon(new double[]{x + 5.2,   x + 7.7, x + 7.7}, 
                              new double[]{y, y, y + 5});
        
        StdDraw.filledPolygon(new double[]{x, x,     x - 7}, 
                              new double[]{y, y - 7, y - 7});
        StdDraw.filledPolygon(new double[]{x + 5.2, x + 5.2, x - 7}, 
                              new double[]{y - 2.5, y - 7.5, y - 7.5});
    }
    
    protected void endInfo() {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (catsMeet()) StdDraw.text(100, 182, "��Ҫ��һ��Сè�ˣ�");
        else if (cat1.escaped() || cat2.escaped()) StdDraw.text(100, 182, "�㾹Ȼ��è���ˣ�");
        else StdDraw.text(100, 182, "������ " + count + " ��ץס��è��");
        StdDraw.show();
    }
    
    // ���������
    public void run() {
        initBoard();
        draw();
        boolean cat1stuck = false;
        boolean cat2stuck = false;
        // ��������è������������δ���������
        while (canMeet() && !catsMeet()) {
            inputAndClose();
            draw(1000);
            cat1.tryMove();
            if (cat1.isStuck()) cat1stuck = true;
            else cat1stuck = false;
            draw(200);
            if (catsMeet()) break;
            cat2.tryMove();
            if (cat2.isStuck()) cat2stuck = true;
            else cat2stuck = false;
            if (cat1stuck && cat2stuck) break;
            draw();
        }
        // whileѭ����ֹԭ�����������è����������һ������è������������ʱ��Ϸ����
        // ע��һè����è���ܴ��ڡ��ٱ�����״̬
        // ���ٱ�����ֻ���ܷ����ڣ�è������ʱǡ�ô�������λ�ã��������ƶ��������Ϊ����
        // �����Ҫͬʱ�ж��Ƿ���è����
        // ���ڽ��endInfo()��ע��Ӧ����è���ѵ����ȼ�����ë����
        if (canMeet() || cat1.escaped() || cat2.escaped()) {
            endInfo();
            return;
        }
        // �����ֹԭ������è������������ڶ������
        while (!cat1.escaped() && !cat2.escaped()) {
            inputAndClose();
            count++;
            draw(1000);
            cat1.tryMove();
            if (cat1.isStuck()) cat1stuck = true;
            else cat1stuck = false;
            draw(200);
            if (catsMeet()) break;
            cat2.tryMove();
            if (cat2.isStuck()) cat2stuck = true;
            else cat2stuck = false;
            if (cat1stuck && cat2stuck) break;
            draw();
        }
        endInfo();
    }
    
    public static void main(String[] args) {
        PsyMateCats g = new PsyMateCats();
        g.run();
    }
    
}
