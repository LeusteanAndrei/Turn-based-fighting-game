package Classes.gameClasses;

import java.util.List;

public class Stage {


    String name ="default";
    List<String> attributes;


    public Stage()
    {

    }

    public Stage(Stage stage) {
        this.name = stage.name;
        this.attributes = stage.attributes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }



}
