package com.example.android.bakingrecipe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewAdapter>{

    private ArrayList<Step> steps;
    private Context context;
    private final StepAdapter.StepAdapterOnClickListener mClickHandler;

    public StepAdapter(StepAdapter.StepAdapterOnClickListener clickListener){
        mClickHandler = clickListener;
    }

    @Override
    public StepAdapterViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_details_step_content;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new StepAdapterViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(StepAdapterViewAdapter holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (this.steps == null ) return 0;
        return this.steps.size();
    }

    private Step getStepByPosition(int position){
        return steps.get(position);
    }

    public interface StepAdapterOnClickListener{
        void stepOnClick(String videoURL, String description);
    }

    public void setStep(ArrayList<Step> steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    class StepAdapterViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView mThumbnail;
        private final TextView mShortDescription;

        StepAdapterViewAdapter(View itemView) {
            super(itemView);
            mThumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
            mShortDescription = (TextView)itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            Step step = steps.get(position);
            String shortDescription = step.getShortDescription();
            String videoURL = step.getVideoURL();
            String thumbnail = step.getThumbnailURL();

            mShortDescription.setText(shortDescription);

            if(thumbnail.length() == 0) {
                if (videoURL.length() == 0) {
                    Picasso.with(context)
                            .load(R.drawable.play_button)
                            .into(mThumbnail);
                } else {
                    Picasso.with(context)
                            .load(R.drawable.play_button_active)
                            .into(mThumbnail);
                }
            }else{
                Picasso.with(context)
                        .load(thumbnail)
                        .placeholder(R.drawable.play_button)
                        .error(R.drawable.play_button)
                        .into(mThumbnail);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.stepOnClick(getStepByPosition(adapterPosition).getVideoURL(),
                    getStepByPosition(adapterPosition).getDescription());
        }
    }
}
