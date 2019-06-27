import java.util.Random;
import java.util.ArrayList;
/**
 *  A disease list generate a list of diseases.
 *
 * @author Bonian Hu and Yilei Liang
 * @version 2018. 2.18
 */
public class DiseaseList
{
    // instance variables - replace the example below with your own
    private ArrayList<Disease> diseaseList;
    

    /**
     * Constructor for objects of class DiseaseList
     */
    public DiseaseList()
    {
       initialTheList();   
    }
    
    /**
     * Initialize the disease List. Each disease have differnt names, probability of getting infected by others,
     * The infection from the disease (The life grows fasters) and the probability of getting recoveried at next step
     * E.g. Cold : When an animal met another animal with cold, the probability of getting infected is 5%
     * And if the aniaml is getting cold, their life will grow 50% faster than usual.
     * And in the next step, the probability of getting recoveried is 75%.
     */
    
    public void initialTheList()
    {
        diseaseList = new ArrayList<>();
        
        Disease Cold = new Disease ("Cold", 5, 2, 75);
        Disease Cancer = new Disease("Cancer", 0, 8, 5);
        Disease H7N9 = new Disease ("H7N9", 35, 3, 15);
        Disease H5N1 = new Disease ("H5N1", 20, 4, 25);
        
        diseaseList.add(Cold);
        diseaseList.add(Cancer);
        diseaseList.add(H7N9);
        diseaseList.add(H5N1);
        
    }
    
    /**
     * Random generate a disease from the list
     * return a disease.
     */
    public Disease randomGenerateDisease()
    {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(diseaseList.size());
        Disease diseaseGenerated = diseaseList.get(randomIndex);
        return diseaseGenerated;
    }
}
