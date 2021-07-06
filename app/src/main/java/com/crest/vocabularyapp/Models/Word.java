package com.crest.vocabularyapp.Models;

import java.io.Serializable;

public class Word implements Serializable {
    private String wordName, definition, mnemonic, type;
    private int collectionId;

    public Word(String wordName, String definition, String mnemonic, String type) {
        this.wordName = wordName;
        this.definition = definition;
        this.mnemonic = mnemonic;
        this.type = type;
    }

    public Word() {

    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
}
