package test.pets;

import com.heliorm.annotation.Pojo;

/**
 * @author gideon
 */
@Pojo
public class Cat extends Mamal {

    public enum Type {
        INDOOR, OUTDOOR;
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


}
