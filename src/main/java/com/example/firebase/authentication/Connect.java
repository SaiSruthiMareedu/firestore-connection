package com.example.firebase.authentication;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import java.util.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Connect {
    public static void setData( Firestore db){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter EmpId");
        String empId = scan.nextLine();

        System.out.println("Enter name");
        String name = scan.nextLine();

        System.out.println("Enter org");
        String org = scan.nextLine();

        System.out.println("Enter age");
        String age = scan.nextLine();
        DocumentReference docRef2 = db.collection("user").document(empId);
        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", name);
        data2.put("org", org);
        data2.put("age",age);
        ApiFuture<WriteResult> result = docRef2.set(data2);


    }
    public static void getData(Firestore db) throws ExecutionException, InterruptedException {
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("user").get();

        // retrieving the data

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("ID: " + document.getId());
            System.out.println("Name: " + document.getString("name"));
            System.out.println("Organisation: " + document.getString("org"));
            System.out.println("Age: " + document.getString("age"));
        }


    }
    public static void getDataId(Firestore db ) throws ExecutionException, InterruptedException {
        // asynchronously retrieve all users
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter document id to display details ");
        String id = scan.nextLine();
        ApiFuture<QuerySnapshot> query = db.collection("user").get();
        int tag = 0;
        // retrieving the data

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getId().equals(id)) {
                System.out.println("ID: " + document.getId());
                System.out.println("Name: " + document.getString("name"));
                System.out.println("Organisation: " + document.getString("org"));
                System.out.println("Age: " + document.getString("age"));
                tag = 1;
            }
        }
        if(tag == 0){
            System.out.println("Record doesn't exists");
        }


    }

    public static Firestore quickStart() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("/home/sruthi/Downloads/testpro-ea6d4-firebase-adminsdk-9sv71-c6f015fcab.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://testpro-ea6d4.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);

// As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("restricted_access/secret_document");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }


            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        Firestore db = FirestoreClient.getFirestore();
        return db;
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Firestore db = null;

        Scanner scan = new Scanner(System.in);
        db = quickStart();
        int tag =0;
        do {
            System.out.println("Enter the crud operations to be performed \n 1) Add document to the collection user\n  2) Get all the documents from the collection \n3) Get a specified document based on ID ");
            int i = scan.nextInt();
            switch (i){
                case 1:

                    setData( db);
                    break;
                case 2:
                    getData(db);
                    break;
                case 3:

                    getDataId(db);
                    break;
            }
            System.out.println("To continue press 1");
            tag = scan.nextInt();
        }while(tag == 1);



    }


}