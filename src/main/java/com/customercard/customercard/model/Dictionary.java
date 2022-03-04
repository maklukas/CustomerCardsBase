package com.customercard.customercard.model;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public abstract class Dictionary extends AbstractEntity {

    @Indexed(unique = true)
    @NotNull
    private String name;

    public Dictionary(String name) {
        this.name = name;
    }

    public Dictionary() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
