package com.benweinshel.calculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void calculateResult(View view) {
        // do something
        EditText inputEditText = (EditText) findViewById(R.id.editText);
        String input = inputEditText.getText().toString();

        // Do the calculation
        String result = doAddition(input);

        TextView resultText = (TextView) findViewById(R.id.tvResult);
        resultText.setText(result);
        resultText.setVisibility(View.VISIBLE);
    }

    private String doAddition(String input) {
        String[] separated = input.split("\\+");
        Log.d("MainActivity", separated[0]);
        int adder1 = Integer.parseInt(separated[0]);
        int adder2 = Integer.parseInt(separated[1]);
        int result = adder1 + adder2;
        String resultString = Integer.toString(result);
        return resultString;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
