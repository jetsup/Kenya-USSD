package com.ngichtech.kenyaussd;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

    {
        ispDeco.put("CodeContent", "The Better Option");
        ispDeco.put("Airtel", "The Smartphone Network");
        ispDeco.put("Telkom", "Moving Forward");
        ispDeco.put("Faiba", "I am Future Proof");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_isp);
        sloganHeaderText = findViewById(R.id.sloganHeaderText);
        ussdItemRecycler = findViewById(R.id.ussd_items_recycler);
        ussdItemRecycler.setLayoutManager(new LinearLayoutManager(this));

        ispNameReceived = getIntent().getStringExtra("ISP_NAME");
        ispSloganReceived = getIntent().getStringExtra("ISP_SLOGAN");
        Objects.requireNonNull(getSupportActionBar()).setTitle(ispNameReceived);
        sloganHeaderText.setText(ispDeco.get(ispNameReceived));

        ispContentAdapter = new ISPContentAdapter(CodeContent.this, ispNameReceived.toLowerCase());
        ussdItemRecycler.setAdapter(ispContentAdapter);
    }
}
