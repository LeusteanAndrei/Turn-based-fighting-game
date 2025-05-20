package Classes;

import java.util.HashMap;
import java.util.Map;

public class User {

    Map<Integer, Relic> relics = new HashMap<>();

    public static final User instance = new User();

    private User()
    {}

    public static User getInstance()
    {
        return instance;
    }

    public Relic getRelic(Integer integer)
    {
        if (relics.get(integer) == null)
            return null;
        return new Relic(relics.get(integer));
    }

}
