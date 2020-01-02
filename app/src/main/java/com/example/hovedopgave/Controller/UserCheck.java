package com.example.hovedopgave.Controller;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserCheck {

    private static final String TAG = "UserCheck";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void payAuto(final String fromAccount, final String date) {
        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Abonnoment").document(fromAccount)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            db.collection("Users").document(mAuth.getCurrentUser().getEmail())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        int issuesNeeded = document.getLong("Issues_needed").intValue();
                                        int loyalPoints = document.getLong("Loyal_point").intValue();
                                        String userInterest = document.getString("Interesse");
                                        int newIssuesNeeded = issuesNeeded - 1;
                                        int newLoyalPoints = loyalPoints + 1;

                                        Map<String, Object> data2 = new HashMap<>();
                                        data2.put("Loyal_point", newLoyalPoints);
                                        data2.put("Issues_needed", newIssuesNeeded);
                                        data2.put("Interesse", userInterest);
                                        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).set(data2);

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Next_issue", date);
                                        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Abonnoment").document(fromAccount).set(data);

                                    }
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


}
