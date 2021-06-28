package rs.edu.raf.mtomic.paclike;

import java.util.Random;

public class Chromosome {

    public float blinkyPenalty, clydePenalty, inkyPenalty, pinkyPenalty, pelletReward;

    public Chromosome() {
        Random r = new Random();
        this.blinkyPenalty = -r.nextFloat();
        this.clydePenalty = -r.nextFloat();
        this.inkyPenalty = -r.nextFloat();
        this.pinkyPenalty = -r.nextFloat();
        this.pelletReward = r.nextFloat();
    }

    public Chromosome(float bp, float cp, float ip, float pp, float pr) {
        Random r = new Random();
        this.blinkyPenalty = bp;
        this.clydePenalty = cp;
        this.inkyPenalty = ip;
        this.pinkyPenalty = pp;
        this.pelletReward = pr;
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "blinkyPenalty=" + this.blinkyPenalty +
                ", clydePenalty=" + this.clydePenalty +
                ", inkyPenalty=" + this.inkyPenalty +
                ", pinkyPenalty=" + this.pinkyPenalty +
                ", pelletReward=" + this.pelletReward +
                '}';
    }
}
