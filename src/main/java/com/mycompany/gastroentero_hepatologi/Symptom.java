package com.mycompany.gastroentero_hepatologi;

public class Symptom {
    private String id;
    private String name;
    private String question;

    public Symptom(String id, String name, String question) {
        this.id = id;
        this.name = name;
        this.question = question;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getQuestion() { return question; }

    @Override
    public String toString() {
        return name;
    }
}
