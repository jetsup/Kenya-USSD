package com.ngichtech.kenyaussd.adapters;

import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_SLOGAN_EXT;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.CodeContent;
import com.ngichtech.kenyaussd.R;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ISPAdapter extends RecyclerView.Adapter<ISPAdapter.MyISPViewHolder> {
    Context context;
    List<String> ispNames;
    List<String> ispSlogans;

    public ISPAdapter(Context context) {
        this.context = context;
        ispNames = Arrays.asList(context.getResources().getStringArray(R.array.isp_names));
        ispSlogans = Arrays.asList(context.getResources().getStringArray(R.array.isp_slogans));
    }

    @NonNull
    @Override
    public ISPAdapter.MyISPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main_activity, parent, false);
        return new MyISPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ISPAdapter.MyISPViewHolder holder, int position) {
        holder.ispLogo.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), getIspLogo(position), null));
        holder.ispName.setText(ispNames.get(position));
        holder.ispSlogan.setText(ispSlogans.get(position));
        holder.ispMainLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(context, CodeContent.class);
            intent.putExtra(ISP_NAME_EXT, ispNames.get(position));
            intent.putExtra(ISP_SLOGAN_EXT, ispSlogans.get(position));
            context.startActivity(intent);
        });
    }

    private int getIspLogo(int ispPosition) {
        switch (ispPosition) {
            case 0:
                return R.drawable.safaricom_logo;
            case 1:
                return R.drawable.airtel_logo;
            case 2:
                return R.drawable.telkom_logo;
            default:
                return R.drawable.hck;
        }
    }

    @Override
    public int getItemCount() {
        return ispNames.size();
    }

    public static class MyISPViewHolder extends RecyclerView.ViewHolder {
        CardView ispMainLayout;
        CircleImageView ispLogo;
        TextView ispName, ispSlogan;

        public MyISPViewHolder(@NonNull View itemView) {
            super(itemView);
            ispMainLayout = itemView.findViewById(R.id.isp_main_layout);
            ispLogo = itemView.findViewById(R.id.isp_main_logo);
            ispName = itemView.findViewById(R.id.serviceProvider);
            ispSlogan = itemView.findViewById(R.id.serviceProviderSlogan);
        }
    }
}
