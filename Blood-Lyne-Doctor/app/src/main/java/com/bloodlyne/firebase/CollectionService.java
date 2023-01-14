package com.bloodlyne.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public abstract class CollectionService<T> {

    String collectionName;
    FirebaseFirestore db;

    public CollectionService(String collectionName) {
        this.collectionName = collectionName;
        db = FirebaseFirestore.getInstance();
    }

    public Task<DocumentReference> addDocument(Map<String, String> document) {
        return db.collection(collectionName).add(document);
    }

    public Task<Void> addDocumentWithId(String documentId, Map<String, Object> document){
        return db.collection(collectionName).document(documentId).set(document);
    }

    public Task<com.google.firebase.firestore.DocumentSnapshot> getDocument(String documentId){
        return db.collection(collectionName).document(documentId).get();
    }

    public Task<com.google.firebase.firestore.QuerySnapshot> getCollection(){
        return db.collection(collectionName).get();
    }

    public Task<Void> updatedDocument(String documentId, Map<String, Object> document){
        return db.collection(collectionName).document(documentId).update(document);
    }

    public Task<Void> deleteDocument(String documentId){
        return db.collection(collectionName).document(documentId).delete();
    }
}
