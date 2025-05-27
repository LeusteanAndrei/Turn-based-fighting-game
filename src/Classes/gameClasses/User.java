package Classes.gameClasses;

import utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    int id;
    private  Map<Integer, Relic> relics = new HashMap<>();
    private List<Pair> characters = new ArrayList<>();
    private String username;
    private String password;

    public User(){}

    public User(User user)
    {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.setCharacters(user.getCharacters());
        this.setRelics(user.getRelics());
    }

    public Relic getRelic(Integer integer)
    {
        if (relics.get(integer) == null)
            return null;
        return new Relic(relics.get(integer));
    }

    public void setRelic( Relic relic)
    {
        relics.put(relic.getId(), new Relic(relic));
    }
    public List<Pair> getCharacters() {
        List<Pair> characters = new ArrayList<>();
        for (Pair character : this.characters) {
            characters.add(new Pair(character));
        }
        return characters;
    }
    public void setCharacters(List<Pair> characters) {
        this.characters = new ArrayList<>();
        for (Pair character : characters) {
            this.characters.add(new Pair(character));
        }
    }
    public void setRelics(Map<Integer, Relic> relics) {
        this.relics = new HashMap<>();
        for (Map.Entry<Integer, Relic> entry : relics.entrySet()) {
            this.relics.put(entry.getKey(), new Relic(entry.getValue()));
        }
    }
    public Map<Integer, Relic> getRelics() {
        Map<Integer, Relic> relics = new HashMap<>();
        for (Map.Entry<Integer, Relic> entry : this.relics.entrySet()) {
            relics.put(entry.getKey(), new Relic(entry.getValue()));
        }
        return relics;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void removeRelicFromCharacter(int relic_id, int character_id)
    {
        Playable character = null;
        for (Pair pair : characters) {
            if (pair.getCharacter().getId() == character_id) {
                character = pair.getCharacter();
                if (pair.getRelic() != relic_id)
                    return;
                pair.setRelic(-1);
                Relic relic = relics.get(relic_id);
                relic.setEquipped(false);
                break;
            }
        }
    }

    public void equipRelicToCharacter(int relic_id, int character_id)
    {
        Playable character = null;
        for (Pair pair : characters) {
            if (pair.getCharacter().getId() == character_id) {
                character = pair.getCharacter();
                if (pair.getRelic() == relic_id)
                    return;
                if (pair.getRelic() != -1)
                    removeRelicFromCharacter(pair.getRelic(), character_id);
                pair.setRelic(relic_id);
                Relic relic = relics.get(relic_id);
                relic.setEquipped(true);
                break;
            }
        }
    }

    public List<Playable> getPlayableCharacters() {
        List<Playable> playableCharacters = new ArrayList<>();
        for (Pair pair : characters) {
            playableCharacters.add(pair.getCharacter());
        }
        return playableCharacters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ").append(id).append("\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Password: ").append(password).append("\n");
        sb.append("Characters:\n");
        for (Pair pair : characters) {
            sb.append("  - ").append(pair).append("\n");
        }
        return sb.toString();
    }
}
