package edu.ub.pis2324.projecte.domain.usecases;

import java.util.HashMap;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.Record;

public class HistorialUsecase {
    private static HashMap<String, Record> records;

    private HistorialUsecase() {
        records = new HashMap<>();
    }

    public static HistorialUsecase getInstance() {
        if(records == null) {
            return new HistorialUsecase();
        }else{
            return null;
        }
    }

    public void addRecord(String userId, Record record) {
        records.put(userId, record);
    }

    public Record getRecord(String userId) {
        return records.get(userId);
    }

    public void removeRecord(String userId) {
        records.remove(userId);
    }

    public void clear() {
        records.clear();
    }

    public boolean containsRecord(String userId) {
        return records.containsKey(userId);
    }

    public void addRecipe(String userId, Recipe recipe) {
        Record record = records.get(userId);
        record.addRecipe(recipe);
    }

    public Recipe getRecipe(String userId, String recipeId) {
        Record record = records.get(userId);
        return record.getRecipe(recipeId);
    }

    public void removeRecipe(String userId, String recipeId) {
        Record record = records.get(userId);
        record.removeRecipe(recipeId);
    }

    public void clearRecord(String userId) {
        Record record = records.get(userId);
        record.clear();
    }
}
