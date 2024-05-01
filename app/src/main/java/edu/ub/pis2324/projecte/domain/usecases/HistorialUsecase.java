package edu.ub.pis2324.projecte.domain.usecases;

import java.util.HashMap;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import edu.ub.pis2324.projecte.data.UserRepository;
/*
import edu.ub.pis2324.projecte.data.RecipeRepository;

public class HistorialUsecase {
    //TODO: FALTAN OBSERVERS QUE UTILIZEN LOS METODOS DE ESTA CLASE
    //TODO: FALTAN REPOSITORIOS QUE RELACIONEN ESTE USECASE CON LA BASE DE DATOS
    private static HashMap<User, Record> records;

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

    public User getUser(String userId) {
        return UserRepository.getInstance().getById(userId);
    }

    public Recipe getRecipe(String recipeId) {
        return RecipeRepository.getInstance().getById(recipeId);
    }

    public void addRecord(String userId) {
        if(!records.containsKey(getUser(userId))) records.put(getUser(userId), new Record(getUser(userId)));
        else throw new IllegalArgumentException("User already has a record");
    }

    public Record getRecord(String userId) {
        return records.get(getUser(userId));
    }

    public void removeRecord(String userId) {
        records.remove(getUser(userId));
    }

    public void clear() {
        records.clear();
    }

    public boolean containsRecord(String userId) {
        return records.containsKey(getUser(userId));
    }

    public void addRecipe(String userId, String recipeId) {
        Record record = records.get(getUser(userId));
        if(record == null) throw new IllegalArgumentException("Record not found");
        record.addRecipe(getRecipe(recipeId));
    }

    public Recipe getRecipe(String userId, String recipeId) {
        Record record = records.get(getUser(userId));
        if(record == null) throw new IllegalArgumentException("Record not found");
        return record.getRecipe(recipeId);
    }

    public void removeRecipe(String userId, String recipeId) {
        Record record = records.get(getUser(userId));
        if(record == null) throw new IllegalArgumentException("Record not found");
        record.removeRecipe(recipeId);
    }

    public void clearRecord(String userId) {
        if (!records.containsKey(getUser(userId))) throw new IllegalArgumentException("User does not have a record");
        Record record = records.get(getUser(userId));
        if(record == null) throw new IllegalArgumentException("Record not found");
        if (record.isEmpty()) throw new IllegalArgumentException("Record is already empty");
        record.clear();
    }
public interface HistorialUsecase {
}
