package com.example.xoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GameSummaryAdapter extends RecyclerView.Adapter<GameSummaryAdapter.ViewHolder> {

    private List<GameSummary> gameSummaryList;

    public GameSummaryAdapter(List<GameSummary> gameSummaryList) {
        this.gameSummaryList = gameSummaryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameSummary gameSummary = gameSummaryList.get(position);
        holder.tvPlayers.setText(gameSummary.getPlayer1() + " vs " + gameSummary.getPlayer2());
        holder.tvWinner.setText("Winner: " + gameSummary.getWinner());
        holder.tvTime.setText("Time: " + gameSummary.getTime());
    }

    @Override
    public int getItemCount() {
        return gameSummaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayers, tvWinner, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayers = itemView.findViewById(R.id.tvPlayers);
            tvWinner = itemView.findViewById(R.id.tvWinner);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
