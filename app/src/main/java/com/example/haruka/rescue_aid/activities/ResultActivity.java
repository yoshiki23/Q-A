package com.example.haruka.rescue_aid.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.CareList;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.ResultLineLayout;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/7/2017 AD.
 */

public class ResultActivity extends AppCompatActivity {

    private AssetManager assetManager;
    private MedicalCertification medicalCertification;
    private int urgency;
    private ArrayList<Care> cares;
    LinearLayout linearLayout;
    ScrollView scrollView;
    LinearLayout inflateLayout;
    TextView textView;
    Button dealingBtn;

    final int MATCH_P = ViewGroup.LayoutParams.MATCH_PARENT;

    CareList careList;



    private String[] menuActivities = null;
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList = null;
    private ActionBarDrawerToggle mDrawerToggle = null;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private void setLinearLayout(){
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        scrollView.addView(linearLayout);
    }

    private void setScrollView(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout_result);
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(MATCH_P, MATCH_P));
        ll.addView(scrollView);
    }

    private void setTextView(){
        textView = (TextView)findViewById(R.id.textview_notice_result); //
        //textView = new TextView(this);
        textView.setTextSize(34);

        textView.setTextColor(Utils.URGENCY_COLORS[urgency]);
        textView.setText(Utils.URGENCY_WARNING[urgency]);
        /*
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        */
        //linearLayout.addView(textView);
    }

    private void showCareList(){
        for (final Care c : cares){
            ResultLineLayout resultLineLayout = new ResultLineLayout(this);
            addDescription(c);
            resultLineLayout.setTitle(c.name);
            resultLineLayout.setDescription(c.description);
            resultLineLayout.setView();
            linearLayout.addView(resultLineLayout);
            android.view.ViewGroup.LayoutParams layoutParams = resultLineLayout.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            resultLineLayout.setLayoutParams(layoutParams);

            resultLineLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = c.index;
                    Log.d("result line", Integer.toString(i));
                }
            });
        }

    }

    private void setInflate(){
        inflateLayout = new LinearLayout(this);
        inflateLayout.setOrientation(LinearLayout.VERTICAL);

        inflateLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        inflateLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.inflate_result_, inflateLayout);
        linearLayout.addView(inflateLayout);

        String t = "";
        for(Care c : cares){
            t += c.name;
        }
        TextView tv = (TextView)findViewById(R.id.textview_inflate);
        tv.setText(t);

    }

    private void setDealingBtn(){
        final Intent intent = new Intent(this, ExplainActivity.class);

        dealingBtn = (Button)findViewById(R.id.btn_start_care); //new Button(this);
        dealingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("CERTIFICATION", medicalCertification);
                startActivity(intent);
                finish();
            }
        });
        dealingBtn.setText("応急手当開始");
        /*
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dealingBtn.setLayoutParams(buttonLayoutParams);
        */
        //linearLayout.addView(dealingBtn);
    }

    private void addDescription(Care c){
        int xmlID = Utils.getXMLID(c.xml);
        if (xmlID >= 0) {
            XmlResourceParser xpp = this.getResources().getXml(xmlID);


            try {
                int eventType = xpp.getEventType();
                while (eventType != XmlResourceParser.END_DOCUMENT) {
                    final String name = xpp.getName();
                    //Log.d("tag loop", " " + name);
                    if (name == null) {
                        Log.d("xpp", "name is null");
                        eventType = xpp.next();
                        continue;
                    }
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            Log.d("tag", "Start Document");
                            break;

                        case XmlPullParser.START_TAG:
                            if ("notice".equals(name)){

                            } else if ("notice_description".equals(name)){
                                c.setDescription(xpp.nextText());
                                Log.d("notice_description", c.description);
                            }
                        case XmlPullParser.END_TAG:
                            if ("notice".equals(name)){
                                //
                            }
                            break;
                        default:
                            break;
                    }

                    eventType = xpp.next();
                }
                Log.d("tag end document", Boolean.toString(eventType == XmlResourceParser.END_DOCUMENT));

            } catch (Exception e) {
                Log.e("Emergency", e.toString());
            }
        }



    }

    public String getCareString(boolean[] care_boolean){
        assetManager = this.getResources().getAssets();

        cares = new ArrayList<>();
        String s = "";
        for (int i = 0; i < care_boolean.length; i++){
            if (care_boolean[i]){
                s += "Y";
                Care c = CareList.getCare(i);

                cares.add(c);
            } else {
                s += "N";
            }
        }

        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        careList = new CareList(this);
        careList.showCareList();

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
        } catch (Exception e){
            medicalCertification = new MedicalCertification();
        }
        urgency = getIntent().getIntExtra("URGENCY", 1);
        Log.d("URGENCY", Integer.toString(urgency));
        boolean[] _cares = getIntent().getBooleanArrayExtra("CARES");
        Log.d("CARES", getCareString(_cares));
        for (Care c : cares){
            Log.d("care required" , c.name);
        }

        medicalCertification.showRecords("ResultActivity");


        setScrollView();
        setLinearLayout();
        setTextView();
        //setInflate();
        showCareList();
        setDealingBtn();

        setDrawerLayout();

    }

    void setDrawerLayout(){

        mTitle = getTitle();
        mDrawerTitle = mTitle;

        // ドロワーメニューのリストの値を初期化
        menuActivities = new String[]{"QRコード", "診断書", "応急手当"};

        // ドロワーレイアウト、リストビューのidを取得
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_result);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_result);

        // リストビューとデータを関連付け
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuActivities));

        // 選択時のイベントを登録
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );

        // アクションバーにhomeボタンを追加
        // ホームボタンクリック時の動作
        // 押下することで開閉を切り替える
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* メインアクティビティ */
                mDrawerLayout,         /* ドロワーメニュー */
                new Toolbar(this),  /* ドロワーアイコン */
                R.string.open,  /* 開く */
                R.string.close  /* 閉じる */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // onPrepareOptionsMenuを呼びます
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // onPrepareOptionsMenuを呼びます
            }
        };
        // リスナーに登録
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }



    @SuppressLint("ValidFragment")
    public class PlanetFragment extends Fragment {

        public static final String ARG_TEXT = "menu_choice";


        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_result, container, false);

            String str = getArguments().getString(ARG_TEXT);
            Log.d("ResultAct side menu", str + "is selected");

            if (str.equals("QRコード")){
                Intent intent = new Intent(ResultActivity.this, Display_qr.class);
                intent.putExtra("RESULT", medicalCertification.toString());
                startActivity(intent);
            } else if (str.equals("診断書")){
                Intent intent = new Intent(ResultActivity.this, CertificationActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent);
            } else if (str.equals("応急手当")){
                Intent intent = new Intent(ResultActivity.this, CareChooseActivity.class);
                startActivity(intent);
            }
            //getActivity().setTitle(str);
            return rootView;
        }
    }

    private void selectItem(int position) {
        // フラグメントを生成して値を引き渡す
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putString(PlanetFragment.ARG_TEXT, menuActivities[position]);
        fragment.setArguments(args);

        // 画面切り替え
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.layout_content_result, fragment).commit();

        // 選択状態
        mDrawerList.setItemChecked(position, true);

        // ドロワーメニューを閉じる
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
