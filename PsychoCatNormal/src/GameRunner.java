


public class GameRunner {
    
    private Game g;
    
    public GameRunner () {
        g = new Game1();
    }
    
    private void run() {
        g.show();
        while (!g.catEscaped()) {
            g.inputAndClose();
            g.show(1000);
            if (!g.catTryMove()) break;
            g.show();
        }
        g.endInfo();
    }
    
    public static void main(String[] args) {
        GameRunner gr = new GameRunner();
        gr.run();
    }
    
}
