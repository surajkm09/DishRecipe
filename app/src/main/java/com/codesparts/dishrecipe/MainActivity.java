package com.codesparts.dishrecipe;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public  class  DownloadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected  String doInBackground(String... params )
        {
            String result = "";
            URL url  ;
            HttpURLConnection urlConnection = null ;
            try {

                url = new URL(params[0]) ;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data!=-1)
                {
                    char current  = (char )data ;
                    result += current ;
                    data=reader.read();

                }


            }
            catch (Exception e )
            {
                Log.i("exception","caught");
                e.printStackTrace();
            }
            return  result ;

        }
    }
    public void hide()
    {
        TextView dish = (TextView) findViewById(R.id.DishName);
        dish.setVisibility(View.INVISIBLE);
        TextView  title = (TextView) findViewById(R.id.Title);
        title.setVisibility(View.INVISIBLE);
        TextView button = (TextView) findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);
        TextView header = ( TextView) findViewById(R.id.Header);
        header.setVisibility(View.VISIBLE);
    }
    public  void GetIngredients(View view)
    {
        EditText DishName = (EditText) findViewById(R.id.DishName);
        String Dish = DishName.getText().toString();
        String url =  "http://www.aryansuraj.xyz/recipe/select.php?nam="+Dish.toUpperCase() ;
        DownloadTask task =  new DownloadTask() ;
        String result  =  null ;
        try {
            result = task.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

         result =result.substring(0,result.length()-2);
        Log.i("result",result);
        if (result.equals("error") )
        {

            Toast.makeText(getApplicationContext(),"Sorry,we could not find ingredients for that dish",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Try another dish",Toast.LENGTH_LONG).show();



            return;
        }
        else
        {
            hide();
            Log.i("data1",result);
            result=result.trim();
            String [] tokens =result.split(" ");
            Log.i("data",tokens[1]);

            ArrayList<String > myDat = new ArrayList<String>();
            ListView myView = (ListView ) findViewById(R.id.mylist);

            for(int i=0 ;i<tokens.length-1;i+=2)
            {
                myDat.add(tokens[i]+" "+tokens[i+1]);
            }

            ArrayAdapter<String> arrayAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myDat);
            myView.setAdapter(arrayAdapter);

            myView.setVisibility(View.VISIBLE);
        }


        return;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
