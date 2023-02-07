package com.ngichtech.kenyaussd.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.MainActivity;
import com.ngichtech.kenyaussd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ISPContentAdapter extends RecyclerView.Adapter<ISPContentAdapter.ContentViewHolder> {
    List<String> ussdCodeNames;
    List<String> ussdCode;
    Context context;
    Drawable ispLogoIcon;

    @SuppressLint("DiscouragedApi")
    public ISPContentAdapter(Context context, String isp_name) {
        this.context = context;
        ussdCodeNames = new ArrayList<>();
        ussdCode = new ArrayList<>();
        Log.w(MainActivity.TAG, "ISPContentAdapter: "+ isp_name + "_ussd_code_names");
        int codeNamesIdentifier = context.getResources().getIdentifier(isp_name + "_ussd_code_names", "array", context.getPackageName());
        ussdCodeNames = Arrays.asList(context.getResources().getStringArray(codeNamesIdentifier));
        Log.w(MainActivity.TAG, "ISPContentAdapter: "+codeNamesIdentifier +" <> "+ Arrays.toString(context.getResources().getStringArray(codeNamesIdentifier)));

        int codeIdentifier = context.getResources().getIdentifier(isp_name + "_ussd_codes", "array", context.getPackageName());
        ussdCode = Arrays.asList(context.getResources().getStringArray(codeIdentifier));

        int drawableIdentifier = context.getResources().getIdentifier(isp_name + "_logo", "drawable", context.getPackageName());
        ispLogoIcon = ResourcesCompat.getDrawable(context.getResources(), drawableIdentifier, null);
    }

    @NonNull
    @Override
    public ISPContentAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ussd_option_item_holder, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ISPContentAdapter.ContentViewHolder holder, int position) {
        holder.ussdCodeName.setText(ussdCodeNames.get(position));
        holder.ussdCode.setText(ussdCode.get(position));
        holder.ispLogoImage.setImageDrawable(ispLogoIcon);
        holder.cardViewLayout.setOnClickListener(v -> {
//            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Clicked index: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return ussdCodeNames.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewLayout;
        CircleImageView ispLogoImage;
        TextView ussdCodeName, ussdCode;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewLayout = itemView.findViewById(R.id.ussd_card_layout);
            ispLogoImage = itemView.findViewById(R.id.isp_content_logo);
            ussdCodeName = itemView.findViewById(R.id.ussd_code_name);
            ussdCode = itemView.findViewById(R.id.ussd_code);
        }

    }
}
