package ospace.com.swipedeletelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MainActivity extends AppCompatActivity {
@Bind(R.id.my_recycler_view)
RecyclerView my_recycler_view;
    private ArrayList<String >mdatas=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        my_recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        my_recycler_view.setItemAnimator(new SlideInLeftAnimator());
        my_recycler_view.setAdapter(new MyAdapter(mdatas,this));


    }
    public void initData(){
        for(int i =0;i<=100;i++){
            mdatas.add("第"+i+"个条目");
        }
    }
}
