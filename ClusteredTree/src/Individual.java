
import java.util.ArrayList;
import java.util.HashMap;
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
public class Individual {

    private Random rd = Main.rand;
    private ArrayList<Integer> gene = new ArrayList<>();
    private double fitness = 0;

    public void initgene() {
        for (int i = 0; i < Main.NUMBER_OF_CLUSTERS; i++) {
            int k = rd.nextInt(Main.ListClusterCity.get(i).size());
            gene.add(Main.ListClusterCity.get(i).get(k));
        }
    }

    public void setFitness() {
        GA.dem++;
        fitness = 0;
        for (int i = 0; i < Main.NUMBER_OF_CLUSTERS; i++) {
            if (i!=GA.mark && GA.saveCostofOneCluster.get(gene.get(i)) == null) {
                Dijsktra al = new Dijsktra((ArrayList<Integer>) Main.ListClusterCity.get(i), Main.distances);
                double sum = 0;
                HashMap<Integer, Double> pos = al.run(gene.get(i));
                for (Double d : pos.values()) {
                    sum += d;
                }
                GA.saveCostofOneCluster.put(gene.get(i), sum);
            }
        }
        Dijsktra al = new Dijsktra(gene, Main.distances);

        HashMap<Integer, Double> pos = al.run(gene.get(GA.mark));
        for (int i = 0; i < gene.size(); i++) {
            if (i!=GA.mark) {
                fitness += GA.saveCostofOneCluster.get(gene.get(i));
            } else {
                fitness += GA.saveCostofOneCluster.get(Main.SOURCE_VERTEX);
            }
            if (i!=GA.mark) {
                fitness += (pos.get(gene.get(i)) + GA.costClusterSOURCE.get(gene.get(GA.mark))) * Main.ListClusterCity.get(i).size();
            }
        }

    }

    public ArrayList<Integer> getGene() {
        return gene;
    }

    public double getFitness() {
        return fitness;
    }

    public Individual cloneInd() {
        Individual newInd = new Individual();
        for (int i = 0; i < Main.NUMBER_OF_CLUSTERS; i++) {
            newInd.gene.add(gene.get(i));
        }
        return newInd;
    }

}
