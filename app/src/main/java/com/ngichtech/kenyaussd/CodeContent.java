package com.ngichtech.kenyaussd;

import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_AIRTEL;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_FAIBA;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_SAFARICOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_TELKOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_SLOGAN_EXT;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.adapters.ISPContentAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CodeContent extends AppCompatActivity {
    String ispNameReceived, ispSloganReceived;
    TextView sloganHeaderText;
    RecyclerView ussdItemRecycler;
    ISPContentAdapter ispContentAdapter;
    Map<String, String> ispDeco = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_isp);
        sloganHeaderText = findViewById(R.id.sloganHeaderText);
        ussdItemRecycler = findViewById(R.id.ussd_items_recycler);
        ussdItemRecycler.setLayoutManager(new LinearLayoutManager(this));

        ispDeco.put(ISP_NAME_SAFARICOM, "The Better Option");
        ispDeco.put(ISP_NAME_AIRTEL, "The Smartphone Network");
        ispDeco.put(ISP_NAME_TELKOM, "Moving Forward");
        ispDeco.put(ISP_NAME_FAIBA, "I am Future Proof");

        ispNameReceived = getIntent().getStringExtra(ISP_NAME_EXT);
        ispSloganReceived = getIntent().getStringExtra(ISP_SLOGAN_EXT);

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setTitle(ispNameReceived);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sloganHeaderText.setText(ispDeco.get(ispNameReceived));

        ispContentAdapter = new ISPContentAdapter(CodeContent.this, ispNameReceived.toLowerCase());
        ussdItemRecycler.setAdapter(ispContentAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
