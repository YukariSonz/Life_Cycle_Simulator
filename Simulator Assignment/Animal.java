import java.util.List;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    //The gender of the animal;
    private boolean isMale;
    // A shared random number generator to control gender.
    private static final Random rand = Randomizer.getRandom();
    // A disease 
    protected Disease disease;
    //
    //A weather that in the field.
    private Weather weather;
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, boolean isMale, Disease disease)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        setGender();
        this.disease = disease;
    }
    
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newHumans);

    /**
     * when the animal sleep.
     */
    abstract public void sleep();
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Set the gender of the animal when one animal is created.
     */
    protected void setGender()
    {
        this.isMale = rand.nextBoolean();
    }
    
    /**
     * Return the animal's gender.
     * @return animal's gender.
     */
    protected boolean getGender()
    {
        return isMale;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */    
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Set the weather.
     * @param curret weather.
     */
    protected void setWeather(Weather fieldWeather)
    {
        this.weather = fieldWeather;
    }
    
    /**
     * Return the weather
     * @return the weather.
     */
    protected Weather getWeather()
    {
        return weather;
    }
    
    /**
     * return the weather's name
     * @return the name.
     */
    protected String getWeatherName(){
        return getWeather().getWeather();
    }
    
    /**
     * return the disease
     * @return the disease.
     */
    
    protected Disease generateDisease()
    {
        Disease proviDisease = null;
        int randomNum = rand.nextInt(100);
        DiseaseList diseaseList = new DiseaseList();
        if (getWeatherName().equals("Rainy")){
            if (randomNum>=90){
                proviDisease = diseaseList.randomGenerateDisease();
            }
        }
        else{
            if (randomNum>=95){
                proviDisease = diseaseList.randomGenerateDisease();
            }
        }
        return proviDisease;
    }
    
    abstract public void diseaseInflucence();
    
    public void setDisease(Disease infectDisease)
    {
        if (disease==null){
            disease = infectDisease;
        }
        else{
            return;
        }
    }
    
    public Disease getDisease()
    {
        return disease;
    }
}
