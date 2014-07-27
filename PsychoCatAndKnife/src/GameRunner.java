


public class GameRunner {
    
    private Game g;
    
    public GameRunner (int mode) {
        if (mode == 0) g = new Game();
        else if (mode == 1) g = new Game();
    }
    
    private void run() {   
//        g.draw();
//        while (!g.cat.escaped()) {
//            g.inputAndClose();
//            g.draw(1000);
//            if (g.cat.isStuck()) break;
//            g.draw();
//        }
//        g.endInfo();
    }
    
    public static void main(String[] args) {
        GameRunner gr = new GameRunner(1);
        gr.run();
    }
    
}
