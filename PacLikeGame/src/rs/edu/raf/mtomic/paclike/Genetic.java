package rs.edu.raf.mtomic.paclike;

import javafx.util.Pair;
import rs.edu.raf.mtomic.paclike.agent.player.PlayerOne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Genetic {

    private static int iterationsCap = 100;
    private static int populationSize = 100;
    private static int tournamentK = 3;
    private static float populationTransferRate = 0.25f;
    private static float mutationRate = 0.5f;
    private static float mutationValue = 0.3f;             // parametar koji mutira ce se promeniti u opsegu od (-mutVal, mutVal)

    private static ArrayList<Chromosome> GeneratePool() {
        ArrayList<Chromosome> pool = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            pool.add(new Chromosome());
        }
        return pool;
    }

    private static Chromosome crossover(Chromosome c1, Chromosome c2) {
        float blinkyPenalty = (c1.blinkyPenalty + c2.blinkyPenalty) / 2f;
        float clydePenalty = (c1.clydePenalty + c2.clydePenalty) / 2f;
        float inkyPenalty = (c1.inkyPenalty + c2.inkyPenalty) / 2f;
        float pinkyPenalty = (c1.pinkyPenalty + c2.pinkyPenalty) / 2f;
        float pelletReward = (c1.pelletReward + c2.pelletReward) / 2f;
        return new Chromosome(blinkyPenalty, clydePenalty, inkyPenalty, pinkyPenalty, pelletReward);
    }

    private static Chromosome mutate(Chromosome c) {
        Random r = new Random();
        float blinkyPenalty = c.blinkyPenalty;
        float clydePenalty = c.clydePenalty;
        float inkyPenalty = c.inkyPenalty;
        float pinkyPenalty = c.pinkyPenalty;
        float pelletReward = c.pelletReward;
        if (r.nextFloat() < mutationRate) {
            blinkyPenalty += r.nextFloat() * 2 * mutationValue - mutationValue;
            if (blinkyPenalty > 0) blinkyPenalty*=-1;
        }
        if (r.nextFloat() < mutationRate) {
            clydePenalty += r.nextFloat() * 2 * mutationValue - mutationValue;
            if (clydePenalty > 0) clydePenalty*=-1;
        }
        if (r.nextFloat() < mutationRate) {
            inkyPenalty += r.nextFloat() * 2 * mutationValue - mutationValue;
            if (inkyPenalty > 0) inkyPenalty*=-1;
        }
        if (r.nextFloat() < mutationRate) {
            pinkyPenalty += r.nextFloat() * 2 * mutationValue - mutationValue;
            if (pinkyPenalty > 0) pinkyPenalty*=-1;
        }
        if (r.nextFloat() < mutationRate) {
            pelletReward += r.nextFloat() * 2 * mutationValue - mutationValue;
            if (pelletReward < 0) pelletReward*=-1;
        }
        return new Chromosome(blinkyPenalty, clydePenalty, inkyPenalty, pinkyPenalty, pelletReward);
    }

    private static Chromosome tournamentSelect(ArrayList<Pair<Chromosome, Integer>> fitnessPool, int size) {
        ArrayList<Pair<Chromosome, Integer>> randomChroms = new ArrayList<>();
        Random r = new Random();
        while (randomChroms.size() < size) {
            randomChroms.add(fitnessPool.get(r.nextInt(fitnessPool.size())));
        }
        Chromosome bestChrom = randomChroms.get(0).getKey();
        int bestValue = randomChroms.get(0).getValue();
        for (Pair<Chromosome, Integer> chrom : randomChroms) {
            if (chrom.getValue() > bestValue) {
                bestChrom = chrom.getKey();
                bestValue = chrom.getValue();
            }
        }
        return bestChrom;
    }

    private static ArrayList<Chromosome> tournamentPool(ArrayList<Pair<Chromosome, Integer>> fitnessPool) {
        fitnessPool.sort(Comparator.comparing(Pair::getValue));

        ArrayList<Chromosome> newPool = new ArrayList<>();
        int fitN = (int)(populationSize * populationTransferRate);
        System.out.println(fitN);

        int i = 0;
        while (i < populationSize) {
            if (i < fitN) {
                newPool.add(fitnessPool.get(populationSize - 1 - i).getKey());
            }
            else {
                Chromosome p1 = tournamentSelect(fitnessPool, tournamentK);
                Chromosome p2 = tournamentSelect(fitnessPool, tournamentK);
                Chromosome p = crossover(p1, p2);
                p = mutate(p);
                newPool.add(p);
            }
            i++;
        }
        return newPool;
    }

    private static ArrayList<PlayerOne> createPlayers(ArrayList<Chromosome> pool) {
        ArrayList<PlayerOne> players = new ArrayList<>();
        for (Chromosome chromosome : pool) {
            players.add(new PlayerOne(null, chromosome));
        }
        return players;
    }

    private static Float calculateAvgByGen(ArrayList<Pair<Chromosome, Integer>> fitnessPool) {
        float total = 0f;
        for (int j = 0; j < fitnessPool.size(); j++) {
            total += fitnessPool.get(j).getValue();
        }
        return total / populationSize;
    }

    private static Pair<Chromosome, Integer> getBestInGen(ArrayList<Pair<Chromosome, Integer>> fitnessPool) {
        Pair<Chromosome, Integer> bestInGen = new Pair<>(new Chromosome(), -1);
        for (int j = 0; j < fitnessPool.size(); j++) {
            if (fitnessPool.get(j).getValue() > bestInGen.getValue()) {
                bestInGen = fitnessPool.get(j);
            }
        }
        return bestInGen;
    }

    public static void main(String[] args) {
        ArrayList<Chromosome> pool = GeneratePool();


        ArrayList<Float> averageByGeneration = new ArrayList<>();
        ArrayList<Pair<Chromosome, Integer>> bestByGeneration = new ArrayList<>();

        for (int i = 0; i < iterationsCap; i++) {
            ArrayList<PlayerOne> players = createPlayers(pool);

            ArrayList<Pair<Chromosome, Integer>> fitnessPool = new ArrayList<>();

            for (int j = 0; j < players.size(); j++) {
                PacLike pacLike = new PacLike(players.get(j));
                try {
                    pacLike.join();
                    fitnessPool.add(new Pair<>(players.get(j).chromosome, pacLike.getTotalPoints()));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            averageByGeneration.add(calculateAvgByGen(fitnessPool));
            bestByGeneration.add(getBestInGen(fitnessPool));
            pool = tournamentPool(fitnessPool);
            System.out.print(averageByGeneration.get(i) + " ");
            System.out.print(bestByGeneration.get(i) + " \n");
        }
    }

}
