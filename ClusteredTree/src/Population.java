
import java.util.ArrayList;
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
public class Population {

    private Random rd = Main.rand;

    public ArrayList<Individual> getPopulation() {
        return population;
    }
    private ArrayList<Individual> population = new ArrayList<>();

    public void Init() {
        for (int i = 0; i < Main.sizePopulation; i++) {
            Individual ind = new Individual();
            ind.initgene();
            ind.setFitness();
            population.add(ind);
        }
    }

  
}
