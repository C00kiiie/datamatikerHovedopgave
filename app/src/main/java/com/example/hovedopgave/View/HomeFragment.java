package com.example.hovedopgave.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hovedopgave.Controller.ImageAdapter;
import com.example.hovedopgave.Controller.UserCheck;
import com.example.hovedopgave.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    TextView magazineToGo;
    CardView cardView;
    ImageView imageView;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> arrayArticle = new ArrayList();
    private ArrayList<String> arrayOffers = new ArrayList();
    private String userInterestOffers;
    private String userInterest;

    UserCheck userCheck = new UserCheck();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        magazineToGo = rootView.findViewById(R.id.home_cardViewToGoText);
        cardView = rootView.findViewById(R.id.home_cardView);
        imageView = rootView.findViewById(R.id.home_cardViewProgressbarImage);

        setProgressBar();

        try {
            checkAutoPay();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        onClick();
        getArticles();
        getImagesOffers();
        getMagazineToGo();



        return rootView;
    }

    public void getMagazineToGo(){
        DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int issues = document.getLong("Issues_needed").intValue();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        magazineToGo.setText("Du mangler kun " + issues + " magasiner for at få din næste gave!" );

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public void setProgressBar(){
        final DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    int issue = documentSnapshot.getLong("Issues_needed").intValue();

                    if (issue == 7){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar01);
                    } else if (issue == 6){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar02);
                    } else if (issue == 5){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar03);
                    } else if (issue == 4){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar04);
                    } else if (issue == 3){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar05);
                    } else if (issue == 2){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar06);
                    } else if (issue == 1){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar07);
                    } else if (issue == 0){
                        imageView.setImageResource(R.drawable.egmontfremskridtbar08);
                    }
                }
            }
        });

    }

    public void getArticles(){
        DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    userInterest = document.getString("Interesse");
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        db.collection("Articles").whereEqualTo("Interesse", userInterest).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        Log.d(TAG, doc.getString("Picture"));

                                        arrayArticle.add(doc.getString("Picture"));

                                        ViewPager viewPager = getActivity().findViewById(R.id.home_articleViewPager);
                                        ImageAdapter adapter = new ImageAdapter(getActivity(), arrayArticle);

                                        TabLayout tabLayout = getActivity().findViewById(R.id.tabDots);
                                        tabLayout.setupWithViewPager(viewPager, true);

                                        viewPager.setAdapter(adapter);

                                    }
                                } else {
                                    Log.d(TAG,"No interrest matching");
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void getImagesOffers(){
        DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    userInterestOffers = document.getString("Interesse");
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        db.collection("Offers").whereEqualTo("Interesse", userInterestOffers).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        Log.d(TAG, doc.getString("Picture"));

                                        arrayOffers.add(doc.getString("Picture"));

                                        final ViewPager viewPager = getActivity().findViewById(R.id.home_viewPager_offers);
                                        ImageAdapter adapter = new ImageAdapter(getActivity(), arrayOffers);

                                        TabLayout tabLayout = getActivity().findViewById(R.id.tabDots2);
                                        tabLayout.setupWithViewPager(viewPager, true);

                                        viewPager.setAdapter(adapter);


                                    }
                                } else {
                                    Log.d(TAG,"No interrest matching");
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void onClick(){

        final DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    final int issues = document.getLong("Issues_needed").intValue();
                    final int points = document.getLong("Loyal_point").intValue();

                    if (issues != 0){
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());

                                alertadd.setTitle("Hov!");
                                alertadd.setMessage("du mangler stadig " + issues + " inden du kan indløse din gave");
                                alertadd.setNegativeButton("Luk", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {

                                    }
                                });

                                alertadd.show();
                            }
                        });

                    } else if (issues == 0) {

                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                //final View view = factory.inflate(R.layout.activity_pop_up, null);
                                //alertadd.setView(view);
                                alertadd.setTitle("Hej!");
                                alertadd.setMessage("Du har modtaget nok magasiner, til at kunne indløse dine gave!");
                                alertadd.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {
                                        //Presses button to cancel dialog
                                        return;
                                    }
                                });
                                alertadd.setPositiveButton("Indløs!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {

                                        int newPoints = points + 4;

                                        userRef.update("Loyal_point", newPoints).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                                                userRef.update("Issues_needed",7); //Resets the counter for issues needed, for the next gift

                                                //Makes another alertbox, telling the user that his points has been added to his account
                                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
                                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                                //final View view = factory.inflate(R.layout.activity_pop_up, null);
                                                //alertadd.setView(view);
                                                alertadd.setTitle("Tillykke!");
                                                alertadd.setMessage("Vi har opdateret dine points!");
                                                alertadd.setPositiveButton("Se mine points", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        // Creating a new fragment, so that an intent can happen
                                                        Fragment newFragment = new OffersFragment();
                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                        // Replace whatever is in the fragment_container view with this fragment,
                                                        // and add the transaction to the back stack
                                                        transaction.replace(R.id.fragment_container, newFragment);
                                                        transaction.addToBackStack(null);

                                                        // Commit the transaction
                                                        transaction.commit();

                                                    }
                                                });

                                                alertadd.show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                                    }
                                });

                                alertadd.show();
                            }
                        });

                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }

    public void checkAutoPay() throws ParseException {
        Log.d(TAG, "Calling checkAutoPay Method()");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        Log.d(TAG, "current date: " + dateFormat.format(date));

        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Abonnoment").whereEqualTo("Next_issue", dateFormat.format(date))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String docName = doc.getId();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date newDate = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(newDate);
                        cal.add(Calendar.MONTH, 1);
                        newDate = cal.getTime();

                        String fromAccount = doc.getString("fromAccount");


                        Log.d(TAG, "printing found id's: " + doc.getId() + "with pay date set for: " + dateFormat.format(newDate));
                        userCheck.payAuto(docName, dateFormat.format(newDate));
                        //db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Accounts").document(docName).delete();

                    }

                } else {
                    Log.d(TAG, "onComplete: checking for autopayments, something went wrong: " + task.getException());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"onFailure, could get doc's" + e.getMessage());

            }
        });
    }

}
