package uk.ac.rgu.rectv;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowNameRecyclerViewAdapter extends RecyclerView.Adapter<ShowNameRecyclerViewAdapter.ShowNameViewHolder> {

    private List<ShowName> shownames;
    private LayoutInflater inflater;

    public ShowNameRecyclerViewAdapter(Context context, List<ShowName> shownames) {
        super();
        this.shownames = shownames;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShowNameRecyclerViewAdapter.ShowNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View shownameView = this.inflater.inflate(R.layout.poster_row_item, parent, false);
        ShowNameViewHolder viewHolder = new ShowNameViewHolder(this, shownameView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowNameRecyclerViewAdapter.ShowNameViewHolder holder, int position) {
        ShowName shownameToBeDisplayed = this.shownames.get(position);

        TextView tvShowName = holder.itemView.findViewById(R.id.tvShowName);
        tvShowName.setText(shownameToBeDisplayed.getEpisodeName());
    }

    @Override
    public int getItemCount() {
        return this.shownames.size();
    }

    class ShowNameViewHolder extends RecyclerView.ViewHolder{

        private ShowNameRecyclerViewAdapter adapter;
        private View itemView;

        public ShowNameViewHolder(ShowNameRecyclerViewAdapter adapter, View itemView) {
            super(itemView);
            this.adapter = adapter;
            this.itemView = itemView;
        }
    }
}
