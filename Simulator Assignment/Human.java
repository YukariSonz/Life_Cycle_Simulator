import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a human.
 * humanes age, move, eat all, and die.
 * 
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class Human extends Animal
{
    // Characteristics shared by all humanes (class variables).
    
    // The age at which a human can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a human can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a human breeding.
    private double BREEDING_PROBABILITY = 0.1;

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single animal. In effect, this is the
    // number of steps a human can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 3;
    private static final int EAGLE_FOOD_VALUE = 5;
    private static final int FOX_FOOD_VALUE = 5;
    private static final int GRASS_FOOD_VALUE = 2;
    private static final int HAMSTER_FOOD_VALUE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // Individual characteristics (instance fields).
    // The human's age.
    private int age;
    // The human's food level, which is increased by eating animals.
    private int foodLevel;  
    private Disease disease;    
    /**
     * Create a human. An human can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the human will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Human(boolean randomAge, Field field, Location location, 
    boolean isMale,Disease disease)
    {
        super(field, location, isMale, null);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(EAGLE_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = EAGLE_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the human does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHumans A list to return newly born humans.
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
     * Increase the age. This could result in the human's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this human more hungry. This could result in the human's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for animals adjacent to the current location.
     * All the animal is eaten
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
            if(animal instanceof Eagle) {
                Eagle eagle = (Eagle) animal;
                if(eagle.isAlive()) { 
                    if (disease == null){
                        setDisease(eagle.getDisease());
                    }
                    eagle.setDead();
                    foodLevel += EAGLE_FOOD_VALUE;
                    return where;
                }
            }
            else if(animal instanceof Hamster) {
                Hamster hamster = (Hamster) animal;
                if(hamster.isAlive()) { 
                    if (disease == null){
                        setDisease(hamster.getDisease());
                    }
                    hamster.setDead();
                    foodLevel += HAMSTER_FOOD_VALUE;
                    return where;
                }
            }
            else if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                    if (disease == null){
                        setDisease(rabbit.getDisease());
                    }
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            
            else if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    if (disease == null){
                        setDisease(fox.getDisease());
                    }
                    fox.setDead();
                    foodLevel += FOX_FOOD_VALUE;
                    return where;
                }
            }
            
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
     * Check whether or not this human is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHumans A list to return newly born humanes.
     */
    
    private void giveBirth(List<Animal> newHumans)
    {
        // New humanes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Human young = new Human(false, field, loc, getGender(), null);
            newHumans.add(young);
        }
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
     * A human can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * A human can find a partner if it finds a human with different gender.
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
            if(animal instanceof Human) {
                Human human = (Human) animal;
                if(human.getGender() != this.getGender()){
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
            BREEDING_PROBABILITY = 0.6;
        }
        else if(getWeatherName().equals("Rainy")){    
            BREEDING_PROBABILITY = 0.7;
        }
        else{
            BREEDING_PROBABILITY = 0.5;
        }
    }
    
    /**
     * Return the age of human.
     * return int The age.
     */
    private int getAge()
    {
        return age;
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
               if (animal instanceof Human){
                   Human human = (Human) animal;
                   if (num <= infectProb){
                       human.setDisease(disease); //Set the disease
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