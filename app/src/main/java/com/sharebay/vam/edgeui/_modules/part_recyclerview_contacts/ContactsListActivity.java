package com.sharebay.vam.edgeui._modules.part_recyclerview_contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.sharebay.vam.edgeui.R;

public class ContactsListActivity extends AppCompatActivity {

    RecyclerView recyclerView_constatctsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        initView();
    }

    private void initView() {
        recyclerView_constatctsList = (RecyclerView) findViewById(R.id.recyclerView_constatctsList);
    }
}
