import java.util.ArrayList;



public class Cat {
    
    private int pos;
    private ArrayList<ArrayList<Integer>> edges;
    private boolean stuck;

    public Cat() {
        pos = 40;
        edges = new ArrayList<ArrayList<Integer>>(81);
        stuck = false;
        int i, j;
        for (int n = 0; n < 81; n++) {
            edges.add(new ArrayList<Integer>(6));
            i = n / 9;
            j = n % 9;
            if (j != 0) edges.get(n).add(i * 9 + j - 1); // ��߸�
            if (i % 2 == 1) edges.get(n).add((i - 1) * 9 + j); // ���ϸ�
            else if (i != 0 && j != 0) edges.get(n).add((i - 1) * 9 + j - 1); // ���ϸ�
            if (i % 2 == 1 && j != 8) edges.get(n).add((i - 1) * 9 + j + 1); // ���ϸ�
            else if (i % 2 == 0 && i != 0) edges.get(n).add((i - 1) * 9 + j); // ���ϸ�
            if (j != 8) edges.get(n).add(i * 9 + j + 1); // �ұ߸�
            if (i % 2 == 1 && j != 8) edges.get(n).add((i + 1) * 9 + j + 1); // ���¸�
            else if (i % 2 == 0 && i != 8) edges.get(n).add((i + 1) * 9 + j); // ���¸�
            if (i % 2 == 1) edges.get(n).add((i + 1) * 9 + j); // ���¸�
            else if (i != 8 && j != 0) edges.get(n).add((i + 1) * 9 + j - 1); // ���¸�
        }
    }
    
    public int getPos() {
        return pos;
    }
    
    public boolean isStuck() {
        return stuck;
    }
    
//    // ������Game3�汾��
//    protected void setPos(int n) {
//        pos = n;
//    }
    
    public void tryMove() {
        int n = next(pos);
        if (n == pos) stuck = true;
        pos = n;
    }
    
    public void close(int i, int j) {
        int v = i * 9 + j;
        // ˫�����v���漰�ı�
        for (int w : edges.get(v)) {
            edges.get(w).remove(Integer.valueOf(v));
        }
        edges.get(v).clear();
    }
    
    private boolean escaped(int n) {
        return n < 9 || n > 71 || n % 9 == 0 || n % 9 == 8;
    }
    
    public boolean escaped() {
        return escaped(pos);
    }
    
    public int next(int n) {
        ArrayList<Integer> reachable = new ArrayList<Integer>();
        int[] orient = new int[81]; // ��¼���ܷ���ĵ�һ��
        for (int w : edges.get(n)) {
            if (escaped(w)) return w;
            reachable.add(w);
            orient[w] = w;
        }
        int num = 0;
        while (num < reachable.size()) {
            // ��reachable��ȡ����һ��num����������ӵĸ���
            int v = reachable.get(num++);
            for (int w : edges.get(v)) {
                if (escaped(w)) return orient[v];
                if (!reachable.contains(w)) {
                    reachable.add(w);
                    orient[w] = orient[v];
                }
            }
        }
        if (reachable.size() == 0) return n; // һ�����޷���
        return reachable.get(0); // �ѱ�Χס���Կɶ�
    }
    
    public ArrayList<Integer> around(int n) {
        return edges.get(n);
    }

//    public void run() {
//        
//        if (!stuck) {
//            tryMove();
//            System.out.println("Cat moved!!");
//            StdDraw.show();
//        }
//        
//        
////        while (!stuck) {
////            tryMove();
////            try {
////                this.wait();
////            } catch (InterruptedException e) {
////                System.out.println("Cat �� wait �������⣡");
////                e.printStackTrace();
////            }
////        }
//    }
    
}