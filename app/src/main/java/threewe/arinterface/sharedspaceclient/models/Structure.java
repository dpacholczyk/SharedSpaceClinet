package threewe.arinterface.sharedspaceclient.models;


import threewe.arinterface.sharedspaceclient.utils.State;

/**
 * Created by dpach on 13.11.2016.
 */

public class Structure {
    public Long id;

    public String name;

    public String definition;

    public Structure() {

    }

    public Structure(Long id, String name, String definition) {
        this.id = id;
        this.name = name;
        this.definition = definition;
    }

    public static Structure findStructureByName(String name) {
        for(Structure structure : State.currentSession.structures) {
            if(structure.name.equals(name)) {
                return structure;
            }
        }

        return null;
    }

}
