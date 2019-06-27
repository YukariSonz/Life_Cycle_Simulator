import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a eagle.
 * eaglees age, move, eat rabbits, and die.
 * 
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class Eagle extends Animal
{
    // Characteristics shared by all eaglees (class variables).
    
    // The age at which a eagle can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a eagle can live.
    private static final int MAX_AGE = 12;
    // The likelihood of a eagle breeding.
    private  double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a eagle can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 8;
    private static final int HAMSTER_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The eagle's age.
    private int age;
    // The eagle's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    private Disease disease;


    /**
     * Create a eagle. An eagle can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the eagle will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Eagle(boolean randomAge, Field field, Location location, boolean isMale, Disease disease)
    {
        super(field, location, isMale, null);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the eagle does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newEagles A list to return newly born eagles.
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
     * Increase the age. This could result in the eagle's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this eagle more hungry. This could result in the eagle's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            else if(animal instanceof Hamster) {
                Hamster hamster = (Hamster) animal;
                if(hamster.isAlive()) { 
                    hamster.setDead();
                    foodLevel += HAMSTER_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this eagle is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newEagles A list to return newly born eaglees.
     */
    private void giveBirth(List<Animal> newEagles)
    {
        // New eaglees are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Eagle young = new Eagle(false, field, loc, getGender(),null);
            newEagles.add(young);
        }
    }
    
       /**
     * when the animal sleep
     */
    public void sleep(){
        incrementAge();
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * if it find a partner
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
     * A eagle can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
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
            BREEDING_PROBABILITY = 0.4;
        }
        else{
            BREEDING_PROBABILITY = 0.7;
        }
    }
    /**
     * A eagle can find a partner if it finds a eagle with different gender.
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
            if(animal instanceof Eagle) {
                Eagle eagle = (Eagle) animal;
                if(eagle.getGender() != this.getGender()){
                    return true;
                }
            }
        }
        return canFind;
    }
    
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
               if (animal instanceof Eagle){
                   Eagle eagle = (Eagle) animal;
                   if (num <= infectProb){
                       eagle.setDisease(disease); //Set the disease
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