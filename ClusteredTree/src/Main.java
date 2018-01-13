
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thang
 */
public class Main {

    static int NUM_CITY = 0;
    static int NUMBER_OF_CLUSTERS = 0;
    static int SOURCE_VERTEX = 0;
    static List<List<Integer>> ListClusterCity = new ArrayList<List<Integer>>();
    static int seed = 0;
    public static int sizePopulation = 100;
    static City[] cities;
    static Double[][] distances;
    static Random rand;
    static int numFitness = 0;
    static String filename = null;
    static double plc = 0.05;
    static String type = null;
    static boolean flag = false;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        filename = args[0];
        seed = Integer.parseInt(args[1]);
        rand = new Random(seed);
        //  plc = Double.parseDouble(args[2]);
        numFitness = Integer.parseInt(args[2]);
        sizePopulation = Integer.parseInt(args[3]);
        plc = Double.parseDouble(args[4]);
        type = args[5];
        //  lc = Integer.parseInt(args[6]);
        /**
         * ************************************************************************
         * read file
         */
        readFile();

        // GA
        GA ga = new GA();

        Individual best = ga.Run(numFitness, 0.5, 0.05, plc);
        long end = System.currentTimeMillis();
        int[][] Matrix = new int[NUM_CITY][NUM_CITY];
        for (int i = 0; i < best.getGene().size(); i++) {
            Dijsktra al = new Dijsktra((ArrayList<Integer>) ListClusterCity.get(i), distances, Matrix);
            al.run(best.getGene().get(i));
        }
        Dijsktra al = new Dijsktra(best.getGene(), distances, Matrix);
        al.run(SOURCE_VERTEX);
        System.out.println(best.getFitness());
        System.out.println(getDateFromMillis(end - start));
        System.out.println(GA.dem);
        System.out.println("");
        FileOutputStream fos = null;
        String[] str = filename.split(".clt");
        String file = "Para_File(" + "GA_Clus_Tree_" + str[0] + ")_Instance(" + str[0] + ")_Seed(" + seed + ").opt";
        String dirType = "Result/" + type;
        File dir0 = new File(dirType);
        if (!dir0.exists()) {
            dir0.mkdir();
        }
        String dirName = dirType + "/Para_File(" + "GA_Clus_Tree_" + str[0] + ")_Instance(" + str[0] + ")";

        File directory = new File(dirName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String subDirName = dirName;
        if (plc != 0) {
            subDirName += "/LocalSearch";
        } else {
            subDirName += "/NOT_LocalSearch";
        }
        File dir = new File(subDirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            fos = new FileOutputStream(subDirName + "/" + file);
        } catch (FileNotFoundException ex) {
            System.out.println("Lỗi ghi file");
        }
        PrintWriter pw = new PrintWriter(fos);
        pw.println("filename: " + subDirName + "/" + file);
        pw.println("Seed: " + seed);
        pw.println("Fitness: " + best.getFitness());
        pw.println("Time: " + getDateFromMillis(end - start));
        pw.println("pLS: " + plc);
        pw.print(best.getGene().toString());
        for (int i = 0; i < NUM_CITY; i++) {
            pw.println();
            for (int j = 0; j < NUM_CITY; j++) {
                pw.print(Matrix[i][j]+" ");
            }
            
        }

        pw.close();
//        ArrayList<Integer> Arr = new ArrayList<Integer>();
//        for(int i=1;i<=NUM_CITY;i++) Arr.add(i);
//        Dijsktra al1 = new Dijsktra(Matrix, Arr, distances);
//        al1.run2(SOURCE_VERTEX);
//        System.out.println("*******************************");
        
    }
    


    public static String getDateFromMillis(long millis) {
        String string = String.format("%02d:%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
        return string;
    }

    public static void readFile() {

        BufferedReader br = null;
        try {
            String sCurrentLine = null;
            br = new BufferedReader(new FileReader(filename));
            String[] str = null;
            while (true) {
                sCurrentLine = br.readLine();
                str = sCurrentLine.split(": ");
                if (str[0].equals("DIMENSION ") || str[0].equals("DIMENSION")) {
                    NUM_CITY = Integer.parseInt(str[1]);
                } else if (str[0].equals("NUMBER_OF_CLUSTERS") || str[0].equals("NUMBER_OF_CLUSTERS ")) {
                    NUMBER_OF_CLUSTERS = Integer.parseInt(str[1]);
                    break;
                }
            }
            distances = new Double[NUM_CITY][NUM_CITY];
            for (int j = 0; j < 2; j++) {
                sCurrentLine = br.readLine();
            }
            str = sCurrentLine.split(" ");
            if (str[0].equals("NODE_COORD_SECTION") || str[0].equals("NODE_COORD_SECTION:")) {
                cities = new City[NUM_CITY];
                for (int j = 0; j < NUM_CITY; j++) {
                    sCurrentLine = br.readLine();
                    str = sCurrentLine.split(" "); // split by space
                    ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < str.length; i++) {
                        if (!str[i].isEmpty()) {
                            data.add(str[i]);
                        }
                    }

                    cities[j] = new City(Double.parseDouble(data.get(1)), Double.parseDouble(data.get(2)));
                    for (int i = 0; i <= j; i++) {
                        if (i == j) {
                            distances[j][i] = 0.0;
                        } else {
                            distances[j][i] = distances[i][j] = Math.sqrt(Math.pow((cities[j].getX() - cities[i].getX()), 2)
                                    + Math.pow((cities[j].getY() - cities[i].getY()), 2));
                        }
                    }

                }
            } else if (str[0].equals("EDGE_WEIGHT_SECTION:") || str[0].equals("EDGE_WEIGHT_SECTION")) {
                for (int j = 0; j < NUM_CITY; j++) {
                    sCurrentLine = br.readLine();
                    str = sCurrentLine.split(" "); // split by space
                    ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < str.length; i++) {
                        if (!str[i].isEmpty()) {
                            data.add(str[i]);
                        }
                    }
                    for (int k = j; k < NUM_CITY; k++) {
                        if (k == j) {
                            distances[k][j] = distances[j][k] = 0.0;
                        } else {
                            distances[j][k] = distances[k][j] = Double.parseDouble(data.get(k));
                        }
                    }
                }
            }
            for (int j = 0; j < 2; j++) {
                sCurrentLine = br.readLine();
            }
            str = sCurrentLine.split(": ");
            SOURCE_VERTEX = Integer.parseInt(str[1])+1;
            for (int j = 0; j < NUMBER_OF_CLUSTERS; j++) {
                sCurrentLine = br.readLine();
                str = sCurrentLine.split(" ");
                List<Integer> L = new ArrayList<>();
                for (int k = 1; k < str.length - 1; k++) {
                    L.add(Integer.parseInt(str[k]) + 1);
                }
                ListClusterCity.add(L);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
