//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author thang
// */
//public class Test {
//    static int NUM_CITY = 0;
//    static int NUMBER_OF_CLUSTERS = 0;
//    static int SOURCE_VERTEX = 0;
//    static List<List<Integer>> ListClusterCity = new ArrayList<List<Integer>>();
//    static int seed = 0;
//    public static int sizePopulation = 100;
//    static City[] cities;
//    static Double[][] distances;
//    static Random rand;
//    static int numFitness = 0;
//    static String filename = "10berlin52.clt";
//    static double plc = 0.05;
//    static String type =null;
//    static int dem=0;
//    
//    
//    public static void main(String[] args) {
//        readFile();
//        int [] A= {32, 27, 42, 16, 15, 9, 33, 8, 12, 17};
//        ArrayList<Integer> gene = new ArrayList<Integer>();
//        for(int i :A){
//            gene.add(i);
//        }
//        int[][] Matrix = new int[NUM_CITY][NUM_CITY];
//        for(int i=0;i<gene.size();i++){
//            Dijsktra al = new Dijsktra((ArrayList<Integer>) ListClusterCity.get(i),distances,Matrix);
//            al.run(gene.get(i));
//        }
//        Dijsktra al = new Dijsktra(gene,distances,Matrix);
//        al.run(32);
//        System.out.println(dem);
//        for(int i=0;i<NUM_CITY;i++){
//            for(int j=0;j<NUM_CITY;j++){
//                System.out.print(Matrix[i][j]);
//            }
//            System.out.println("");
//        }
//        
//    }
//    public static void readFile(){
//        BufferedReader br = null;
//        try {
//            String sCurrentLine = null;
//            br = new BufferedReader(new FileReader(filename));
//            String[] str = null;
//            while (true) {
//                sCurrentLine = br.readLine();
//                str = sCurrentLine.split(": ");
//                if (str[0].equals("DIMENSION ") || str[0].equals("DIMENSION")) {
//                    NUM_CITY = Integer.parseInt(str[1]);
//                } else if (str[0].equals("NUMBER_OF_CLUSTERS") || str[0].equals("NUMBER_OF_CLUSTERS ")) {
//                    NUMBER_OF_CLUSTERS = Integer.parseInt(str[1]);
//                    break;
//                }
//            }
//            distances = new Double[NUM_CITY][NUM_CITY];
//            for (int j = 0; j < 2; j++) {
//                sCurrentLine = br.readLine();
//            }
//            str = sCurrentLine.split(" ");
//            if (str[0].equals("NODE_COORD_SECTION") || str[0].equals("NODE_COORD_SECTION:")) {
//                cities = new City[NUM_CITY];
//                for (int j = 0; j < NUM_CITY; j++) {
//                    sCurrentLine = br.readLine();
//                    str = sCurrentLine.split(" "); // split by space
//                    ArrayList<String> data = new ArrayList<>();
//                    for (int i = 0; i < str.length; i++) {
//                        if (!str[i].isEmpty()) {
//                            data.add(str[i]);
//                        }
//                    }
//
//                    cities[j] = new City(Double.parseDouble(data.get(1)), Double.parseDouble(data.get(2)));
//                    for (int i = 0; i <= j; i++) {
//                        if (i == j) {
//                            distances[j][i] = 0.0;
//                        } else {
//                            distances[j][i] = distances[i][j] = Math.sqrt(Math.pow((cities[j].getX() - cities[i].getX()), 2)
//                                    + Math.pow((cities[j].getY() - cities[i].getY()), 2));
//                        }
//                    }
//
//                }
//            } else if (str[0].equals("EDGE_WEIGHT_SECTION:") || str[0].equals("EDGE_WEIGHT_SECTION")) {
//                for (int j = 0; j < NUM_CITY; j++) {
//                    sCurrentLine = br.readLine();
//                    str = sCurrentLine.split(" "); // split by space
//                    ArrayList<String> data = new ArrayList<>();
//                    for (int i = 0; i < str.length; i++) {
//                        if (!str[i].isEmpty()) {
//                            data.add(str[i]);
//                        }
//                    }
//                    for (int k = j; k < NUM_CITY; k++) {
//                        if (k == j) {
//                            distances[k][j] = distances[j][k] = 0.0;
//                        } else {
//                            distances[j][k] = distances[k][j] = Double.parseDouble(data.get(k));
//                        }
//                    }
//                }
//            }
//            for (int j = 0; j < 2; j++) {
//                sCurrentLine = br.readLine();
//            }
//            str = sCurrentLine.split(": ");
//            SOURCE_VERTEX = Integer.parseInt(str[1]);
//            for (int j = 0; j < NUMBER_OF_CLUSTERS; j++) {
//                sCurrentLine = br.readLine();
//                str = sCurrentLine.split(" ");
//                List<Integer> L = new ArrayList<>();
//                for (int k = 1; k < str.length - 1; k++) {
//                    L.add(Integer.parseInt(str[k]) + 1);
//                }
//                ListClusterCity.add(L);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//    }
//
//    
//}
