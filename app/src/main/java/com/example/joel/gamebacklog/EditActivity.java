package com.example.joel.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    private EditText titleText;
    private EditText deviceText;
    private EditText noteText;
    private EditText statusText;
    private boolean mIsUpdate = false;
    private GameObj gameObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addgame_activity);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        titleText = findViewById(R.id.editTextTitle);
        deviceText = findViewById(R.id.editTextDevice);
        noteText = findViewById(R.id.editTextNote);
        statusText = findViewById(R.id.editTextStatus);


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString(MainActivity.EXTRA_MODE) != null) {
            if (extras.getString(MainActivity.EXTRA_MODE).equals(MainActivity.EXTRA_UPDATE)){
                mIsUpdate = true;
                gameObj = (GameObj) extras.getSerializable(MainActivity.EXTRA_GAME);
                statusText.setText(Objects.requireNonNull(gameObj).getStatus());
                titleText.setText(gameObj.getTitle());
                deviceText.setText(gameObj.getDevice());
                noteText.setText(gameObj.getNotes());

            }
        }

        FloatingActionButton fab = findViewById(R.id.saveButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mTitleText = titleText.getText().toString();
                String mDeviceText = deviceText.getText().toString();
                String mNotesText = noteText.getText().toString();
                String mStatusText = statusText.getText().toString();
                Intent intent = new Intent();

                if (
                        mTitleText.isEmpty()
                                || mDeviceText.isEmpty()
                                || mNotesText.isEmpty()
                                || mStatusText.isEmpty()
                        ) {
                    Toast.makeText(
                            EditActivity.this,
                            "fill all",
                            Toast.LENGTH_LONG
                    ).show();
                } else if (mIsUpdate) {
                    gameObj.setTitle(mTitleText);
                    gameObj.setDevice(mDeviceText);
                    gameObj.setNotes(mNotesText);
                    gameObj.setStatus(mStatusText);
                    intent.putExtra(MainActivity.EXTRA_MODE, MainActivity.EXTRA_UPDATE);
                    intent.putExtra(MainActivity.EXTRA_GAME, gameObj);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    gameObj = new GameObj(mTitleText,mDeviceText,mNotesText, mStatusText);
                    intent.putExtra(MainActivity.EXTRA_MODE, MainActivity.EXTRA_CREATE);
                    intent.putExtra(MainActivity.EXTRA_GAME, gameObj);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

