package com.example.agentie_imobiliara.model;

import java.util.Objects;

public class SavedHouses {
    private String email;
    private String key;

    public SavedHouses()
    {

    }

    public SavedHouses(String email, String key) {
        this.email = email;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedHouses that = (SavedHouses) o;
        return email.equals(that.email) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, key);
    }

    @Override
    public String toString() {
        return "SavedHouses{" +
                "email='" + email + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
