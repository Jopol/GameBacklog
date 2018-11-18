package com.example.joel.gamebacklog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameObjAdapter extends RecyclerView.Adapter<GameObjAdapter.ViewHolder> {

    private List<GameObj> GamesList;
    final private GameClickListener gameClickListener;

    public GameObjAdapter(List<GameObj> gamesList, GameClickListener gameClickListener) {
        this.GamesList = gamesList;
        this.gameClickListener = gameClickListener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.gamecard,
                viewGroup,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final GameObj gameobj = GamesList.get(i);
            viewHolder.textViewTitle.setText(gameobj.getTitle());
            viewHolder.textViewDevice.setText(gameobj.getDevice());
            viewHolder.textViewNotes.setText(gameobj.getNotes());
            viewHolder.textViewStatus.setText(gameobj.getStatus());
    }

    @Override
    public int getItemCount() {
        return GamesList.size();
    }

    public void removeItem(int position) {
        GamesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, GamesList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewTitle, textViewDevice, textViewNotes, textViewStatus;
        public View view;


        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle =  itemView.findViewById(R.id.textViewTitle);
            this.textViewDevice =  itemView.findViewById(R.id.textViewDevice);
            this.textViewNotes =  itemView.findViewById(R.id.textViewNotes);
            this.textViewStatus =  itemView.findViewById(R.id.textViewStatus);
            this.view = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            gameClickListener.gameOnClick(clickedPosition);
        }

    }

    public void swapList (List<GameObj> newList) {
        GamesList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}
