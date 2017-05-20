package com.example.vimadhavan.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vimadhavan.todo.adapter.TaskAdapter;
import com.example.vimadhavan.todo.database.DBhandler;
import com.example.vimadhavan.todo.model.Task;
import com.example.vimadhavan.todo.utils.Util;

import java.util.ArrayList;

public class CompletedTask extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemLongClickListener{

    private Toast exitToast;
    private ListView allTaskList;
    private TaskAdapter allTaskAdapter;
    private ArrayList<Task> allTasks;
    private DBhandler handleDB;
    private TextView defaultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.completed));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(this);

        allTaskList = (ListView) findViewById(R.id.allTaskList);
        defaultText=(TextView) findViewById(R.id.defaultTxt);
        handleDB= DBhandler.getInstance(this);
        allTaskList.setOnItemLongClickListener(this);
        init();

    }

    private void init(){
        //get all the completed  task from db and if there is no task completed then show that messages
        allTasks=handleDB.getAllCompletedTasks();

        if(allTasks.isEmpty()){
            defaultText.setText(getString(R.string.noCompletedTasks));

            defaultText.setVisibility(View.VISIBLE);
            allTaskList.setVisibility(View.GONE);
        }else{
            defaultText.setVisibility(View.GONE);
            allTaskList.setVisibility(View.VISIBLE);


        }

        allTaskAdapter=new TaskAdapter(this,allTasks);
        allTaskList.setAdapter(allTaskAdapter);
        allTaskAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        //back to main activity
        Intent backIntent=new Intent(this,MainActivity.class);

        startActivity(backIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if(exitToast!=null){
            //on double back exit from the app
            handleDB.close();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            //on first time back button pressed display message
            exitToast=Toast.makeText(this, getString(R.string.exitMsg), Toast.LENGTH_LONG);
            exitToast.show();
            Util.delay(Toast.LENGTH_LONG, new Util.DelayCallback() {
                @Override
                public void afterDelay() {
                    exitToast.cancel();
                    exitToast=null;
                }
            });
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //on LongPress delete that task from db
        try{
            handleDB.deleteTask(allTasks.get(position));
            allTasks.remove(position);
        }catch (Exception ex){
            Log.d("Debug:Exception",ex.getMessage());
        }catch (Error er){
            Log.d("Debug:Error",er.getMessage());
        }

        if(allTasks.isEmpty()){
            defaultText.setText(getString(R.string.noCompletedTasks));

            defaultText.setVisibility(View.VISIBLE);
            allTaskList.setVisibility(View.GONE);
        }else{
            defaultText.setVisibility(View.GONE);
            allTaskList.setVisibility(View.VISIBLE);


        }
        allTaskAdapter.notifyDataSetChanged();
        allTaskList.invalidateViews();
        return true;
    }
}
