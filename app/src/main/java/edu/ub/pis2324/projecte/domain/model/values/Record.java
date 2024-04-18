package edu.ub.pis2324.projecte.domain.model.values;

import java.util.HashMap;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

public class Record {
    private static final HashMap<String, Record> instances = new HashMap<>();
    private String userId;
    private HashMap<String, Recipe> recipes;

    private Record(String userId) {
        this.userId = userId;
        this.recipes = new HashMap<>();
    }

    public static Record getInstance(String userId) {
        if (!instances.containsKey(userId)) {
            instances.put(userId, new Record(userId));
        }
        return instances.get(userId);
    }
}
