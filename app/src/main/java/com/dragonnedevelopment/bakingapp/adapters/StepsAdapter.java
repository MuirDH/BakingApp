package com.dragonnedevelopment.bakingapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.models.Step;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BakingApp Created by Muir on 30/04/2018.
 * <p>
 * Creates a list of recipe steps to a {@link android.support.v7.widget.RecyclerView}
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private StepsOnClickHandler onClickHandler;
    private Context context;
    private List<Step> steps;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Interface to receive onClick messages
     */
    public interface StepsOnClickHandler {
        void onClick(Step step);
    }

    /*
     * OnClick handler for the adapter which handles the situation when a single item is clicked
     */
    public StepsAdapter(Context context, StepsOnClickHandler onClickHandler) {
        this.context = context;
        this.onClickHandler = onClickHandler;
        sharedPreferences =
                context.getSharedPreferences(Config.PREFERENCE_KEY_RECIPE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ButterKnife bindings
        @BindView(R.id.rlayout_step)
        RelativeLayout rlayoutStep;

        @BindView(R.id.textview_step)
        TextView textViewStep;

        @BindColor(R.color.colorLightGrey)
        int colourDefaultBackground;

        @BindColor(R.color.colorListBackground)
        int colourSelectedBackground;


        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        // called when the child view is clicked
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = steps.get(adapterPosition);
            onClickHandler.onClick(step);
        }
    }

    /**
     * Called when a new ViewHolder gets created in the event of Recyclerview being laid out.
     * This create enough ViewHolders to fill up the screen and allow scrolling
     *
     * @return a new ViewHolder which holds the View for each list item
     */
    @NonNull
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int listItemLayoutId = R.layout.list_item_step;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(listItemLayoutId, parent, false);
        return new StepViewHolder(view);
    }

    /*
     * Used by the RecyclerView to list the steps of the recipe
     */
    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepViewHolder holder, int position) {
        if (position < getItemCount()) {
            Step step = steps.get(position);
            String stepShortDescription = String.format(context.getString(R.string.display_step_id),
                    step.getStepId(), step.getStepShortDescription());

            if (!Utils.isEmptyString(stepShortDescription)) {
                holder.textViewStep.setText(stepShortDescription);
            }

            if (steps.get(position).getIsSelected()) {
                holder.rlayoutStep.setBackgroundColor(holder.colourSelectedBackground);
            } else {
                holder.rlayoutStep.setBackgroundColor(holder.colourDefaultBackground);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.size();
    }

    /*
     * Refreshes the list once the adapter is already created to avoid creating a new one
     */
    public void setStepsData(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setSelected(int pos) {
        int oldPos = sharedPreferences.getInt(Config.PREFERENCE_KEY_STEP_SELECTED, -1);
        if ((oldPos > -1) && (oldPos < getItemCount())) {
            steps.get(oldPos).setSelected(false);
        }

        editor.putInt(Config.PREFERENCE_KEY_STEP_SELECTED, pos);
        editor.commit();
        steps.get(pos).setSelected(true);
        notifyDataSetChanged();
    }


}
