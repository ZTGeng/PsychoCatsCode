import java.util.Random;



public class Game{
    
    private Random r;
    protected int[][] board;
    protected Cat cat;
    protected int count;
    
    

    public Game() {
        r = new Random();
        board = new int[9][9];
        cat = new Cat();
        count = 0;
        initBoard();
    }
    
    public void draw(int t) {}
    public void draw() {}
    public void inputAndClose() {}
    public void endInfo() {}
    
    private void initBoard() {
        int initCloseNum = r.nextInt(5) + r.nextInt(5) + 8;
        for (; initCloseNum > 0; initCloseNum--) {
            int n = r.nextInt(81);
            tryClose(n / 9, n % 9);
        }
    }
    
    
//    public int getCatPos() {
//        return cat.getPos();
//    }
    
    protected boolean tryClose(int i, int j) {
        if (i * 9 + j == cat.getPos()) return false;
        if (board[i][j] == 0) {
            board[i][j] = -1;
            cat.close(i, j);
            return true;
        }
        return false;
    }
    
//    protected boolean catTryMove() {
//        return cat.tryMove();
//    }
//    
//    public boolean catEscaped() {
//        return cat.escaped();
//    }
    
}
