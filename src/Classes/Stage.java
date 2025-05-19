package Classes;

import java.util.List;

public class Stage {


    String name;
    List<String> attributes;


    public Stage()
    {

    }

    public Stage(Stage stage) {
        this.name = stage.name;
        this.attributes = stage.attributes;
    }


}
