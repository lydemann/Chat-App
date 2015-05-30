package com.strangerchat.strangerchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class GuiActivity extends Activity implements OnItemRecycleViewClickListener {

    RecyclerView mRecyclerView;
    private List<Data> mData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for(int x = 10; x < 20; x =x+1) {
          mData.add(new Data("Mor" + x, "Yo, pikfjs, hva sker der for dig"));
          mData.add(new Data("Far"+ x, "Yo, pikfjs, hva sker der for dig"));
          mData.add(new Data("Bror" + x, "Yo, pikfj, hva sker der for dig"));
        }
        setContentView(R.layout.activity_main2);

        mRecyclerView = (RecyclerView) findViewById(R.id.idRecyclerView);
        RelativeLayout StrangerLayout =(RelativeLayout)findViewById(R.id.StrangerLayout);//starnger chatt knappen

        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearManager);

        //          StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        //          mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //
        //          GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //          mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new RecyclerAdapter(mData, this));


        //"Knappen" til stranger chat
        StrangerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getBaseContext(), MessageActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onItemClicked(int position, RecyclerAdapter mAdapter) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG).show();
    }

}



