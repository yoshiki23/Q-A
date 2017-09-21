package com.example.haruka.rescue_aid.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.TempDataUtil;
import com.example.haruka.rescue_aid.utils.Utils;

/**
 * Created by Tomoya on 9/21/2017 AD.
 */

public class CertificationLoadActivity extends AppCompatActivity {

    Context contextLoadDataActivity;
    LinearLayout linearLayout;


    private void setView(){
        String[] filenames = fileList();
        for(int i = 1; i < filenames.length; i++){
            final String filename = filenames[i];

            Button b = new Button(this);
            b.setText(filename);
            linearLayout.addView(b);

            MedicalCertification medicalCertification = TempDataUtil.load(contextLoadDataActivity, filename);
            medicalCertification.showRecords();

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicalCertification medicalCertification = TempDataUtil.load(contextLoadDataActivity, filename);
                    Intent intent = new Intent(contextLoadDataActivity, CertificationEditActivity.class);
                    intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
        contextLoadDataActivity = this;

        setView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load_data, menu);
        return true;
    }

    static String TAG = "Menu";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_delete_certification:
                new AlertDialog.Builder(contextLoadDataActivity).setMessage("すべての問診履歴を消去します\nよろしいですか").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] filenames = fileList();
                        for (String filename : filenames){

                            deleteFile(filename);
                            linearLayout.removeAllViews();
                        }
                    }
                }).show();
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        linearLayout.removeAllViews();
        setView();
    }
}