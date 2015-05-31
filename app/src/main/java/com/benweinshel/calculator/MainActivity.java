package com.benweinshel.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.editText)
    EditText inputEditText;
    @InjectView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<CalculationLog> calculations = new ArrayList<CalculationLog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //   inputEditText.setErrorEnabled(true);
        inputEditText.requestFocus();
        //    inputEditText.getEditText().setOnTouchListener(otl);
        inputEditText.setOnTouchListener(otl);

//        IBinder wt = inputEditText.getWindowToken();
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(wt, 0);
//        inputEditText.getEditText().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int inType = inputEditText.getEditText().getInputType(); // backup the input type
//                inputEditText.getEditText().setInputType(InputType.TYPE_NULL); // disable soft input
//                inputEditText.getEditText().onTouchEvent(event); // call native handler
//                inputEditText.getEditText().setInputType(inType); // restore input type
//                return true; // consume touch even
//            }
//        });


        //inputEditText.getEditText().setInputType(null);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(calculations);
        mRecyclerView.setAdapter(mAdapter);

    }


    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void calculateResult(View view) {

        //String input = inputEditText.getEditText().getText().toString();
        String input = inputEditText.getText().toString();
        if (input.isEmpty()) {
            return;
        }

        // Do the calculation
        try {
            doResult(input);
        } catch (Exception e) {
            if (!e.getMessage().isEmpty()) {
                CharSequence message = (CharSequence) e.getMessage();
                //        inputEditText.setError(message);
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                CharSequence error = (CharSequence) e;
                //        inputEditText.setError(error);
                Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }

    private void doResult(String input) throws Exception {

        String result = Maths.doMath(input);
        CalculationLog c = new CalculationLog(input, result);
        int calcSize = calculations.size();
        calculations.add(c);
        mAdapter.notifyItemInserted(calcSize);
        mRecyclerView.scrollToPosition(calcSize);
    }

    public void buttonPressed(View view) {
        Button b = (Button) view;
        CharSequence buttonText = b.getText();
        // inputEditText.getEditText().append(buttonText);
        inputEditText.append(buttonText);
        //inputEditText.setError(null);
    }

    public void delPressed(View view) {
        //  inputEditText.getEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        //   inputEditText.setError(null);
    }

    public void rightPressed(View view) {
        // inputEditText.getEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
        //   inputEditText.setError(null);
    }

    public void leftPressed(View view) {
        //   inputEditText.getEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
        //   inputEditText.setError(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_about:
                return true;
            case R.id.action_clear_hist:
                calculations.clear();
                mAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_about).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            }
        });
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}