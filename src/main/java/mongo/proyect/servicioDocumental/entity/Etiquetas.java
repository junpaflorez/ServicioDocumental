package mongo.proyect.servicioDocumental.entity;

public class Etiquetas {
    private String id;
    private int value;

    public void setId(String name)
    {
        this.id = name;
    }

    public void setValue(int count)
    {
        this.value = count;
    }

    public String getId()
    {
        return id;
    }

    public int getValue()
    {
        return value;
    }
}
