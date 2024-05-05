package edu.ub.pis2324.projecte.domain.model.values;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class RecipeId implements Serializable {
    private final String id;
    public RecipeId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RecipeId recipeId = (RecipeId) obj;
        return Objects.equals(id, recipeId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        String result;
        if (id == null) {
            result = "null";
        } else {
            result = id;
        }
        return result;
    }
}
