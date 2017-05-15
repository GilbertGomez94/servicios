import java.io.Serializable;

/**
 * Created by zedin on 14/05/17.
 */
public class Food implements Serializable{
    private int id;
    private String foodName;
    private String foodWeight;
    private String foodCalorie;
    private Boolean foodPersonalized;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(String foodWeight) {
        this.foodWeight = foodWeight;
    }

    public String getFoodCalorie() {
        return foodCalorie;
    }

    public void setFoodCalorie(String foodCalorie) {
        this.foodCalorie = foodCalorie;
    }

    public Boolean getFoodPersonalized() {
        return foodPersonalized;
    }

    public void setFoodPersonalized(Boolean foodPersonalized) {
        this.foodPersonalized = foodPersonalized;
    }
}
