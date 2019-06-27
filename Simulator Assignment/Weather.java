
/**
 * Weather class define a weather, each weather has a name.
 * @author Bonian Hu and Yilei Liang
 * @version 2018.2.18
 */
public class Weather
{
    
    private String weatherName;
    /**
     * Constructor for objects of class Weather
     */
    public Weather(String name)
    {
        this.weatherName = name;
    }

    
    /**
     * get the weather name
     * return String the weather name.
     */
    public String getWeather(){
        return weatherName;
    }
    
}
