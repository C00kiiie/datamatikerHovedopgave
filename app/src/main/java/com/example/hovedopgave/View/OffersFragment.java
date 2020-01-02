package com.example.hovedopgave.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hovedopgave.Controller.ImageAdapter;
import com.example.hovedopgave.Controller.OfferAdapter;
import com.example.hovedopgave.Model.Offer;
import com.example.hovedopgave.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class OffersFragment extends Fragment {

    private static final String TAG = "OffersFragment";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userInterest;


    TextView pointsText, needMoreText;
    ImageView extranummer;
    ArrayList<String> array = new ArrayList();
    ArrayList<Offer> offerList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        pointsText = rootView.findViewById(R.id.offers_points);

        getPoints();
        getImages();
        getOffers();

        return rootView;
    }

    public void getImages(){
        DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    userInterest = document.getString("Interesse");
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        db.collection("Offers").whereEqualTo("Interesse", userInterest).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        Log.d(TAG, doc.getString("Picture"));

                                        array.add(doc.getString("Picture"));

                                        ViewPager viewPager = getActivity().findViewById(R.id.offers_viewPager);
                                        ImageAdapter adapter = new ImageAdapter(getActivity(), array);
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

    public void getPoints(){
        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    int loyalPoints = document.getLong("Loyal_point").intValue();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        pointsText.setText(String.valueOf(loyalPoints) + " Point");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void getOffers(){
        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    final int loy_points = document.getLong("Loyal_point").intValue();

                    db.collection("CompanyDeals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    int dealPrice = doc.getLong("Cost").intValue();
                                    String isNew = doc.getString("IsNew");
                                    String textInfo = doc.getString("Text");

                                    if (loy_points >= dealPrice){
                                        offerList.add(new Offer(doc.getString("PictureColor"), doc.getString("ButtonUrl"), isNew, textInfo));
                                    } else if (loy_points < dealPrice) {
                                        offerList.add(new Offer(doc.getString("PictureBlack"), doc.getString("ButtonUrl"), isNew, textInfo));
                                    }
                                }
                                recyclerView = getActivity().findViewById(R.id.offers_recycleView);
                                recyclerView.hasFixedSize();
                                mLayoutManager = new LinearLayoutManager(getActivity());
                                mAdapter = new OfferAdapter(offerList);

                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(mAdapter);

                            }
                        }
                    });



                }
            }
        });
    }

}
