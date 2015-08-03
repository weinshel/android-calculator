package com.benweinshel.calculator;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.PopupMenu;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.editText) EditText inputEditText;
    @InjectView(R.id.my_recycler_view) RecyclerView mRecyclerView;
    @InjectView(R.id.fab) FloatingActionButton floatingActionButton;
    @InjectView(R.id.button_del) ImageButton delButton;
    @InjectView(R.id.button_left) ImageButton leftButton;
    @InjectView(R.id.button_right) ImageButton rightButton;


    private RecyclerView.Adapter mAdapter;

    private final List<CalculationLog> calculations = new ArrayList<CalculationLog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        inputEditText.requestFocus();
        inputEditText.setOnTouchListener(otl);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(calculations);
        mRecyclerView.setAdapter(mAdapter);

        delButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });

        // Delete button long press
        delButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputEditText.setText(null);
                return true;
            }
        });

        // Left button long press
        leftButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputEditText.setSelection(0, 0);
                return true;
            }
        });

        // Right button long press
        rightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int textLength = inputEditText.getText().length();
                inputEditText.setSelection(textLength, textLength);
                return true;
            }
        });

    }



    // an OnTouchListener for the edit text that does absolutely nothing (absorbs the touch)
    private final View.OnTouchListener otl = new View.OnTouchListener() {
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
            inputEditText.setText(null);
        } catch (Exception e) {
            if (!e.getMessage().isEmpty()) {
                CharSequence message = e.getMessage();
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

        String result = Maths.doMath(input, this);
        CalculationLog c = new CalculationLog(input, result);
        int calcSize = calculations.size();
        calculations.add(c);
        mAdapter.notifyItemInserted(calcSize);
        mRecyclerView.scrollToPosition(calcSize);
    }

    public void buttonPressed(View view) {
        Button b = (Button) view;
        CharSequence buttonText = b.getText();
        inputEditText.getText().insert(inputEditText.getSelectionStart(), buttonText);
    }

//    public void delPressed(@SuppressWarnings("UnusedParameters") View view) {
//        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//    }
//
//    public void delClear(View view) {
//        inputEditText.setText(null);
//    }

    public void rightPressed(@SuppressWarnings("UnusedParameters") View view) {
        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
    }

    public void leftPressed(@SuppressWarnings("UnusedParameters") View view) {
        inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
    }

    public void trigButtonPressed(View view) {
        PopupMenu myMenu = new PopupMenu(MainActivity.this, view);
        myMenu.inflate(R.menu.menu_trig);

        view.setOnTouchListener(myMenu.getDragToOpenListener());
        // Define a click listener
        myMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                inputEditText.getText().insert(inputEditText.getSelectionStart(), item.getTitle());
                return true;
            }
        });

        myMenu.show();
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}