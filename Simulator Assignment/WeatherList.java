import java.util.Random;
import java.util.ArrayList;
/**
 * The weather list class define a list of the weather.
 *
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class WeatherList
{
    //define  a list of weather
    private ArrayList<Weather> weatherList;
    //A random generator.
    private Random randomGenerator;

    /**
     * Constructor for objects of class Weather_List
     */
    public WeatherList()
    {
        initialiseList();
    }
    
    /**
     * Retrive a random weather from the weather list.
     * return a random weather.
     */
    public Weather generateWeather(){
        randomGenerator = new Random();
        int randomWeatherIndex = randomGenerator.nextInt(weatherList.size());
        Weather currentWeather = weatherList.get(randomWeatherIndex);
        return currentWeather;
    }
    /**
     * Set up different weathers and add them in to the weatherList.
     */
    public void initialiseList()
    {
        weatherList = new ArrayList<>();
        Weather sunny = new Weather("Sunny");
        Weather foggy = new Weather("Foggy");
        Weather rainy = new Weather("Rainy");
        Weather snowy = new Weather("Snowy");
        
        
        weatherList.add(sunny);
        weatherList.add(foggy);
        weatherList.add(rainy);
        weatherList.add(snowy);
    }
}
