import java.util.List;
/**
 * A class representing shared characteristics of plants.
 *
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
abstract public class Plant
{ 
    // Check the grass is alive or not.
    private boolean alive;
    // The grass's field.
    private Field field;
    // The grass's position in the field.
    private Location location;
    // The weather of the field weather.
    private Weather weather;
    /**
     * Create a new grass at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    
    /**
     * Make this plant act - that is: make it do
     * whatever it wants/needs to do.
     * @param newPlants A list to receive newly born plants.
     */
    abstract public void act(List<Plant> newPlants);
    
    /**
     * Check whether the plant is alive or not.
     * @return true if the plant is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Indicate that the plant is no longer alive.
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
     * Place the plant at the new location in the given field.
     * @param newLocation The plant's new location.
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
     * Return the plant's location.
     * @return The plant's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the plant's field.
     * @return The plant's field.
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
}
