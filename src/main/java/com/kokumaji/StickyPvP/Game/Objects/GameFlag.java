package com.kokumaji.StickyPvP.Game.Objects;

import java.util.ArrayList;

public enum GameFlag {

    /**
     * TODO: implement game flag types (integer, boolean, string)
     * TODO: Add protection flag logic to lobby spawn
     */

    HUNGER("HUNGER", State.ALLOW),
    FALLDAMAGE("FALLDAMAGE", State.ALLOW),
    PLACE_BLOCK("PLACE_BLOCK", State.ALLOW),
    BREAK_BLOCK("BREAK_BLOCK", State.ALLOW),
    ITEM_PICKUP("ITEM_PICKUP", State.ALLOW),
    ITEM_DROP("ITEM_DROP", State.ALLOW),
    TOOL_DAMAGE("TOOL_DAMAGE", State.ALLOW),
    WEATHER_CYCLE("WEATHER_CYCLE", State.ALLOW),
    DAY_CYCLE("DAY_CYCLE", State.ALLOW),
    LEAF_DECAY("LEAF_DECAY", State.ALLOW),
    EXPLOSION_DAMAGE("EXPLOSION_DAMAGE", State.ALLOW),

    /**
     * TODO: fix stupid mob spawn flags
     */
    SPAWN_ANIMALS("SPAWN_ANIMALS", State.DENY),
    SPAWN_MONSTERS("SPAWN_MONSTERS", State.DENY);

    public static ArrayList<String> AsStringList(boolean colored) {
        ArrayList<String> gfStrings = new ArrayList<>();
        if(colored) {
            for(GameFlag gf : GameFlag.values()) {
                gfStrings.add("§7" + gf.GetIdentifier());
            }
        } else {
            for(GameFlag gf : GameFlag.values()) {
                gfStrings.add(gf.GetIdentifier());
            }
        }

        return gfStrings;
    }


    public enum State {
        ALLOW,
        DENY;

        public static State FromString(String s) {
            switch(s.toLowerCase()) {
                case "allow":
                case "true":
                    return ALLOW;
                case "deny":
                case "false":
                    return DENY;
            }

            return DENY;
        }
    }

    private final String identifier;
    private State state;

    GameFlag(String pIdentifier, State pState) {
        this.identifier = pIdentifier;
        this.state = pState;
    }

    public State GetState() {
        return this.state;
    }

    public void SetState(State pState) {
        this.state = pState;
    }

    public String GetIdentifier() {
        return this.identifier;
    }
}
