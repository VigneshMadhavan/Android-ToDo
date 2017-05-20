package com.example.vimadhavan.todo;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vimadhavan.todo.adapter.TaskAdapter;
import com.example.vimadhavan.todo.database.DBhandler;
import com.example.vimadhavan.todo.model.Task;
import com.example.vimadhavan.todo.utils.Util;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private Dialog taskDialog;
    private Button saveBtn,cancelBtn;
    private DatePicker datePicker;
    private EditText taskTitle,taskDes;
    private Toast exitToast;
    private ListView allTaskList;
    private TaskAdapter allTaskAdapter;
    private ArrayList<Task> allTasks;
    private DBhandler handleDB;
    private TextView defaultText;
    private boolean inEdit=false;
    private Task editTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        allTaskList = (ListView) findViewById(R.id.allTaskList);
        defaultText=(TextView) findViewById(R.id.defaultTxt);
        handleDB= DBhandler.getInstance(this);

        allTaskList.setOnItemClickListener(this);
        allTaskList.setOnItemLongClickListener(this);
        inEdit=false;
        init();
    }

    /*
    Function init to read data from Sqlite and if there is no data it need to display as message

     */
    private void init(){
        allTasks=handleDB.getAllTasks();

        if(allTasks.isEmpty()){
            defaultText.setText(getString(R.string.noData));

        }else{
            defaultText.setVisibility(View.GONE);
            allTaskList.setVisibility(View.VISIBLE);


        }

        allTaskAdapter=new TaskAdapter(this,allTasks);
        allTaskList.setAdapter(allTaskAdapter);
        allTaskAdapter.notifyDataSetChanged();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create custom menu for  adding new Task and listing completed task
        MenuInflater M = getMenuInflater();
        M.inflate(R.menu.custom_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
             case R.id.done:
                 jumpToCompletedTaskActivity();
                break;
            case R.id.newTask:
                addNewItemDialog();
                break;

        }




        return super.onOptionsItemSelected(item);
    }

    private void jumpToCompletedTaskActivity(){
        //Start the Activity by using intent to show the completed ativity
        Intent completedTaskIntent =new Intent(MainActivity.this,CompletedTask.class);
        completedTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(completedTaskIntent);

    }

    private void addNewItemDialog(){
        //show dialog to get the parameter for the new task

        taskDialog =new Dialog(MainActivity.this);
        taskDialog.setCancelable(false);
        taskDialog.setContentView(R.layout.new_task);

        cancelBtn = (Button) taskDialog.findViewById(R.id.cancelBtn);
        saveBtn = (Button) taskDialog.findViewById(R.id.saveBtn);
        datePicker =(DatePicker) taskDialog.findViewById(R.id.datePicker);
        taskTitle=(EditText) taskDialog.findViewById(R.id.titleTxt);
        taskDes=(EditText) taskDialog.findViewById(R.id.descTxt);

        datePicker.setMinDate(System.currentTimeMillis()-1000);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);



        taskDialog.show();
    }

    private void editItemDialog(Task task){
        //show dialog to edit the parameter for the new task
        editTask=task;
        taskDialog =new Dialog(MainActivity.this);
        taskDialog.setCancelable(false);
        taskDialog.setContentView(R.layout.new_task);

        cancelBtn = (Button) taskDialog.findViewById(R.id.cancelBtn);
        saveBtn = (Button) taskDialog.findViewById(R.id.saveBtn);
        datePicker =(DatePicker) taskDialog.findViewById(R.id.datePicker);
        taskTitle=(EditText) taskDialog.findViewById(R.id.titleTxt);
        taskDes=(EditText) taskDialog.findViewById(R.id.descTxt);

        taskTitle.setText(task.getTitle());
        taskDes.setText(task.getDescription());

        datePicker.updateDate(task.getYear(),task.getMonth()-1,task.getDate());

        datePicker.setMinDate(System.currentTimeMillis()-1000);
        inEdit=true;
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        taskDialog.show();
    }

    private void saveAction(){
        //save the edited/created task
        //Toast.makeText(this, "Save: "+datePicker.getDayOfMonth()+":"+datePicker.getMonth()+":"+datePicker.getYear(), Toast.LENGTH_SHORT).show();

        if(taskTitle.getText().toString().isEmpty() || taskDes.getText().toString().isEmpty()){
            //if blank show message
            Toast.makeText(this, getString(R.string.fillAllDetails), Toast.LENGTH_LONG).show();
        }else {
            if(!inEdit) {
                //if it new then add as new
                Task newTask = new Task(taskTitle.getText().toString(), taskDes.getText().toString(), datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear());


                long id = handleDB.addTask(newTask);

                newTask.setId((int) id);

                //Toast.makeText(this, "Added: "+id, Toast.LENGTH_LONG).show();
                defaultText.setVisibility(View.GONE);
                allTaskList.setVisibility(View.VISIBLE);
                allTasks.add(newTask);
                Collections.sort(allTasks);
                allTaskAdapter.notifyDataSetChanged();
                allTaskList.invalidateViews();
                taskDialog.dismiss();
            }else{
                //if its edit then update the ole one with new data
                editTask.setTitle(taskTitle.getText().toString());
                editTask.setDescription(taskDes.getText().toString());
                editTask.setYear(datePicker.getYear());
                editTask.setMonth(datePicker.getMonth()+1);
                editTask.setDate(datePicker.getDayOfMonth());
                handleDB.updateTask(editTask);
                inEdit=false;
                editTask.updateDateTime();
                Collections.sort(allTasks);
                allTaskAdapter.notifyDataSetChanged();
                allTaskList.invalidateViews();
                taskDialog.dismiss();
            }
        }

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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cancelBtn:
                inEdit=false;
                taskDialog.dismiss();
                break;
            case R.id.saveBtn:
                saveAction();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editItemDialog(allTasks.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //Ttoogle the task status, like from complete to incomplete or incomplete to complete
        if(allTasks.get(position).getStatus()==0){
            allTasks.get(position).setStatus(1);
        }else{
            allTasks.get(position).setStatus(0);
        }

        handleDB.updateTask(allTasks.get(position));
        allTaskAdapter.notifyDataSetChanged();
        allTaskList.invalidateViews();
        return true;
    }
}
