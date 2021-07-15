package com.crest.vocabularyapp.Models;

public class Mnemonic {

    private String mnemonic;
    private int likes;
    private int dislike;

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public Mnemonic(String mnemonic, int likes, int dislike) {
        this.mnemonic = mnemonic;
        this.likes = likes;
        this.dislike = dislike;
    }
}
