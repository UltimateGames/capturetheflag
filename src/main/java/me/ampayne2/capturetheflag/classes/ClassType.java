package me.ampayne2.capturetheflag.classes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ClassType {
    
    ARCHER(Archer.class, "archer"), 
    BUILDER(Builder.class, "builder"), 
    //DEMOLITIONIST(Demolitionist.class, "demolitionist"), 
    WARRIOR(Warrior.class, "warrior");

    private static final Map<Class<? extends ArenaClass>, ClassType> classTypes = new HashMap<Class<? extends ArenaClass>, ClassType>();
    private static final Map<String, ClassType> labels = new HashMap<String, ClassType>();

    static {
        for (ClassType classType : EnumSet.allOf(ClassType.class)) {
            classTypes.put(classType.getClassType(), classType);
            labels.put(classType.getLabel(), classType);
        }
    }

    private Class<? extends ArenaClass> classClass;
    private String label;

    private ClassType(Class<? extends ArenaClass> classClass, String label) {
        this.classClass = classClass;
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }

    public Class<? extends ArenaClass> getClassType() {
        return classClass;
    }

    public static ClassType fromClass(Class<? extends ArenaClass> arenaClass) {
        return classTypes.get(arenaClass);
    }
    
    public static ClassType fromLabel(String string) {
        return labels.containsKey(string) ? labels.get(string) : null;
    }
    
}
