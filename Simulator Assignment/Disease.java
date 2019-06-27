
/**
 * A object of disease. Each disease has a name, a probability to get infect. 
 * A life increasing rate. And a recovery rate.
 * 
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class Disease
{
    private String diseaseName; // The name of the disease
    private int probabilityInfection; //The probability of infecting to others (Range: 0-100)
    private int lifeIncreaseRate;  //If the animal get infected by these diseases, their life will increase faster,
                                     //This field states how fast will their age increase
    private int recoveryProbability; //This field states the probility that the animal can recover from the disease
    /**
     * Constructor for objects of class Disease
     */
    public Disease(String name, int probabilityInf, int rate, int recovery)
    {
        this.probabilityInfection = probabilityInf;
        this.diseaseName = name;
        this.lifeIncreaseRate = rate;
        this.recoveryProbability = recovery;
    }
    
    /**
     * Return the infection probability
     * @return int the probability
     */
    public  int getProbability()
    {
        return probabilityInfection;
    }
    
    /**
     * return the name of the disease
     * @return the name
     */
    public String getName()
    {
        return diseaseName;
    }
    
    /**
     * return the rate of the disease
     * @return the rate of increase the age
     */
    public int getRate()
    {
        return lifeIncreaseRate;
    }
    
    /**
     * Return the recover probability
     * @return the probability that the disease is recovered.
     */
    public int getRecoveryProbability()
    {
        return recoveryProbability;
    }
}
