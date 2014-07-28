

import java.awt.Font;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class PsyCatAndKnife {
    
    final static Font font1 = new Font("΢���ź�", Font.PLAIN, 32);
    final static Font font2 = new Font("΢���ź�", Font.PLAIN, 16);
    final static Font font3 = new Font("΢���ź�", Font.PLAIN, 8);
    
    private Random r;
    private int[][] board;
    private Cat cat;
    private int count;
    private double lastX;
    private TreeMap<Double, Double> knives;
    private boolean isDead;
    
    public PsyCatAndKnife() {
        r = new Random();
        board = new int[9][9];
        cat = new Cat();
        count = 0;
        StdDraw.setXscale(0, 200);
        StdDraw.setYscale(0, 200);
        lastX = 0;
        knives = new TreeMap<Double, Double>();
        isDead = false;
    }
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + r.nextInt(5) + 8;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            if (n != cat.getPos())
                tryClose(n / 9, n % 9);
        }
    }
    
    private double getX(int i, int j) {
        double offset = (i % 2 == 0) ? 0 : 10;
        return j * 20 + 15 + offset;
    }
    
    // �����˳��ص�λ��
    private double getY(int i) {
        return 185 - i * 17.5;
    }
    
    private void draw(int t) {
        StdDraw.clear();
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j ++) {
                drawCircle(i, j);
            }
        }
        if (isDead) {
            // ��è���ʱdrawDeadKnives()������drawDeadCat��
            drawDeadCat(cat.getPos() / 9, cat.getPos() % 9);
        } else {
            drawCat(cat.getPos() / 9, cat.getPos() % 9);
            drawDeadKnives();
        }
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(175, 30, "�ѳ���" + count + " �ѷɵ�");
        StdDraw.show(t);
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
    
    private void drawDeadCat(int i, int j) {
        double x = getX(i, j), y = getY(i);
        drawCircle(i, j);
        drawDeadKnives();
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        StdDraw.filledPolygon(new double[]{x - 7, x - 7,   x,     x + 7,   x + 7}, // �ڱ�
                              new double[]{y + 8, y + 1.5, y - 9, y + 1.5, y + 8});
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledPolygon(new double[]{x - 3  , x - 5,   x - 7}, // ����
                              new double[]{y + 1.5, y - 0.5, y + 1.5});
        StdDraw.filledPolygon(new double[]{x + 3, x + 5, x + 7}, // �ҳ��
                              new double[]{y + 1.5, y - 0.5, y + 1.5});
        StdDraw.filledPolygon(new double[]{x - 2.5, x + 2.5, x    },  // ����
                              new double[]{y + 1.5, y + 1.5, y - 9});
        StdDraw.filledCircle(x, y + 4, 2.5); // ͷ
        StdDraw.filledPolygon(new double[]{x,     x - 2.5, x - 2.5}, // ���
                              new double[]{y + 4, y + 4,   y + 8});
        StdDraw.filledPolygon(new double[]{x,     x + 2.5, x + 2.5}, // �Ҷ�
                              new double[]{y + 4, y + 4,   y + 8});
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setPenRadius(0.002);
        StdDraw.ellipse(x, y + 8.5, 2.5, 1); // �⻷
        StdDraw.setFont(font3);
        StdDraw.text(x - 1.5, y + 5, "x"); // ����
        StdDraw.text(x + 1.5, y + 5, "x"); // ����
    }
    
    private void drawKnife(double x, double y) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledPolygon(new double[]{x + 2.5, x + 2.5, x,      x + 0.5, x - 2.5, x - 2.5},
                              new double[]{y + 10,  y - 10,  y - 10, y - 5,   y - 5,   y + 5});
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.arc(x + 2.5, y + 5, 5, 90, 180);
        StdDraw.line(x + 2.5, y - 5, x + 2.5, y + 10);
        StdDraw.line(x - 2.5, y - 5, x - 2.5, y + 5);
        StdDraw.polygon(new double[]{x - 2.5, x + 2.5, x + 2.5, x,      x + 0.5},
                        new double[]{y - 5,   y - 5,   y - 10,  y - 10, y - 5});
    }
    
    private void drawDeadKnife(double x, double y) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledPolygon(new double[]{x + 0.5, x + 10,  x + 10, x + 5,   x + 5,   x - 0.5},
                              new double[]{y + 2.5, y + 2.5, y,      y + 0.5, y - 2.5, y - 2.5});
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(new double[]{x + 0.5, x + 10,  x + 10, x + 5,   x + 5,   x + 5,   x - 0.5},
                        new double[]{y + 2.5, y + 2.5, y,      y + 0.5, y + 2.5, y - 2.5, y - 2.5});
    }
    
    private void drawDeadKnives() {
        for (double x : knives.keySet()) {
            drawDeadKnife(-x, knives.get(x));
        }
    }
    
    private void drawPower(double x, double y, double time) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x, y + 2.5, 25, 2.5);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledRectangle(x + 25 - time / 40, y + 2.5, time / 40, 2.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.002);
        StdDraw.rectangle(x, y + 2.5, 25, 2.5);
        double mark = x + 20 - (y - 35) / 4;
        StdDraw.line(mark, y - 0.5, mark, y + 5.5);
    }
    
    private boolean tryClose(int i, int j) {
        if (i * 9 + j == cat.getPos()) {
            isDead = true;
            return false;
        }
        if (board[i][j] <= 0) {
            board[i][j] = 10;
            cat.close(i, j);
            return true;
        }
        return false;
    }
    
    protected int inputAndClose() {
        int n = input();
        // ��û�������κθ��ӣ�����Ϸ������ֱ�ӷ���
        if (n == -1) {
            draw();
            return n;
        }
        tryClose(n / 9, n % 9);
        draw();
        return n;
    }
    
    private int input() {
        boolean isPressing = false;
        long startTime = 0;
        double x = 0, y = 0;
        while (!cat.isStuck() && !cat.escaped()) {
            StdDraw.show(40);
            if (!StdDraw.mousePressed() && !isPressing) { // δ�������
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledRectangle(lastX, 15, 3, 11);
                lastX = StdDraw.mouseX();
                drawKnife(lastX, 15);
                continue;
            }
            if (StdDraw.mousePressed() && !isPressing) { // �ոհ������
                isPressing = true;
                x = StdDraw.mouseX();
                y = StdDraw.mouseY();
                startTime = System.currentTimeMillis();
                continue;
            }
            if (StdDraw.mousePressed() && isPressing) { // �����������
                drawPower(x, y, (double) (System.currentTimeMillis() - startTime));
                continue;
            }
            if (!StdDraw.mousePressed() && isPressing) { // �ոշſ����
                // ʵ��yͨ��power����ó�
//                double realY = (double) (System.currentTimeMillis() - startTime) / 5 + 15;
                // ����40����������ӳټ��
                double realY = (double) (System.currentTimeMillis() - startTime - 20) / 5 + 15;
                if (startTime == 0) realY = 0;
                startTime = 0;
                isPressing = false;
                count++;
                
                while (knives.containsKey(-x))
                    x += 0.0000025;
                knives.put(-x, realY);
                
                // �ɵ�����
                if (realY > 115) {
                    drawKnife(x, (realY - 15) / 3);
                    StdDraw.show(100);
                    draw();
                    drawKnife(x, 15);
                    drawKnife(x, (realY - 15) / 3 * 2);
                    StdDraw.show(100);
                    draw();
                } else if (realY > 45) {
                    drawKnife(x, (realY - 15) / 2);
                    StdDraw.show(100);
                    draw();
                }
                
                // ����ͨ��[x, realY]����[i, j]
                int i, j;
                if (realY >= 185) i = 0;
                else if (realY <= 45) i = 8;
                else i = (int) ((185 - realY) / 17.5);
                if (x <= 15) j = 0;
                else if (x >= 185) j = 8;
                else j = (int) ((x - 15) / 20);
                if (inCircle(x, realY, i, j)) return i * 9 + j;
                if (i != 8 && inCircle(x, realY, i + 1, j)) return (i + 1) * 9 + j;
                if (j != 8 && inCircle(x, realY, i, j + 1)) return i * 9 + j + 1;
                if (i != 8 && j != 8 && inCircle(x, realY, i + 1, j + 1)) return (i + 1) * 9 + j + 1;
                // û�������κθ��ӣ�����-1
                return -1;
            }
        }
        // è��ס�������ѡ�����������Ϸ����������-1
        return -1;
    }
    
    private boolean inCircle(double x, double y, int i, int j) {
        return (getX(i, j) - x) * (getX(i, j) - x) + (getY(i) - y) * (getY(i) - y) < 100;
    }
    
    private void endInfo() {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setFont(font1);
        if (cat.escaped()) StdDraw.text(100, 15, "�㾹Ȼ��è���ˣ�");
        else if (isDead) StdDraw.text(100, 15, "������ɱ����è��");
        else StdDraw.text(100, 15, "������ " + count + " ��ץס��è��");
        StdDraw.show();
    }
    
    public void run() {
        CatRun catRun = new CatRun();
        Timer timer = new Timer();
        
        initBoard();
        draw();
        // ��ʼ����ʱ��ʾ
        int count = 3;
        StdDraw.setFont(font1);
        while (count > 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setFont(font1);
            StdDraw.text(100, 150, "����ʱ��" + count--);
            StdDraw.show(1000);
            draw();
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(font1);
        StdDraw.text(100, 150, "��ʼ��");
        // ����ʱ����
        timer.schedule(catRun, 3000, 2500);
        while (!cat.escaped() && !cat.isStuck() && !isDead) {
            inputAndClose();
        }
        endInfo();
    }
    
    private class CatRun extends TimerTask{
        @Override
        public void run() {
            if (!cat.isStuck() && !cat.escaped() && !isDead) {
                int n = cat.getPos();
                drawCircle(n / 9, n % 9);
                
                cat.tryMove();
                
                n = cat.getPos();
                drawCat(n / 9, n % 9);
            }
        }
    }
    
    public static void main(String[] args) {
        
        PsyCatAndKnife g = new PsyCatAndKnife();
        g.run();
//        g.drawKnife(100, 100);
    }
    
}
