package com.crest.vocabularyapp.Models;

public class Collection {
    String name;
    int id, numberOfWords = 0;

    public Collection(String name, int numberOfWords) {
        this.name = name;
        this.numberOfWords = numberOfWords;
    }

    public Collection() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }
}
