package com.crest.vocabularyapp.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLLECTION_TABLE = "COLLECTION";
    public static final String COLLECTION_TABLE_ID = "COLLECTION_ID";
    public static final String COLLECTION_TABLE_NAME = "COLLECTION_NAME";
    public static final String COLLECTION_TABLE_NOOFWORDS = "COLLECTION_NOOFWORDS";

    public static final String WORD_TABLE = "WORD";
    public static final String WORD_TABLE_ID = "WORD_ID";
    public static final String WORD_TABLE_NAME = "WORD_NAME";
    public static final String WORD_TABLE_DEFINITION = "WORD_DEFINITION";
    public static final String WORD_TABLE_MNEMONIC = "WORD_MNEMONIC";
    public static final String WORD_TABLE_TYPE = "WORD_TYPE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "vocabapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCollectionTable = "CREATE TABLE " + COLLECTION_TABLE + " (" + COLLECTION_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLLECTION_TABLE_NAME + " TEXT, " + COLLECTION_TABLE_NOOFWORDS + " INTEGER)";
        String createWordTable = "CREATE TABLE " + WORD_TABLE + " (" + WORD_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORD_TABLE_NAME + " TEXT, " + WORD_TABLE_DEFINITION + " TEXT, " + WORD_TABLE_MNEMONIC + " TEXT, " + WORD_TABLE_TYPE + " TEXT, " + COLLECTION_TABLE_ID + " INTEGER)";

        db.execSQL(createCollectionTable);
        db.execSQL(createWordTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTION_TABLE);
    }

    public boolean addOne(Word addWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(WORD_TABLE_NAME, addWord.getWordName());
        cv.put(WORD_TABLE_DEFINITION, addWord.getDefinition());
        cv.put(WORD_TABLE_MNEMONIC, addWord.getMnemonic());
        cv.put(WORD_TABLE_TYPE, addWord.getType());
        cv.put(COLLECTION_TABLE_ID, addWord.getCollectionId());

        long insert = db.insert(WORD_TABLE, null, cv);

        if (insert == -1) {
            return false;
        }

        return true;
    }

    public boolean addCollection(Collection addCollection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLLECTION_TABLE_NAME, addCollection.getName());
        cv.put(COLLECTION_TABLE_NOOFWORDS, addCollection.getNumberOfWords());

        long insert = db.insert(COLLECTION_TABLE, null, cv);

        if (insert == -1) {
            return false;
        }

        return true;
    }

    public boolean addWordToCollection(Word addWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(WORD_TABLE_NAME, addWord.getWordName());
        cv.put(WORD_TABLE_DEFINITION, addWord.getDefinition());
        cv.put(WORD_TABLE_MNEMONIC, addWord.getMnemonic());
        cv.put(WORD_TABLE_TYPE, addWord.getType());
        cv.put(COLLECTION_TABLE_ID, addWord.getCollectionId());

        long insert = db.insert(WORD_TABLE, null, cv);

        if (insert == -1) {
            return false;
        }

        return true;
    }

    public ArrayList<Collection> getCollections() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + COLLECTION_TABLE;
        ArrayList<Collection> collections = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Collection collection = new Collection();
                collection.setName(cursor.getString(cursor.getColumnIndex(COLLECTION_TABLE_NAME)));
                collection.setNumberOfWords(cursor.getInt(cursor.getColumnIndex(COLLECTION_TABLE_NOOFWORDS)));
                collection.setId(cursor.getInt(cursor.getColumnIndex(COLLECTION_TABLE_ID)));
                collections.add(collection);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return collections;
    }

    public boolean deleteCollection(int deleteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryDeleteCollection = "delete from " + COLLECTION_TABLE + " where " + COLLECTION_TABLE_ID + "=" + deleteId;
        String queryDeleteWords = "delete from " + WORD_TABLE + " where " + COLLECTION_TABLE_ID + "=" + deleteId;

        db.execSQL(queryDeleteCollection);
        db.execSQL(queryDeleteWords);

        return true;
    }

    public ArrayList<Word> getCollectionsWord(int collectionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + WORD_TABLE + " where " + COLLECTION_TABLE_ID + " = " + collectionId;
        ArrayList<Word> collectionsWord = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setCollectionId(cursor.getInt(cursor.getColumnIndex(COLLECTION_TABLE_ID)));
                word.setDefinition(cursor.getString(cursor.getColumnIndex(WORD_TABLE_DEFINITION)));
                word.setMnemonic(cursor.getString(cursor.getColumnIndex(WORD_TABLE_MNEMONIC)));
                word.setWordName(cursor.getString(cursor.getColumnIndex(WORD_TABLE_NAME)));
                word.setType(cursor.getString(cursor.getColumnIndex(WORD_TABLE_TYPE)));
                collectionsWord.add(word);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return collectionsWord;
    }

}
