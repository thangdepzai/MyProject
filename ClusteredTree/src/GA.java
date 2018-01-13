
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thang
 */
public class GA {
    static int mark;
    static HashMap<Integer, Double> costClusterSOURCE ;
    static HashMap<Integer, Double> saveCostofOneCluster = new HashMap<Integer, Double>();
    static int dem = 0;
    static int generation =0;
    Random rd = Main.rand;
    List<List<Integer>> blackList = new ArrayList<List<Integer>>();

    public Individual Run(int numFitness, double pc, double pm,double plc) {
        
        for(int i =1;i<Main.NUM_CITY+1;i++){
            saveCostofOneCluster.put(i, null);
        }
        mark=0;
        for (int i = 0; i < Main.NUMBER_OF_CLUSTERS; i++) {
            blackList.add(new ArrayList<Integer>());
            if(Main.ListClusterCity.get(i).contains(Main.SOURCE_VERTEX)) mark =i;
        }
        Dijsktra al = new Dijsktra((ArrayList<Integer>) Main.ListClusterCity.get(mark), Main.distances);
         costClusterSOURCE =   al.run(Main.SOURCE_VERTEX);
         double sum =0;
         for(double u:costClusterSOURCE.values() ) sum+=u;
         saveCostofOneCluster.put(Main.SOURCE_VERTEX,sum);
        Individual bestInd;
        Population p = new Population();
        p.Init();
        bestInd = p.getPopulation().get(0);
        for (int i = 1; i < p.getPopulation().size(); i++) {
            if (bestInd.getFitness() > p.getPopulation().get(i).getFitness()) {
                bestInd = p.getPopulation().get(i);
            }
        }
        //------------------------------------
        String dirType = "Result/" + Main.type;
        File dir0 = new File(dirType);
        if (!dir0.exists()) {
            dir0.mkdir();
        }
         String[] str = Main.filename.split(".clt");
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
        subDirName += "/gene";
         File dir2 = new File(subDirName);
        if (!dir2.exists()) {
            dir2.mkdir();
        }
        String file = "Para_File(" + "GA_Clus_Tree_" + str[0] + ")_Instance(" + str[0] + ")_Seed(" + Main.seed + ").gen";
        FileOutputStream fos = null;
         try {
            fos = new FileOutputStream((subDirName + "/" + file), true);
        } catch (FileNotFoundException ex) {
            System.out.println("Loi ghi file");
        }
        PrintWriter pw = new PrintWriter(fos);
        pw.println("Generations	Task_1");
        pw.println(generation+ " "+ bestInd.getFitness());
        //----------------------------------------------
        Population newP = new Population();
        while (dem < numFitness) {

            while (newP.getPopulation().size() < Main.sizePopulation) {
                int m1 = rd.nextInt(p.getPopulation().size());
                int m2;
                do {
                    m2 = rd.nextInt(p.getPopulation().size());
                } while (m2 == m1);
                ArrayList<Individual> child = offspring(p.getPopulation().get(m1), p.getPopulation().get(m2), pc,pm);
                for(Individual ind : child){
                    newP.getPopulation().add(ind);
                    if (ind.getFitness() < bestInd.getFitness()) {
                        bestInd = ind;
                    }
                }
                if (dem > Main.numFitness) {
                    break;
                }

            }
            p = newP;
            newP = new Population();
            generation +=1;
            int count = 0;
            while (count / Main.sizePopulation < Main.plc) {
                if (dem > Main.numFitness) {
                    break;
                }
                count++;
                int k = rd.nextInt(p.getPopulation().size());
                Individual ind = LocalSearch(p.getPopulation().get(k));
                if (ind.getFitness() < p.getPopulation().get(k).getFitness()) {
                    p.getPopulation().remove(k);
                    p.getPopulation().add(ind);
                    if (bestInd.getFitness() > ind.getFitness()) {
                        bestInd = ind;
                    }
                }

            }
        pw.println(generation + " "+bestInd.getFitness());
        }
        pw.close();
        return bestInd;

    }

    public ArrayList<Individual> offspring(Individual ind1, Individual ind2, double pc, double pm) {
        ArrayList<Individual> child = new ArrayList<Individual>();
         if (rd.nextDouble()< pm){
            child.add(mutation(ind1));
            child.add(mutation(ind2));
        }else if (rd.nextDouble() < pc) {
            child = crossover(ind1, ind2);
        }
        return child;
    }

    public Individual mutation(Individual ind) {
        Individual newInd = ind.cloneInd();
        int k;
        do {
            k = rd.nextInt(ind.getGene().size());
        } while ( Main.ListClusterCity.get(k).size() == 1);
        int m;
        int newNode;
        do {
            m = rd.nextInt(Main.ListClusterCity.get(k).size());
            newNode = Main.ListClusterCity.get(k).get(m);
        } while (newNode == ind.getGene().get(k));

        newInd.getGene().set(k, newNode);
        newInd.setFitness();
        return newInd;

    }

    public ArrayList<Individual> crossover(Individual ind1, Individual ind2) {
        Individual newInd1 = ind1.cloneInd();
        Individual newInd2 = ind2.cloneInd();
        ArrayList<Individual> child = new ArrayList<>();
        int m1;
        int m2;
        m1 = rd.nextInt(ind1.getGene().size());
        do {
            m2 = rd.nextInt(ind1.getGene().size());
        } while (m1 == m2);
        if (m2 < m1) {
            int temp = m1;
            m1 = m2;
            m2 = temp;
        }
        for (int i = m1; i <= m2; i++) {
            newInd1.getGene().set(i, ind2.getGene().get(i));
            newInd2.getGene().set(i, ind1.getGene().get(i));
        }
        newInd1.setFitness();
        newInd2.setFitness();
        child.add(ind1);
        child.add(ind2);
        return child;

    }

    public Individual LocalSearch(Individual ind) {
//        int count = 0;
//        for (int i = 0; i < Main.NUMBER_OF_CLUSTERS; i++) {
//            if (blackList.get(i).size() == Main.ListClusterCity.get(i).size()) {
//                System.out.println("het");
//                count++;
//            }
//        }
//        if (count == Main.NUMBER_OF_CLUSTERS) {
//            return ind;
//        }
        Individual newInd = ind.cloneInd();
        int m;

            m = rd.nextInt(ind.getGene().size());
        //&& blackList.get(m).size() == Main.ListClusterCity.get(m).size());
        for (int k : Main.ListClusterCity.get(m)) {
            if (blackList.get(m).indexOf(k) == -1 && k != ind.getGene().get(m)) {
                newInd.getGene().set(m, k);
                newInd.setFitness();
                if (ind.getFitness() > newInd.getFitness()) {
                    if (blackList.get(m).indexOf(ind.getGene().get(m)) == -1) {
                        blackList.get(m).add(ind.getGene().get(m));
                        // cho vao danh sach den nhung dinh k cai thien duoc fitness, giam so lan goi tinh fitness
                    }
                    return newInd;

                } else if (ind.getFitness() < newInd.getFitness()) {
                    blackList.get(m).add(k);
                }
            }
            if (GA.dem == Main.numFitness) {
                return ind;
            }
        }
        return ind;
    }

}
