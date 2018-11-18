package com.example.joel.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
            if (Objects.equals(extras.getString(MainActivity.EXTRA_MODE), MainActivity.EXTRA_UPDATE)) {
                mIsUpdate = true;
                gameObj = (GameObj) extras.getSerializable(MainActivity.EXTRA_GAME);
                statusText.setText(gameObj.getStatus());
                titleText.setText(gameObj.getTitle());
                deviceText.setText(gameObj.getDevice());
                noteText.setText(gameObj.getNotes());

            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGame(titleText.getText().toString(), deviceText.getText().toString(), noteText.getText().toString(), statusText.getText().toString());
            }
        });
    }
        private void addGame(String title, String device, String notes, String status){

            Intent intent = new Intent();

            // None should be empty
            if(title.isEmpty() || device.isEmpty() || notes.isEmpty() || status.isEmpty()){
                Toast.makeText(getBaseContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            } else if(mIsUpdate){ // Check whether this is an update or an add
                // Set all of the info
                gameObj.setTitle(title);
                gameObj.setDevice(device);
                gameObj.setNotes(notes);
                gameObj.setStatus(status);

                // Prepare the intent with data
                intent.putExtra(getString(R.string.d_mode), getString(R.string.d_update));
                intent.putExtra(getString(R.string.d_game), gameObj);

                Toast.makeText(getBaseContext(), "Updated " + title, Toast.LENGTH_SHORT).show();

                // Mark the result as OK and pass the intent as data
                setResult(RESULT_OK, intent);
                // Return to the main Activity
                finish();
            } else {
                gameObj = new GameObj(title, device, notes, status);
                intent.putExtra(getString(R.string.d_mode), getString(R.string.d_create));
                intent.putExtra(getString(R.string.d_game), gameObj);

                Toast.makeText(getBaseContext(), "Added " + title, Toast.LENGTH_SHORT).show();

                // Mark the result as OK and pass the intent as data
                setResult(RESULT_OK, intent);
                // Return to the main Activity
                finish();
            }
        }

}
