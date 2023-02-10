package com.ngichtech.kenyaussd.adapters;


import static com.ngichtech.kenyaussd.custom.ISPConstants.ARRAY_TYPE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.DRAWABLE_TYPE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_LOGO_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.USSD_CODE_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.USSD_CODE_NAME_EXT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ISPContentAdapter extends RecyclerView.Adapter<ISPContentAdapter.ContentViewHolder> {
    List<String> ussdCodeNames;
    List<String> ussdCodes;
    Context context;
    Drawable ispLogoIcon;

    @SuppressLint("DiscouragedApi")
    public ISPContentAdapter(Context context, String isp_name) {
        this.context = context;
        ussdCodeNames = new ArrayList<>();
        ussdCodes = new ArrayList<>();
        int codeNamesIdentifier = context.getResources().getIdentifier(isp_name + USSD_CODE_NAME_EXT, ARRAY_TYPE, context.getPackageName());
        ussdCodeNames = Arrays.asList(context.getResources().getStringArray(codeNamesIdentifier));

        int codeIdentifier = context.getResources().getIdentifier(isp_name + USSD_CODE_EXT, ARRAY_TYPE, context.getPackageName());
        ussdCodes = Arrays.asList(context.getResources().getStringArray(codeIdentifier));

        int drawableIdentifier = context.getResources().getIdentifier(isp_name + ISP_LOGO_EXT, DRAWABLE_TYPE, context.getPackageName());
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
        holder.ussdCode.setText(ussdCodes.get(position));
        holder.ispLogoImage.setImageDrawable(ispLogoIcon);
        holder.cardViewLayout.setOnClickListener(v -> {
            // TODO: Extract this and use it to handle data from user input dialog
            Uri dialReqUri = Uri.fromParts("tel", ussdCodes.get(position), null);
            context.startActivity(new Intent(Intent.ACTION_CALL, dialReqUri));
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
