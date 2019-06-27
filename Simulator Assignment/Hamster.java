import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a hamster.
 * hamsters age, move, breed, and die.
 * 
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class Hamster extends Animal
{
    // Characteristics shared by all hamsters (class variables).

    // The age at which a hamster can start to breed.
    private static final int BREEDING_AGE = 4;
    // The age to which a hamster can live.
    private static final int MAX_AGE = 25;
    // The likelihood of a hamster breeding.
    private  double BREEDING_PROBABILITY = 0.5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 8;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
   // The food value of a single hamster. In effect, this is the
    // number of steps a hamster can go before it has to eat again.
    private static final int GRASS_FOOD_VALUE = 10;
    // The hamster's age.
    private int age;
    // The hamster's food level, which is increased by eating hamsters.
    private int foodLevel;
    
    
    

    /**
     * Create a new hamster. A hamster may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the hamster will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hamster(boolean randomAge, Field field, Location location, boolean isMale, Disease disease)
    {
        
        super(field, location, isMale, null);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(GRASS_FOOD_VALUE);
        }
        else{
            age = 0;
            foodLevel = GRASS_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the hamster does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newhamsters A list to return newly born hamsters.
     */
    public void act(List<Animal> newHamsters)
    {
        weatherBreedEffect();
        diseaseInflucence();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHamsters);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null || getWeatherName().equals("Foggy")) { 
                // No food found - try to move to a free location.
                // Or it's foggy so that the animal cannot see where the food is.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            else if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
        }
    
    /**
     * Make this hamster more hungry. This could result in the hamster's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }   

    
        
    /**
     * Increase the age.
     * This could result in the hamster's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this hamster is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newhamsters A list to return newly born hamsters.
     */
    private void giveBirth(List<Animal> newHamsters)
    {
        // New hamsters are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hamster young = new Hamster(false, field, loc, getGender(), null);
            newHamsters.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY && findPartner() && (!this.getGender())) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A hamster can breed if it has reached the breeding age.
     * @return true if the hamster can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Look for hamsters adjacent to the current location.
     * Only the first live hamster is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if(grass.isAlive()) { 
                    grass.setDead();
                    foodLevel += GRASS_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * A hamster can find a partner if it finds a hamster with different gender.
     */
    private boolean findPartner()
    { 
        boolean canFind = false;
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Hamster) {
                Hamster hamster = (Hamster) animal;
                if(hamster.getGender() != this.getGender()){
                    return true;
                }
            }
        }
        return canFind;
    }
    
    
    /**
     * when the animal sleep
     */
    public void sleep(){
        incrementAge();
    }
        /**
     * Produce the weather effect
     */
    private void weatherBreedEffect()
    {
        if(getWeatherName().equals("Foggy")){
            
            BREEDING_PROBABILITY = 0.3;
            
        }
        else if(getWeatherName().equals("Rainy")){
            
            BREEDING_PROBABILITY = 0.7;
        }
        else{
            BREEDING_PROBABILITY = 0.5;
        }
        
        
    }
    
    /**
     * This method will apply the influence of the disease to the hamster
     * And randomly generate the disease (or probably not) to the hamster if it is healthy.
     */
    /**
     * This method will apply the influence of the disease to this animal
     * And randomly generate the disease (or probably not) to this animal if it is healthy.
     */
   public void diseaseInflucence()
   {
       if (disease==null){
           disease = generateDisease();
           incrementAge(); //The age increase normally if this animal is healthy
        }
       else{
           Random randomGen = new Random();
           int infectProb = disease.getProbability(); // The probability that the disease will spread
           //The disease will only spread to others if it is alive.
           if (isAlive()){ 
           Field field = getField();
           List<Location> adjacent = field.adjacentLocations(getLocation());
           Iterator<Location> it = adjacent.iterator();
           while(it.hasNext()) {
               Location where = it.next();
               Object animal = field.getObjectAt(where);
               int num = randomGen.nextInt(100);
               if (animal instanceof Hamster){
                   Hamster hamster = (Hamster) animal;
                   if (num <= infectProb){
                       hamster.setDisease(disease); //Set the disease
                    }
                }
            }
        }
           int recovProb = randomGen.nextInt(100); 
           //The age increase when it is infected
           for (int i=0; i<disease.getRate();i++){
               incrementAge(); 
           }
           
           //If it's sunny, the probability that the animal will revocer is doubled
           if (getWeatherName().equals("Sunny")){
               if (recovProb <= disease.getRecoveryProbability()*2){
                   this.disease = null;
                }
            }
           else{
                if (recovProb <= disease.getRecoveryProbability()*2){
                   this.disease = null;
                }
            }
        }
   }
}
