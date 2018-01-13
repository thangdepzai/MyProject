
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thang
 */
public class Dijsktra {

    int Matrix[][];
    private ArrayList<Integer> vertices;
    private HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
    private double[] D;
    private int[] P;
    private int[] K;
    private int num;
    private Double[][] distances;

    public Dijsktra(int[][] Matrix, ArrayList<Integer> vertices, Double[][] distances) {
        this.Matrix = Matrix;
        this.vertices = vertices;
        this.num = vertices.size();
        this.distances = distances;
        D = new double[num];
        P = new int[num];
        K = new int[num];
        for (int i = 0; i < vertices.size(); i++) {
            hash.put(i, vertices.get(i));
            D[i] = Double.MAX_VALUE;
            P[i] = 0;
            K[i] = 0;
        }
    }

    public Dijsktra(ArrayList<Integer> vertices, Double[][] distances) {
        this.vertices = vertices;
        this.num = vertices.size();
        this.distances = distances;
        D = new double[num];
        P = new int[num];
        K = new int[num];
        for (int i = 0; i < vertices.size(); i++) {
            hash.put(i, vertices.get(i));
            D[i] = Double.MAX_VALUE;
            P[i] = 0;
            K[i] = 0;
        }
    }

    public Dijsktra(ArrayList<Integer> vertices, Double[][] distances, int[][] Matrix) {
        this.vertices = vertices;
        this.num = vertices.size();
        this.distances = distances;
        this.Matrix = Matrix;
        D = new double[num];
        P = new int[num];
        K = new int[num];
        for (int i = 0; i < vertices.size(); i++) {
            hash.put(i, vertices.get(i));
            D[i] = Double.MAX_VALUE;
            P[i] = 0;
            K[i] = 0;
        }
        Main.flag = true;
    }

//------------------------------- Cau truc Heap ----------------------------------------   
    private int parent(int i) {
        return (i + 1) / 2 - 1;
    }

    private int lChild(int i) {
        int x = i + 1;
        int y = x * 2;
        return y - 1;
    }

    private int rChild(int i) {
        int x = i + 1;
        int y = x * 2 + 1;
        return y - 1;
    }

    private void Build_Min_Heap(ArrayList<Double> Q, Map<Integer, Integer> pos) {
        int n = Q.size();
        for (int i = n / 2 - 1; i > -1; i--) {
            Min_Heapify(Q, i, n, pos);
        }
    }

    private void Min_Heapify(ArrayList<Double> A, int i, int n, Map<Integer, Integer> pos) {
        int l = lChild(i);
        int r = rChild(i);
        int minimum;
        if (l < n && A.get(l) < A.get(i)) {
            minimum = l;
        } else {
            minimum = i;
        }

        if (r < n && A.get(r) < A.get(minimum)) {
            minimum = r;
        }

        if (minimum != i) {

            double tmp = A.get(minimum);
            A.set(minimum, A.get(i));
            A.set(i, tmp);
            int tmp1 = pos.get(i);
            pos.put(i, pos.get(minimum));
            pos.put(minimum, tmp1);
            Min_Heapify(A, minimum, n, pos);
        }

    }

    private int Extract_Min(ArrayList<Double> Q, Map<Integer, Integer> pos) {
        int n = Q.size();
        int k = pos.get(0);
        Q.set(0, Q.get(n - 1));
        int tmp = pos.get(n - 1);
        pos.put(n - 1, pos.get(0));
        pos.put(0, tmp);
        Q.remove(n - 1);
        Min_Heapify(Q, 0, n - 1, pos);
        return k;
    }

    private void Decrease_Key(ArrayList<Double> Q, int m, double d, Map<Integer, Integer> pos) {
        Q.set(m, d);
        while (m > 0) {
            if (Q.get(parent(m)) > Q.get(m)) {
                int c = parent(m);
                double tmp = Q.get(c);
                Q.set(c, Q.get(m));
                Q.set(m, tmp);
                int tmp1 = pos.get(m);
                pos.put(m, pos.get(c));
                pos.put(c, tmp1);
                m = c;

            } else {
                break;
            }
        }

    }
///-------------------------------------------Ham RUN----------------------------------------

    public HashMap<Integer, Double> run(int start) {
        int s = 0;
        for (int i : hash.keySet()) {
            if (hash.get(i) == start) {
                D[i] = 0;
                s = i;
                // System.out.println("start " + i);
                break;
            }
        }
        ArrayList<Double> Q = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Q.add(0.0);
        }

        int j = 0;
        Map<Integer, Integer> pos = new HashMap<Integer, Integer>();
        for (int i : hash.keySet()) {
            Q.set(j, D[i]);
            pos.put(j, i);
            j++;
        }
        Build_Min_Heap(Q, pos);
        while (Q.size() != 0) {
            int u = Extract_Min(Q, pos);
            K[u] = 1;
            ArrayList<Integer> Keof_u = DsKe(u);
            for (int v : Keof_u) {
                if (K[v] == 0 && D[v] > D[u] + distances[hash.get(u) - 1][hash.get(v) - 1]) {
                    D[v] = D[u] + distances[hash.get(u) - 1][hash.get(v) - 1];
                    P[v] = u;
                    int m = 0;
                    for (int i : pos.keySet()) {
                        if (pos.get(i) == v) {
                            m = i;
                            break;
                        }
                    }
                    Decrease_Key(Q, m, D[v], pos);
                }
            }

        }

        ArrayList<Integer> T = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T.add(i);
        }
        T.remove(T.indexOf(s));
        HashMap<Integer, Double> result = new HashMap<Integer, Double>();
        result.put(hash.get(s), 0.0);
        for (int i : T) {
            if (Main.flag) {
                Matrix[hash.get(i) - 1][hash.get(P[i]) - 1] = Matrix[hash.get(P[i]) - 1][hash.get(i) - 1] = 1;
            }
            //  System.out.println(" " + hash.get(P[i]) + " " + hash.get(i));
            result.put(hash.get(i), D[i]);
        }
        return result;

    }
    public HashMap<Integer, Double> run2(int start) {
        int s = 0;
        for (int i : hash.keySet()) {
            if (hash.get(i) == start) {
                D[i] = 0;
                s = i;
                // System.out.println("start " + i);
                break;
            }
        }
        ArrayList<Double> Q = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Q.add(0.0);
        }

        int j = 0;
        Map<Integer, Integer> pos = new HashMap<Integer, Integer>();
        for (int i : hash.keySet()) {
            Q.set(j, D[i]);
            pos.put(j, i);
            j++;
        }
        Build_Min_Heap(Q, pos);
        while (Q.size() != 0) {
            int u = Extract_Min(Q, pos);
            K[u] = 1;
            ArrayList<Integer> Keof_u = DsKe2(u);
            for (int v : Keof_u) {
                if (K[v] == 0 && D[v] > D[u] + distances[hash.get(u) - 1][hash.get(v) - 1]) {
                    D[v] = D[u] + distances[hash.get(u) - 1][hash.get(v) - 1];
                    P[v] = u;
                    int m = 0;
                    for (int i : pos.keySet()) {
                        if (pos.get(i) == v) {
                            m = i;
                            break;
                        }
                    }
                    Decrease_Key(Q, m, D[v], pos);
                }
            }

        }

        ArrayList<Integer> T = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T.add(i);
        }
        T.remove(T.indexOf(s));
        HashMap<Integer, Double> result = new HashMap<Integer, Double>();
        result.put(hash.get(s), 0.0);
        double sum =0;
        for (int i : T) {
            if (Main.flag) {
                Matrix[hash.get(i) - 1][hash.get(P[i]) - 1] = Matrix[hash.get(P[i]) - 1][hash.get(i) - 1] = 1;
            }
            //  System.out.println(" " + hash.get(P[i]) + " " + hash.get(i));
            result.put(hash.get(i), D[i]);
            sum += D[i];
        }
        System.out.println(sum);
        return result;

    }

    private ArrayList<Integer> DsKe2(int v) {
        ArrayList<Integer> Ke = new ArrayList<Integer>();
        for (int i = 0; i < num; i++) {
            if (Matrix[hash.get(v) - 1][hash.get(i) - 1] == 1) {
                Ke.add(i);
            }
        }
        return Ke;
    }

    private ArrayList<Integer> DsKe(int v) {
        ArrayList<Integer> Ke = new ArrayList<Integer>();
        for (int i = 0; i < num; i++) {
            if (i != v && distances[hash.get(v) - 1][hash.get(i) - 1] != Double.MAX_VALUE) {
                Ke.add(i);

            }
        }
        return Ke;
    }

    private void clear() {
        for (int i = 0; i < vertices.size(); i++) {
            D[i] = Double.MAX_VALUE;
            P[i] = 0;
            K[i] = 0;
        }
    }

}
