package com.ngichtech.kenyaussd;

import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_AIRTEL_CUSTOMER_CARE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_FAIBA_CUSTOMER_CARE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_AIRTEL;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_FAIBA;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_SAFARICOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_TELKOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_SAFARICOM_CUSTOMER_CARE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_SLOGAN_EXT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_TELKOM_CUSTOMER_CARE;
import static com.ngichtech.kenyaussd.custom.ISPConstants.SELECT_SIM_SLOT;
import static com.ngichtech.kenyaussd.custom.ISPConstants.SIM_CARD_PRESENT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.adapters.ISPAdapter;
import com.ngichtech.kenyaussd.adapters.ISPContentAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CodeContent extends AppCompatActivity {
    final String TAG = "MyTag";
    String ispNameReceived, ispSloganReceived;
    TextView sloganHeaderText;
    RecyclerView ussdItemRecycler;
    ISPContentAdapter ispContentAdapter;
    Map<String, String> ispDeco = new HashMap<>();
    Map<String, String> ispCustomerCare = new HashMap<>();
    private boolean simPresent = false;
    private int simSlot = 99;
    private String ispName;
    private boolean simMatched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_isp);
        ussdItemRecycler = findViewById(R.id.ussd_items_recycler);
        ussdItemRecycler.setLayoutManager(new LinearLayoutManager(this));

        ispDeco.put(ISP_NAME_SAFARICOM, "The Better Option");
        ispDeco.put(ISP_NAME_AIRTEL, "The Smartphone Network");
        ispDeco.put(ISP_NAME_TELKOM, "Moving Forward");
        ispDeco.put(ISP_NAME_FAIBA, "I am Future Proof");

        ispCustomerCare.put(ISP_NAME_SAFARICOM, ISP_SAFARICOM_CUSTOMER_CARE);
        ispCustomerCare.put(ISP_NAME_AIRTEL, ISP_AIRTEL_CUSTOMER_CARE);
        ispCustomerCare.put(ISP_NAME_TELKOM, ISP_TELKOM_CUSTOMER_CARE);
        ispCustomerCare.put(ISP_NAME_FAIBA, ISP_FAIBA_CUSTOMER_CARE);

        ispNameReceived = getIntent().getStringExtra(ISP_NAME_EXT);
        ispSloganReceived = getIntent().getStringExtra(ISP_SLOGAN_EXT);
        simPresent = getIntent().getBooleanExtra(SIM_CARD_PRESENT, false);

        ispName = ispNameReceived;
        if (simPresent) {
            List<SubscriptionInfo> simInformation = ISPAdapter.simInformation;
            for (SubscriptionInfo sim : simInformation) {
                if (sim.getCarrierName().toString().equals(ispNameReceived)) {
                    simSlot = sim.getSimSlotIndex();
                    ispName = sim.getCarrierName().toString();
                    simMatched = true;
                }
            }
        }
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setTitle(ispNameReceived);
        actionBar.setSubtitle(ispSloganReceived);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sloganHeaderText.setText(ispDeco.get(ispNameReceived));
        Log.w("MyTag", "Slot: " + simSlot + " <> " + simMatched + " <> " + ispNameReceived + " <> ");
        if (simMatched) {
            ispContentAdapter = new ISPContentAdapter(CodeContent.this, ispNameReceived.toLowerCase(), simSlot);
        } else {
            ispContentAdapter = new ISPContentAdapter(CodeContent.this, ispNameReceived.toLowerCase());
        }
        ussdItemRecycler.setAdapter(ispContentAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.isp_content_menu, menu);
        if (simSlot != 99) {
            menu.getItem(1).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.contentSearch) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ispContentAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            return true;
        } else if (item.getItemId() == R.id.contentCallProvider) {
            if (simPresent) {
                Uri numberToCall = Uri.parse("tel:" + ispCustomerCare.get(ispName));
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(numberToCall);
                callIntent.putExtra(SELECT_SIM_SLOT, simSlot);
                // TODO: Display a dialog to confirm user want to call
                startActivity(callIntent);
            }
            Toast.makeText(this, "Calling Safaricom: " + ispCustomerCare.get(ispName), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
