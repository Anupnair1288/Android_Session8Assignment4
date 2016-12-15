package com.android.assignment.acadgild.writefiletostorage;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText et_inputText;
    Button button_save;
    Button button_delete;
    TextView tv_fileContent;
    static String fileName = "test.txt";
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIds();
        Log.e("ids", "set");
        String Status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(Status)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath(), File.separator);
            if (dir.exists()) {
                file = new File(dir, fileName);
                try {
                    if (file.createNewFile()) {
                        Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "File exists", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Directory doesn't exist", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "SD card not mounted", Toast.LENGTH_LONG).show();
        }
        button_save.setOnClickListener(this);
        button_delete.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save_data:
                String value = et_inputText.getText().toString();
                et_inputText.setText("");
                ReadFromFile readFile = new ReadFromFile(file);
                readFile.execute(value);
                break;
            case R.id.button_delete_file:
                deleteFile();
                break;
        }
    }
    public void setIds() {
        et_inputText = (EditText) findViewById(R.id.edit_file_input);
        button_save = (Button) findViewById(R.id.button_save_data);
        button_delete = (Button)findViewById(R.id.button_delete_file);
        tv_fileContent = (TextView) findViewById(R.id.textview_file_content);
    }

    private void deleteFile() {
        boolean delete;
        if(file.exists()){
            delete = file.delete();
            if(delete){
                tv_fileContent.setText("");
                Toast.makeText(getApplicationContext(),"File Deleted.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"The file does not exist.", Toast.LENGTH_LONG).show();
        }
    }
    private class ReadFromFile extends AsyncTask<String, Integer, String> {

        String enter = "\n";
        File f;
        String content = et_inputText.getText().toString();
        ReadFromFile(File f) {
            super();
            this.f = f;
        }


        @Override
        protected String doInBackground(String... strings) {

            FileWriter fWriter = null;
            try {
                fWriter = new FileWriter(f, true);
                fWriter.append(strings[0]);
                fWriter.append(enter);
                fWriter.flush();
                Log.e("File Saved :", content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(fWriter != null){
                        fWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String name;
            StringBuilder sb = new StringBuilder();
            FileReader fReader;
            try {
                fReader= new FileReader(f);
                BufferedReader bfReader = new BufferedReader(fReader);
                while ((name = bfReader.readLine()) != null) {
                    sb.append(name);
                    sb.append(enter);
                }
                bfReader.close();
                fReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tv_fileContent.setMovementMethod(new ScrollingMovementMethod());
            tv_fileContent.setText(sb.toString());
        }
    }
}