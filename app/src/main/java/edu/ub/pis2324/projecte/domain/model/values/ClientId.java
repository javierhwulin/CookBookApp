package edu.ub.pis2324.projecte.domain.model.values;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class ClientId implements Serializable {
    private final String id;
    public ClientId(String id) {
        this.id = id;
    }

    public ClientId() {
        this.id = "OOO";
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
        ClientId clientId = (ClientId) obj;
        return Objects.equals(id, clientId.id);
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
