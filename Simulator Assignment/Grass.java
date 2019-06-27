import java.util.List;
import java.util.Random;

/**
 * A simple model of a grass.
 * Grasses age, sow, and die.
 * 
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class Grass extends Plant
{
    // Characteristics shared by all grasss (class variables).

    // The age at which a grass can start to sow.
    private static final int SOWING_AGE = 1;
    // The age to which a grass can live.
    private static final int MAX_AGE = 5;
    // The likelihood of a grass sowing.
    private double SOWING_PROBABILITY = 0.9;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control sowing.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The grass's age.
    private int age;

    /**
     * Create a new grass. A grass may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the grass will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the grass does most of the time - it runs 
     * around. Sometimes it will sow or die of old age.
     * @param newgGrasses A list to return newly born grasses.
     */
    public void act(List<Plant> newGrasses)
    {
        incrementAge();
        weatherEffect();
        if(isAlive()) {
            giveBirth(newGrasses);             
        }
    }

    /**
     * Increase the age.
     * This could result in the grass's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this grass is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newGrasss A list to return newly born grasss.
     */
    private void giveBirth(List<Plant> newGrass)
    {
        // New grasss are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = sow();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc);
            newGrass.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can sow.
     * @return The number of births (may be zero).
     */
    private int sow()
    {
        int births = 0;
        if(canSow() && rand.nextDouble() <= SOWING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A grass can sow if it has reached the sowing age.
     * @return true if the grass can sow, false otherwise.
     */
    private boolean canSow()
    {
        return age >= SOWING_AGE;
    }
    
    /**
     * Produce the weather effect
     */
    private void weatherEffect()
    {
        if(getWeatherName().equals("snow")){
            // if the weather is snow, it is hard to sow and quiker to dead.
            SOWING_PROBABILITY = 0.3;
            incrementAge();
            incrementAge();
        }
        else if(getWeatherName().equals("rain")){
            //if the weather is rain, it will be easier to sow.
            SOWING_PROBABILITY = 0.7;
        }
        else{
            SOWING_PROBABILITY = 0.5;
        }
        
    }
}
