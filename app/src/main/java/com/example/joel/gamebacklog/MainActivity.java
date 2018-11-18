package com.example.joel.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GameClickListener {

    //Local variables

    private List<GameObj> games;
    private GameObjAdapter gameObjAdapter;
    private RecyclerView recyclerView;
    private static AppDataBase gamedb;

    //Constants used when calling the update activity
    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;
    public final static String EXTRA_CREATE = "Create";
    public final static String EXTRA_UPDATE = "Update";
    public final static String EXTRA_MODE = "Mode";
    public final static String EXTRA_GAME = "Game";
    public final static int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gamedb = AppDataBase.getsInstance(this);

        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                1,
                LinearLayoutManager.VERTICAL
        );
        recyclerView.setLayoutManager(mLayoutManager);
        updateUI();

        handleSwipe(gameObjAdapter, recyclerView);

        FloatingActionButton fab = findViewById(R.id.toAddButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null && extras.getString(EXTRA_MODE) != null) {
                    if (extras.getString(EXTRA_MODE).equals(EXTRA_CREATE)) {
                        new GameAsyncTask(TASK_INSERT_GAME).execute(
                                (GameObj) extras.getSerializable(EXTRA_GAME)
                        );
                    } else if (extras.getString(EXTRA_MODE).equals(EXTRA_UPDATE)) {
                        new GameAsyncTask(TASK_UPDATE_GAME).execute(
                                (GameObj) extras.getSerializable(EXTRA_GAME)
                        );
                    }
                }
            }
        }
    }

    @Override
    public void gameOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(EXTRA_MODE, EXTRA_UPDATE);
        intent.putExtra(EXTRA_GAME, games.get(i));
        startActivityForResult(intent, REQUEST_CODE);
    }


    public void onGameDbUpdated(List list) {
        games = list;
        updateUI();
    }

    private void updateUI() {
        if (gameObjAdapter == null) {
            gameObjAdapter = new GameObjAdapter(games, this);
            recyclerView.setAdapter(gameObjAdapter);
        } else {
            gameObjAdapter.swapList(games);
        }
    }

    private void handleSwipe(final GameObjAdapter gameObjAdapter, RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                new GameAsyncTask(TASK_DELETE_GAME).execute(games.get(position));
                gameObjAdapter.removeItem(position);
                Toast.makeText(
                        MainActivity.this,
                        "Game deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class GameAsyncTask extends AsyncTask<GameObj, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(GameObj... games) {
            switch (taskCode) {
                case MainActivity.TASK_DELETE_GAME:
                    gamedb.gameDao().deleteGames(games[0]);
                    break;

                case MainActivity.TASK_UPDATE_GAME:
                    gamedb.gameDao().updateGames(games[0]);
                    break;

                case MainActivity.TASK_INSERT_GAME:
                    gamedb.gameDao().insertGames(games[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return gamedb.gameDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }
    }
}
