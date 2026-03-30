import java.util.Arrays;

public class Row {
    double[] attributes;
    String classification;

    public Row(double[] attributes, String classification){
        this.attributes = attributes;
        this.classification = classification;

    }
    public Row(){};

    @Override
    public String toString() {
        return "Row{" +
                "attributes=" + Arrays.toString(attributes) +
                ", classification='" + classification + '\'' +
                '}';
    }
}
