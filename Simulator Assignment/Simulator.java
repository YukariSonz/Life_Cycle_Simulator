import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 200;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 200;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.1;
    // The probability that a eagle will be created in any given grid position.
    private static final double EAGLE_CREATION_PROBABILITY = 0.03;
    // The probability that a grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.1;
    // The probability that a human will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.03;    
    
    private static final double HAMSTER_CREATION_PROBABILITY = 0.1 ;

    // List of animals in the field.
    private List<Animal> animals;
    // List of plants in the field.
    private List <Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    //A weather of the simulator.
    private WeatherList weatherList;
    private Weather currentWeather;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        field = new Field(depth, width);
        
        plants = new ArrayList<>();
        field = new Field(depth, width);
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Eagle.class, Color.RED);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Human.class, Color.CYAN);
        view.setColor(Hamster.class, Color.GRAY);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
             //delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox, rabbit, eagle, grass.
     */
    public void simulateOneStep()
    {
        step++;
        //generate a weather
        WeatherList weatherList = new WeatherList();
        currentWeather = weatherList.generateWeather();
        view.setWeather(currentWeather.getWeather());
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        if (getDay().equals("day")){
        // in the day, the animal can act
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.setWeather(currentWeather);
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }       
        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        List<Plant> newPlants = new ArrayList<>();        
        // Let all plant act.
        for(Iterator<Plant> it = plants.iterator(); it.hasNext(); ) {
            Plant plant = it.next();
            plant.setWeather(currentWeather);
            plant.act(newPlants);
            
            if(! plant.isAlive()) {
                it.remove();
            }
        }
        plants.addAll(newPlants);
      }
      else{
        //at night, the animals will sleep.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ){
            Animal animal = it.next();
            animal.sleep();
            if(! animal.isAlive()) {
                it.remove();
            }
        }
        
       }
        // Add the newly born grass to the main lists.
      view.showStatus(step, field);
      int numberDay = step/2;
      view.setDay(numberDay);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        plants.clear();
        populate(); 
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes, rabbits, eagles, and grass.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location, rand.nextBoolean(),null);
                    animals.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location,rand.nextBoolean(), null);
                    animals.add(rabbit);
                }
                else if(rand.nextDouble() <= EAGLE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Eagle eagle = new Eagle(true, field, location,rand.nextBoolean(),null);
                    animals.add(eagle);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Human human = new Human (true, field, location,rand.nextBoolean(),null);
                    animals.add(human);
                }
                else if(rand.nextDouble() <= HAMSTER_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Hamster hamster= new Hamster(true, field, location,rand.nextBoolean(),null);
                    animals.add(hamster);
                }
               
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location);
                    plants.add(grass);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    /**
     * Return the state of the day, which is day or night.
     * @return the day or night.
     */
    public String getDay(){
        if (step % 2 == 0){
            return "day";
        }
        else{ return"night";}
    }
}
