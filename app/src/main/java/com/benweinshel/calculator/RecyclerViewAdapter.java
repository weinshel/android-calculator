package com.benweinshel.calculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by bmweinshel15 on 5/26/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<CalculationLog> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView input;
        TextView result;

        public ViewHolder(View v) {
            super(v);
            input = (TextView) itemView.findViewById(R.id.text_input);
            result = (TextView) itemView.findViewById(R.id.text_result);
        }
    }

    // class constructor
    public RecyclerViewAdapter(List<CalculationLog> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the laout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view, parent, false);
        // TODO: set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        CalculationLog model = mDataset.get(position);
        holder.input.setText(model.input);
        holder.result.setText(model.result);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount(){
        return mDataset.size();
    }
}
