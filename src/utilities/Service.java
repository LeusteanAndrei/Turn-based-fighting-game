package utilities;

import Classes.gameClasses.*;
import Classes.dbConnection.*;
import Classes.gameClasses.Character;
import Classes.gameFunctionality.GameState;
import enumerations.CharacterType;
import preparation.PrepareGame;

import java.util.*;
import java.util.stream.Collectors;

public class Service {

    User user;
    String adminPassword = "admin";
    private Map<Integer, Relic> relics = new HashMap<>();
    private static int relic_id = 0;

    public static final Service instance = new Service();

    private Service(){}

    public static Service getInstance() {
        return instance;
    }

    public void start_game()
    {
        setupRelics();
        while(true)
        {
            Printer.clearTerminal();
            String choices = "Create an user, Sign in, Log in as admin, Exit";
            final int choiceNumber = 4;
            Printer.choiceMenu(1, choiceNumber, choices);
            int choice = Reader.readIntInRange(1,choiceNumber);
            switch (choice) {
                case 1 -> {
                    create_user();
                }
                case 2 -> {
                    List<User> users = new UserConnection().read();
                    if (users.isEmpty())
                    {
                        System.out.println("No users found. Please create an user first.");
                        Printer.sleep(2000);
                        continue;
                    }
                    Printer.printUsers(users);
                    System.out.println("Choose and user:");
                    int userChoice = Reader.readIntInRange(1, users.size());
                    User user = users.get(userChoice - 1);
                    Printer.clearTerminal();
                    System.out.println("Welcome " + user.getUsername() );
                    System.out.println("Please enter your password: ");
                    String password = Reader.readPassword();
                    if (!user.getPassword().equals(password))
                    {
                        System.out.println("Wrong password. ");
                        Printer.sleep(2000);
                        continue;
                    }
                    System.out.println("You have logged in successfully.");
                    log_in(user);

                }
                case 3 ->
                {
                    Printer.clearTerminal();
                    System.out.println("Please enter the admin password: ");
                    String password = Reader.readPassword();
                    if (!password.equals(adminPassword))
                    {
                        System.out.println("Wrong password. ");
                        Printer.sleep(2000);
                        continue;
                    }
                    log_in_admin();
                }
                case choiceNumber -> {
                    // Exit
                    System.out.println("Exiting...");
                    return;
                }
            }
        }
    }

    private void log_in_admin()
    {
        while(true) {
            String choices = "View playable characters, View Users, View Relics, View nonplayable characters, Add a playable character, Add a relic, Add a nonplayable character, Add character to user, Add Skill, Add skill to playable character, View skills, Remove, Update, Exit";
            final int choiceNumber = 14;

            Printer.clearTerminal();
            Printer.choiceMenu(1, choiceNumber, choices);
            switch (Reader.readIntInRange(1, choiceNumber)) {
                case 1 -> {
                    List<Playable> characters = new CharacterConnection().getPlayables();
                    view_characters_menu(characters);
                }
                case 2 -> {
                    List<User> user = new UserConnection().read();
                    if (user.isEmpty()) {
                        System.out.println("No users found.");
                        Printer.sleep(2000);
                        return;
                    }
                    Printer.printUsers(user);
                    Reader.readEmpty();
                }
                case 3 -> {
                    Printer.clearTerminal();
                    List<Relic> relics = this.getRelics();
                    Printer.printRelics(relics);
                    Reader.readEmpty();
                }
                case 4 -> {
                    List<NonPlayable> characters = new CharacterConnection().getNonplayable();

                    view_characters_menu(characters);
                }
                case 5 -> {
                    addPlayableCharacter();
                }
                case 6 -> {
                    add_relic();
                }
                case 7 -> {
                    addNonPlayableCharacter();
                }
                case 8 ->
                {
                    User user = selectUser();
                    if (user == null)
                        continue;
                    System.out.println("Choose a character to add to the user " + user.getUsername() + ":");
                    Playable playable  = selectPlayable();
                    if (playable == null)
                        continue;
                    addCharacterToUser(user, playable);
                }
                case 9 ->
                {
                    addSkill();
                }
                case 10 ->
                {
                    Playable playable = selectPlayable();
                    if (playable == null)
                        continue;
                    if (playable.getSkills().size() == 3)
                    {
                        System.out.println("This character already has 3 skills. Please remove one first.");
                        Printer.sleep(2000);
                        continue;
                    }
                    System.out.println("Choose a skill to add to the character " + playable.getName() + ":");
                    List<Skill> allskills = new SkillConnection().read();
                    List<Skill> characterSkills = playable.getSkills();
                    List<Skill> availableSkills = new ArrayList<>();
                    for (Skill skill : allskills) {
                        if (!characterSkills.contains(skill)) {
                            availableSkills.add(skill);
                        }
                    }
                    Skill skill = selectSkill(availableSkills);
                    if (skill == null)
                        continue;

                    int playableId = playable.getId();
                    int skillId = skill.getId();
                    new CharacterConnection().addSkill(playableId, skillId);
                }
                case 11 ->
                {
                    viewSkills();
                }
                case choiceNumber-2 ->
                {
                    remove();

                }
                case choiceNumber-1 ->
                {
                    update();
                }
                case choiceNumber -> {
                    return;
                }
            }
        }
    }

    public void viewSkills()
    {
        Printer.clearTerminal();
        List<Skill> skills = new SkillConnection().read();
        if (skills.isEmpty())
        {
            System.out.println("No skills found.");
            Printer.sleep(2000);
            return;
        }
        Printer.printSkills(skills);
        Reader.readEmpty();
    }

    public Skill selectSkill(List<Skill> skills)
    {
        if (skills.isEmpty())
        {
            System.out.println("No skills found. Please create one first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printSkills(skills);
        System.out.println("Choose a skill:");
        int skillChoice = Reader.readIntInRange(1, skills.size());
        return skills.get(skillChoice - 1);
    }


    public void addSkill()
    {
        Printer.clearTerminal();
        System.out.println("Enter the skill name: ");
        String name = Reader.readString();
        System.out.println("Enter the skill description: ");
        String description = Reader.readString();
        System.out.println("Enter the skill cooldown: ");
        int cooldown = Reader.readIntInRange(0, Integer.MAX_VALUE);

        List<Triplet> effects = readEffects();

        Skill skill = new Skill();
        skill.setName(name);
        skill.setDescription(description);
        skill.setCooldown(cooldown);
        skill.setEffects(effects);

        new SkillConnection().add(skill);
        System.out.println("Skill " + name + " added successfully.");
        Printer.sleep(2000);

    }

    public void updateCharacter(int characterId)
    {

        while(true)
        {
            Printer.clearTerminal();
            String updates = "Update name, Update type, Update attack, Update health, Update damage resistance, Update crit rate, Update crit damage, Update gain, Exit";
            final int updateChoiceNumber = 9;
            Printer.choiceMenu(1, updateChoiceNumber, updates);
            int updateChoice = Reader.readIntInRange(1, updateChoiceNumber);
            switch (updateChoice) {
                case 1 -> {
                    System.out.println("Enter the new name: ");
                    String name = Reader.readString();
                    new CharacterConnection.Updater(characterId).name(name).update();
                }
                case 2 -> {
                    System.out.println("Enter the new type: ");
                    String type = Reader.readString();
                    CharacterType characterType = CharacterType.getCharacterType(type);
                    if (characterType == null) {
                        System.out.println("Invalid type. Please try again.");
                        continue;
                    }
                    new CharacterConnection.Updater(characterId).type(characterType).update();

                }
                case 3 -> {
                    System.out.println("Enter the new attack: ");
                    int attack = Reader.readIntInRange(0, Integer.MAX_VALUE);
                    new CharacterConnection.Updater(characterId).attack(attack).update();
                }
                case 4 -> {
                    System.out.println("Enter the new health: ");
                    int health = Reader.readIntInRange(0, Integer.MAX_VALUE);
                    new CharacterConnection.Updater(characterId).health(health).update();
                }
                case 5 -> {
                    System.out.println("Enter the new damage resistance: ");
                    float damageRes = Reader.readFloatInRange(0, 1);
                    new CharacterConnection.Updater(characterId).damageRes(damageRes).update();
                    System.out.println(characterId + " "+ damageRes);
                    System.out.println("Damage resistance updated successfully.");
                }
                case 6 -> {
                    System.out.println("Enter the new crit rate: ");
                    float critRate = Reader.readFloatInRange(0, 1);
                    new CharacterConnection.Updater(characterId).critRate(critRate).update();
                }
                case 7 -> {
                    System.out.println("Enter the new crit damage: ");
                    float critDamage = Reader.readFloatInRange(0, Float.MAX_VALUE);
                    new CharacterConnection.Updater(characterId).critDamage(critDamage).update();
                }
                case 8 -> {
                    System.out.println("Enter the new gain: ");
                    float gain = Reader.readFloatInRange(0, Float.MAX_VALUE);
                    new CharacterConnection.Updater(characterId).gain(gain).update();
                }
                case updateChoiceNumber -> {
                    return;
                }
            }
        }
    }

    public void update()
    {
        System.out.println("Update a playable character, Update a nonplayable character Exit");
        while(true)
        {
            Printer.clearTerminal();
            final int choiceNumber = 3;
            Printer.choiceMenu(1, choiceNumber, "Update a character stats or type, Update skill, Exit");
            int choice = Reader.readIntInRange(1, choiceNumber);
            switch (choice)
            {
                case 1 ->
                {
                    Printer.clearTerminal();
                    Character character = selectCharacter();
                    int characterId = character.getId();
                    updateCharacter(characterId);
                }
                case 2 ->
                {
                    Printer.clearTerminal();
                    Skill skill = selectSkill(new SkillConnection().read());
                    updateSkill(skill);
                }
                case 3 ->
                {
                    return;
                }
            }
        }

    }

    public void updateSkill(Skill skill)
    {
        int skillId = skill.getId();
        String choices = "Update name, Update description, Update cooldown, Add effect, Remove effect,Change effect,  Exit";
        final int choiceNumber = 7;
        while(true)
        {
            Printer.clearTerminal();
            Printer.choiceMenu(1, choiceNumber, choices);
            int choice = Reader.readIntInRange(1, choiceNumber);
            switch(choice)
            {
                case 1 ->
                {
                    System.out.println("Enter the new name: ");
                    String name = Reader.readString();
                    new SkillConnection.Updater(skillId).name(name).update();
                }
                case 2 ->
                {
                    System.out.println("Enter the new description: ");
                    String description = Reader.readString();
                    new SkillConnection.Updater(skillId).description(description).update();
                }
                case 3 ->
                {
                    System.out.println("Enter the new cooldown: ");
                    int cooldown = Reader.readIntInRange(0, Integer.MAX_VALUE);
                    new SkillConnection.Updater(skillId).cooldown(cooldown).update();
                }
                case 4 ->
                {
                    List<Effect> effects = skill.getEffectList();
                    Set<Integer> effectIds = effects.stream().map(Effect::getId).collect(Collectors.toSet());
                    Effect effect = selectEffect(effectIds);
                    System.out.println("Enter the effect scale: ");
                    float scale = Reader.readFloatInRange(0, Float.MAX_VALUE);
                    System.out.println("Enter the effect duration: ");
                    int duration = Reader.readIntInRange(-1, Integer.MAX_VALUE);
                    new SkillConnection().addEffect(skillId, new Triplet(duration, scale, effect.getName()));
                    System.out.println("Effect " + effect.getName() + " added to skill " + skill.getName() + ".");
                    updateSkillDescription(skill);
                }
                case 5 ->
                {
                    Effect effect = selectSkillEffect(skill);
                    if (effect == null)
                        continue;
                    int effectId = effect.getId();
                    new SkillConnection().removeEffect(effectId, skillId);
                    System.out.println("Effect " + effect.getName() + " removed from skill " + skill.getName() + ".");
                    updateSkillDescription(skill);
                }
                case 6->
                {
                    Effect effect = selectSkillEffect(skill);
                    if (effect == null)
                        continue;
                    changeEffect(skill, effect);
                }
                case choiceNumber ->
                {
                    return;
                }
            }
        }

    }

    public void changeEffect(Skill skill, Effect effect)
    {

        while(true)
        {
            Printer.clearTerminal();
            String choices = "Change effect scale, Change effect duration, Exit";
            final int choiceNumber = 3;
            Printer.choiceMenu(1, choiceNumber, choices);
            int choice = Reader.readIntInRange(1, choiceNumber);

            switch (choice)
            {
                case 1 ->
                {
                    System.out.println("Enter the new effect scale: ");
                    float scale = Reader.readFloatInRange(0, Float.MAX_VALUE);
                    new SkillConnection.Updater(skill.getId()).effectScale(effect.getId(), scale).update();
                    System.out.println("Effect scale updated successfully.");
                    Printer.sleep(1000);
                }
                case 2 ->
                {
                    System.out.println("Enter the new effect duration: ");
                    int duration = Reader.readIntInRange(-1, Integer.MAX_VALUE);
                    new SkillConnection.Updater(skill.getId()).effectDuration(effect.getId(), duration).update();
                    System.out.println("Effect duration updated successfully.");
                    Printer.sleep(1000);
                }
                case choiceNumber ->
                {
                    return;
                }
            }
        }
    }
    public  void  updateSkillDescription ( Skill skill)
    {
        int skillId = skill.getId();
        System.out.println("Update skill description (or enter same for it to stay the same) : ");
        String description = Reader.readString();
        if (description.toLowerCase().equals( "same".toLowerCase()))
        {
            return;
        }
        new SkillConnection.Updater(skillId).description(description).update();

    }

    public Effect selectEffect(Set<Integer> effectIds)
    {
        System.out.println("Available effects:");
        List<Effect> allEffects = new EffectConnection().read();
        List<Effect> availableEffects = allEffects.stream().filter(effect -> !effectIds.contains(effect.getId())).collect(Collectors.toList());
        if (availableEffects.isEmpty())
        {
            System.out.println("No available effects found.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printEffects(availableEffects);
        System.out.println("Choose an effect:");
        int effectChoice = Reader.readIntInRange(1, availableEffects.size());
        return availableEffects.get(effectChoice - 1);
    }

    public Character selectCharacter()
    {
        Printer.clearTerminal();
        List<Character> characters = new CharacterConnection().read();
        if (characters.isEmpty())
        {
            System.out.println("No characters found. Please create one first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printCharacters(characters);
        System.out.println("Choose a character:");
        int characterChoice = Reader.readIntInRange(1, characters.size());
        return characters.get(characterChoice - 1);
    }

    public void remove()
    {
        String choices = "Remove a playable character, Remove a nonplayable character, Remove a user,Remove skill, Remove skill from playable character, Remove effect from skill, Exit";
        final int choiceNumber = 7;
        Printer.clearTerminal();
        Printer.choiceMenu(1, choiceNumber, choices);
        int choice = Reader.readIntInRange(1, choiceNumber);
        switch(choice) {
            case 1 -> {
                Playable playable = selectPlayable();
                int id = playable.getId();
                new CharacterConnection().deletePlayable(id);
                System.out.println("Playable character " + playable.getName() + " removed successfully.");
                Printer.sleep(2000);
            }
            case 2 -> {
                NonPlayable nonPlayable = selectNonPlayable();

                new CharacterConnection().deleteNonPlayable(nonPlayable.getId());
                System.out.println("Non-playable character " + nonPlayable.getName() + " removed successfully.");
                Printer.sleep(2000);
            }
            case 3 -> {
                User user = selectUser();
                if (user == null)
                    return;
                new UserConnection().delete(user.getId());
                System.out.println("User " + user.getUsername() + " removed successfully.");
                Printer.sleep(2000);
            }
            case 4 ->
            {
                Skill skill = selectSkill(new SkillConnection().read());
                if (skill == null)
                    return;
                int skillId = skill.getId();
                new SkillConnection().delete(skillId);
                System.out.println("Skill " + skill.getName() + " removed successfully.");
                Printer.sleep(2000);
            }
            case 5 ->
            {
                Playable playable = selectPlayable();
                if (playable == null)
                    return;
                int playableId = playable.getId();
                Skill skill = selectSkill(playable.getSkills());
                if (skill == null)
                    return;
                int skillId = skill.getId();
                new CharacterConnection().removeSkill(playableId, skillId);
                System.out.println("Skill " + skill.getName() + " removed from character " + playable.getName() + ".");
                Printer.sleep(2000);
            }
            case 6 ->
            {
                Skill skill = selectSkill(new SkillConnection().read());
                if (skill == null)
                    return;
                Effect effect = selectSkillEffect(skill);
                if (effect == null)
                    return;
                int effectId = effect.getId();
                new SkillConnection().removeEffect(effectId, skill.getId());
                System.out.println("Effect " + effect.getName() + " removed from skill " + skill.getName() + ".");
                System.out.println("Update skill description: ");
                String description = Reader.readString();
                new SkillConnection.Updater(skill.getId()).description(description).update();
            }
            case choiceNumber ->
            {
                return;
            }
        }

    }

    public Effect selectSkillEffect(Skill skill)
    {
        Printer.clearTerminal();
        List<Effect> effects = skill.getEffectList();
        if (effects.isEmpty())
        {
            System.out.println("The skill has no effects.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printEffects(effects);
        System.out.println("Choose an effect:");
        int effectChoice = Reader.readIntInRange(1, effects.size());
        return effects.get(effectChoice - 1);
    }
    public NonPlayable selectNonPlayable()
    {
        Printer.clearTerminal();
        List<NonPlayable> characters = new CharacterConnection().getNonplayable();
        if (characters.isEmpty())
        {
            System.out.println("No non-playable characters found. Please create one first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printCharacters(characters);
        System.out.println("Choose a non-playable character:");
        int characterChoice = Reader.readIntInRange(1, characters.size());
        return characters.get(characterChoice - 1);
    }

    public Relic selectRelic()
    {
        Printer.clearTerminal();
        List<Relic> relics = this.getRelics();
        if (relics.isEmpty())
        {
            System.out.println("No relics found. Please create one first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printRelics(relics);
        System.out.println("Choose a relic:");
        int relicChoice = Reader.readIntInRange(1, relics.size());
        return relics.get(relicChoice - 1);
    }

    public void addCharacterToUser(User user, Playable playable)
    {
        if (user == null || playable == null)
        {
            System.out.println("Invalid user or character.");
            return;
        }
        for (Pair pair : user.getCharacters()) {
            if (pair.getCharacter().getId() == playable.getId()) {
                System.out.println("This character is already added to the user.");
                Printer.sleep(1500);
                return;
            }
        }

        new UserConnection().addCharacter(user.getId(),0, -1, playable.getId());
    }

    public Playable selectPlayable()
    {
        Printer.clearTerminal();
        List<Playable> characters = new CharacterConnection().getPlayables();
        if (characters.isEmpty())
        {
            System.out.println("No playable characters found. Please create one first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printCharacters(characters);
        System.out.println("Choose a character:");
        int characterChoice = Reader.readIntInRange(1, characters.size());
        return characters.get(characterChoice - 1);
    }

    public User selectUser()
    {
        Printer.clearTerminal();
        List<User> users = new UserConnection().read();
        if (users.isEmpty())
        {
            System.out.println("No users found. Please create an user first.");
            Printer.sleep(2000);
            return null;
        }
        Printer.printUsers(users);
        System.out.println("Choose an user:");
        int userChoice = Reader.readIntInRange(1, users.size());
        return users.get(userChoice - 1);
    }


    public void add_relic()
    {
        Printer.clearTerminal();
        System.out.println("Enter the relic name: ");
        String name = Reader.readString();
        System.out.println("Enter the relic description: ");
        String description = Reader.readString();

        System.out.println("Do you want to add buffs? (0 for no, 1 for yes)");
        int choice = Reader.readIntInRange(0, 1);
        Map<String, Number> buffs = new HashMap<>();
        if (choice == 1)
        {
            buffs = readBuffs();
        }

        System.out.println("Do you want to add effects? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        List<Triplet> effects = new ArrayList<>();
        if (choice == 1)
        {
            effects = readEffects();
        }

        Relic relic = new Relic();
        relic.setName(name);
        relic.setDescription(description);
        relic.setBuffs(buffs);
        relic.setEffects(effects);
        relic.setId(relic_id);

        relics.put(relic.getId(), relic);
        relic_id++;

    }

    public Map<String, Number> readBuffs()
    {
        Map<String, Number> buffs = new HashMap<>();
        Set<String> buffList = PrepareGame.getInstance().getBuffList();
        System.out.println("Available buffs: " + buffList);
        while(true)
        {
            System.out.println("Enter buff name or 'done' to finish: ");
            String buffName = Reader.readString();
            if (buffName.equals("done"))
            {
                return buffs;
            }
            if (!buffList.contains(buffName))
            {
                System.out.println("Invalid buff name. Please try again.");
                continue;
            }
            System.out.println("Enter buff value: ");
            float buffValue = Reader.readFloatInRange(0, Float.MAX_VALUE);
            if (buffs.containsKey(buffName))
                buffs.put(buffName, buffs.get(buffName).floatValue() + buffValue);
            else
                buffs.put(buffName, buffValue);
        }
    }


    public void view_characters_menu(List<? extends Character> characters)
    {
        String choices;
        int characterChoice =0;
        while (characterChoice !=1) {
            Printer.clearTerminal();
            Printer.printCharacters(characters);
            choices = "Exit, View detailed stats";
            Printer.choiceMenu(1, 2, choices);
            characterChoice = Reader.readIntInRange(1, 2);
            if (characterChoice == 1)
                return;
            System.out.println("Choose a character number to see the detailed stats:\n");
            int choose_character = Reader.readIntInRange(1, characters.size());
            Printer.clearTerminal();
            Printer.detailedPrintCharacter(characters.get(choose_character - 1));
            Reader.readEmpty();
        }
    }


    public List<Playable> view_characters(User user)
    {
        Printer.clearTerminal();
        List<Pair> characters = user.getCharacters();
        if (characters.isEmpty())
        {
            System.out.println("You have no characters.");
            Printer.sleep(2000);
            return new ArrayList<>();
        }
        List<Playable> playableCharacters = new ArrayList<>();
        for (Pair pair : characters) {

            playableCharacters.add(pair.getCharacter());
        }
        Printer.printCharacters(playableCharacters);
        return playableCharacters;
    }

    public List<Relic> view_relics(User user)
    {
        Printer.clearTerminal();
        Map<Integer, Relic> relics = user.getRelics();
        if (relics.isEmpty())
        {
            System.out.println("You have no relics. Please create one.");
            Printer.sleep(2000);
            return new ArrayList<>();
        }
        List<Relic> relicList = new ArrayList<>();
        for (Map.Entry<Integer, Relic> entry : relics.entrySet()) {
            relicList.add(new Relic(entry.getValue()));
        }
        Printer.printRelics(relicList);
        return relicList;


    }

    public void log_in(User user)
    {
        this.user = user;

        for (Pair pair : user.getCharacters()) {
            if (pair.getRelic()!=-1)
                relics.get(pair.getRelic()).setEquipped(true);
        }

        this.user.setRelics(relics);

        while(true)
        {

            String choices = "Start battle, View characters, View relics, Equip relic, Unequip relic, Exit";
            final int choiceNumber = 6;
            Printer.clearTerminal();
            Printer.choiceMenu(1, choiceNumber, choices);
            int choice = Reader.readIntInRange(1, choiceNumber);
            switch (choice) {
                case 1 -> {
                    start_battle();
                }
                case 2 -> {
                    List<Playable> characters = view_characters(user);
                    if (!characters.isEmpty())
                    {
                        view_characters_menu(characters);
                    }
                }
                case 3 -> {
                    view_relics(user);
                    Reader.readEmpty();

                }
                case 4 -> {
                    equip_relic(user);
                }
                case 5 ->
                {
                    unequip_relic(user);
                }
                case choiceNumber -> {
                    return;
                }
            }
        }

    }

    public void equip_relic(User user)
    {
        List<Playable> characters = view_characters(user);

        System.out.println("Choose a character to equip the relic to:");
        int characterChoice = Reader.readIntInRange(1, user.getCharacters().size());
        Playable character = characters.get(characterChoice - 1);
        int characterId = character.getId();

        Printer.clearTerminal();
        System.out.println("Choose a relic to equip or 0 for none:");
        List<Relic> relics = view_relics(user);

        int relicChoice = Reader.readIntInRange(0, user.getRelics().size());
        if (relicChoice == 0)
        {
            System.out.println("No relic equipped.\nGoing back ...");
            Printer.sleep(2000);
            return;
        }

        Relic relic = relics.get(relicChoice - 1);
        int relicId = relic.getId();


        if (relic.isEquipped())
        {
            System.out.println("This relic is already equipped to another character.");
            System.out.println("Do you want to unequip it? (1 for yes, 0 for no)");
            int unequipChoice = Reader.readIntInRange(0, 1);
            if (unequipChoice == 0)
            {
                return;
            }
            int equippedCharacterId = new UserConnection().getEquippedCharacter(user.getId(), relicId);
            new UserConnection.Updater(user.getId()).relic(equippedCharacterId, -1).update();
            this.user.removeRelicFromCharacter(relicId, equippedCharacterId);
        }

        new UserConnection.Updater(user.getId()).relic(characterId, relicId).update();
        this.user.equipRelicToCharacter(relicId, characterId);

        System.out.println("Relic " + relic.getName() + " equipped to character " + character.getName() + ".");
        Printer.sleep(2000);
    }

    public void unequip_relic(User user)
    {
        Printer.clearTerminal();
        List<Playable> characters = view_characters(user);
        System.out.println("Choose a character to unequip the relic from:");
        int characterChoice = Reader.readIntInRange(1, user.getCharacters().size());
        int characterId = characters.get(characterChoice - 1).getId();
        Relic relic = characters.get(characterChoice - 1).getRelic();
        if (relic == null)
        {
            return;
        }
        int relicId = relic.getId();
        user.removeRelicFromCharacter(relicId, characterId);
        new UserConnection.Updater(user.getId()).relic(characterId, -1).update();
        System.out.println("Relic unequipped from character " + characters.get(characterChoice - 1).getName() + ".");
        Printer.sleep(2000);
    }

    public void create_user()
    {
        Printer.clearTerminal();
        System.out.println("Creating an user...");
        System.out.println("Please enter your username: ");
        String username = Reader.readString();
        System.out.println("Please enter your password: " );
        String password = Reader.readPassword();
        System.out.println("Please enter your password again: " );
        String password2 = Reader.readPassword();
        if (!password.equals(password2))
        {
            System.out.println("The passwords do not match. Please try again.");
            Printer.sleep(2000);
            return;
        }

        UserConnection userConnection = new UserConnection();
        int id = userConnection.add(new User());
        new UserConnection.Updater(id)
                .username(username)
                .password(password)
                .update();


        System.out.println("User " + username + " created successfully.");
        Printer.sleep(2000);
    }

    public void setUser(User user)
    {
        this.user =new User( user );
    }
    public  User getUser()
    {
        return new User(user);
    }


    public List<Relic> getRelics()
    {
        List<Relic> relicList = new ArrayList<>();
        for (Map.Entry<Integer, Relic> entry : relics.entrySet()) {
            relicList.add(new Relic(entry.getValue()));
        }
        return relicList;
    }

    public void setRelics(List<Relic> relics)
    {

        this.relics = new HashMap<>();
        for (Relic relic : relics) {
            this.relics.put(relic.getId(), new Relic(relic));
        }
    }

    private void setupRelics()
    {
        Relic relic = new Relic();
        relic.setName("Test Relic");
        relic.setDescription("Test Relic Description");
        relic.setLevel(1);
        relic.setId(relic_id);
        Map<String, Number> buffs = new HashMap<>();
        buffs.put("attack", 100);
        buffs.put("critRate", 0.23f);
        relic.setBuffs(buffs);
        List<Triplet> effects = new ArrayList<>();
        effects.add(new Triplet(5, 0.3f, "self_attack_up"));
        relic.setEffects(effects);

        relics.put(relic.getId(), relic);

        relic_id++;
    }

    public Relic getRelic(int id)
    {
        if (relics.get(id) == null)
            return null;
        return new Relic(relics.get(id));
    }


    private CharacterType readType()
    {
        System.out.println("Add a type ? ( 0 for no, 1 for yes )");
        int choice = Reader.readIntInRange(0, 1);
        if (choice == 0)
            return null;
        System.out.println("Enter a type: ");
        String type = Reader.readString();
        if (CharacterType.getCharacterType(type) == null)
        {
            System.out.println("Invalid type. Please try again.");
            return readType();
        }
        return CharacterType.getCharacterType(type);


    }


    public void addNonPlayableCharacter()
    {
        Printer.clearTerminal();
        System.out.println("Enter the character name: ");
        String name = Reader.readString();
        CharacterType characterType = readType();




        int choice;
        System.out.println("Do you want to add attacks? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        List<Attack> attacks = new ArrayList<>();
        if (choice == 1)
        {
            attacks = readAttacks();
        }

        System.out.println("Do you want to add stats? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        Stats stats = new Stats();
        if (choice == 1)
        {
            stats = readStats();
        }
        else
        {
            System.out.println("No stats added. Default stats will be used.");
        }


        System.out.println("What is the max charge: ");

        int INFINITY = Integer.MAX_VALUE;
        int maxCharge = Reader.readIntInRange(1, INFINITY);
        System.out.println("How many health bars does he have?");
        int healthBars = Reader.readIntInRange(0, INFINITY);
        Stack<Integer> health = new Stack<>();
        for ( int i = 0;i<healthBars;i++)
        {
            System.out.println("Enter the health for health bar " + (i + 1) + ": ");
            int healthBar = Reader.readIntInRange(1, INFINITY);
            health.push(healthBar);
        }

        List<Triplet> effects = new ArrayList<>();
        System.out.println("Do you want to add effects? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        if (choice == 1)
        {
            effects = readEffects();
        }

        NonPlayable nonPlayable = new NonPlayable();
        nonPlayable.setName(name);
        if (characterType != null)
            nonPlayable.setType(characterType);
        nonPlayable.setAttacks(attacks);
        nonPlayable.setMaxCharge(maxCharge);
        nonPlayable.setHealth_bars(health);
        nonPlayable.setEffects(effects);
        nonPlayable.setInitial_stats(stats);

        CharacterConnection characterConnection = new CharacterConnection();
        characterConnection.add(nonPlayable);


    }

    public List<Triplet> readEffects()
    {
        List<Effect> effects = new EffectConnection().read();
        List<Triplet> triplets = new ArrayList<>();
        Printer.printEffects(effects);
        while(true)
        {
            System.out.println("Choose an effect by number or 0 to finish adding effects: ");
            int choice = Reader.readIntInRange(0, effects.size());
            if (choice == 0)
            {
                return triplets;
            }
            System.out.println("Enter the duration for this effect: ");
            int duration = Reader.readIntInRange(-1, Integer.MAX_VALUE);
            System.out.println("Enter the scale for this effect: ");
            float scale = Reader.readFloatInRange(0, Float.MAX_VALUE);
            Effect effect = effects.get(choice - 1);
            Triplet triplet = new Triplet(duration, scale, effect.getName());
            triplets.add(triplet);
        }

    }

    public void addPlayableCharacter()
    {
        Printer.clearTerminal();
        System.out.println("Enter the character name: ");
        String name = Reader.readString();
        CharacterType characterType = readType();

        int choice;
        System.out.println("Do you want to add attacks? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        List<Attack> attacks = new ArrayList<>();
        if (choice == 1)
        {
            attacks = readAttacks();
        }



        List<Skill> skills = new ArrayList<>();
        System.out.println("Do you want to add skills? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        if (choice == 1)
        {
            skills = readSkills();
        }

        System.out.println("Do you want to add stats? (0 for no, 1 for yes)");
        choice = Reader.readIntInRange(0, 1);
        Stats stats = new Stats();
        if (choice == 1)
        {
            stats = readStats();
        }
        else
        {
            System.out.println("No stats added. Default stats will be used.");
        }

        Playable playable = new Playable();
        playable.setName(name);
        if (characterType != null)
            playable.setType(characterType);
        playable.setSkills(skills);
        playable.setAttacks(attacks);
        playable.setInitial_stats(stats);
        playable.setCurrent_stats(stats);

        CharacterConnection characterConnection = new CharacterConnection();
        int id = characterConnection.add(playable);
    }


    public List<Attack> readAttacks()
    {
        List<Attack> allAttacks = new AttackConnection().read();
        Set<Attack> attacks = new HashSet<>();
        Printer.printAttacks(allAttacks);
        while(attacks.size()<3)
        {
            System.out.println("Choose an attack by number or 0 to finish adding attacks: ");
            int choice = Reader.readIntInRange(0,allAttacks.size());
            if (choice ==0)
            {
                return new ArrayList<>(attacks);
            }
            if ( attacks.contains(allAttacks.get(choice - 1)))
            {
                System.out.println("You already added this attack. ");
                continue;
            }
            attacks.add(allAttacks.get(choice - 1));
        }
        return new ArrayList<>(attacks);
    }

    public List<Skill> readSkills()
    {
        List<Skill> skillList = PrepareGame.getInstance().getSkillsList();
        Set<Skill> skills = new HashSet<>();
        Printer.printSkills(skillList);
        while(skills.size()<3) {
            System.out.println("Choose a skill by number or 0 to finish adding skills: ");
            int choice = Reader.readIntInRange(0, skillList.size());
            if (choice == 0)
            {
                return new ArrayList<>(skills);
            }
            Skill skill = skillList.get(choice - 1);
            if (skills.contains(skill))
            {
                System.out.println("You already added this skill. ");
                continue;
            }
            skills.add(skill);
        }
        return new ArrayList<>(skills);
    }


    public Stats readStats()
    {

        Stats stats = new Stats();
        System.out.println("Enter the attack: ");
        stats.setAttack(Reader.readIntInRange(0, Integer.MAX_VALUE));
        System.out.println("Enter the health: ");
        stats.setHealth(Reader.readIntInRange(0, Integer.MAX_VALUE));
        System.out.println("Enter the damage resistance: ");
        stats.setDamageRes(Reader.readFloatInRange(0, 1));
        System.out.println("Enter the crit rate: ");
        stats.setCritRate(Reader.readFloatInRange(0, 1));
        System.out.println("Enter the crit damage: ");
        stats.setCritDamage(Reader.readFloatInRange(0, Float.MAX_VALUE));
        System.out.println("Enter the gain: ");
        stats.setGain(Reader.readFloatInRange(0, Float.MAX_VALUE));
        return stats;
    }


    private List<Playable> selectFightingCharacters()
    {
        List<Playable> characters = user.getPlayableCharacters();
        if (characters.isEmpty())
        {
            System.out.println("You have no playable characters.");
            Printer.sleep(2000);
            return new ArrayList<>();
        }
        Printer.printCharacters(characters);
        List<Playable> fightingCharacters = new ArrayList<>();
        System.out.println("Choose characters to fight ( max 3 ). Enter 0 to finish selecting characters: ");
        while (fightingCharacters.size()<3)
        {

            int choice = Reader.readIntInRange(0, characters.size());
            if (choice == 0)
            {
                if (fightingCharacters.isEmpty())
                {
                    System.out.println("You must select at least one character to fight.");
                    Printer.sleep(2000);
                }
                return fightingCharacters;
            }
            if (fightingCharacters.contains(characters.get(choice - 1)))
            {
                System.out.println("You can't have duplicates.");
                continue;
            }
            fightingCharacters.add(characters.get(choice - 1));
        }
        return fightingCharacters;
    }

    private List<Battle> selectBattles()
    {
        List<Battle> battles = PrepareGame.getInstance().getBattles();
        if (battles.isEmpty())
        {
            System.out.println("No battles found.");
            Printer.sleep(2000);
            return new ArrayList<>();
        }
        Printer.printBattles(battles);
        List<Battle> selectedBattles = new ArrayList<>();
        System.out.println("Choose battles to fight in the order they were chosen. Enter 0 to finish selecting battles: ");
        while(true)
        {
            int choice = Reader.readIntInRange(0, battles.size());
            if (choice == 0)
            {
                if (selectedBattles.isEmpty())
                {
                    System.out.println("You must select at least one battle to fight.");
                    Printer.sleep(2000);
                }
                return selectedBattles;
            }

            selectedBattles.add(battles.get(choice - 1));
        }

    }

    private void start_battle()
    {
        Printer.clearTerminal();
        List<Battle> battles = selectBattles();
        if (battles.isEmpty())
        {
            return;
        }
        List<Playable> fightingCharacters = selectFightingCharacters();

        if (fightingCharacters.isEmpty())
        {
            return;
        }
        GameState gameState = GameState.getInstance();
        gameState.setGameState(fightingCharacters, battles);
        System.out.println("askhjfg skas");
        gameState.printGameState();
        gameState.startFight();

    }

}


