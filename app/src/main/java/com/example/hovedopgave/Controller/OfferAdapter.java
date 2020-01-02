package com.example.hovedopgave.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hovedopgave.Model.Offer;
import com.example.hovedopgave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageUrls;
        public TextView textView, textInfo;
        public Button button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrls = itemView.findViewById(R.id.offer_recycleImageView);
            textView = itemView.findViewById(R.id.offer_recycleTextIsNew);
            button = itemView.findViewById(R.id.offer_recycleButton);
            textInfo = itemView.findViewById(R.id.offer_recycleTextInfo);

        }


    }

    private ArrayList<Offer> mOfferList;

    public OfferAdapter(ArrayList<Offer> offerList){
        mOfferList = offerList;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_offers, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder offerViewAdapter, final int position) {

        final Offer currentItem = mOfferList.get(position);


        //Checks if an offer has the "new" tag. if not, make the textView.background insvible
        if (currentItem.getTextNew() == ""){
            offerViewAdapter.textView.setBackgroundColor(Color.parseColor("#00000000"));
            offerViewAdapter.textView.setVisibility(View.INVISIBLE);
        } else {
            offerViewAdapter.textView.setText(currentItem.getTextNew());
        }

        offerViewAdapter.textInfo.setText(currentItem.getTextInfo());

        //Picasso is used to get the image and load it into the ImageView
        Picasso.get()
                .load(currentItem.getPicture())
                .into(offerViewAdapter.imageUrls);

        //Set an onClickListener. This allows me to make an intent for an outside website.
        offerViewAdapter.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getButtonUrl()));
                v.getContext().startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mOfferList.size();
    }

}
