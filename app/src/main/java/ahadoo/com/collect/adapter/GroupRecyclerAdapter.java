package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyGroup;

class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.CustomViewHolder> {

    private final Context context;
    private final List<SurveyGroup> groups;

    GroupRecyclerAdapter(Context context, List<SurveyGroup> groups) {

        this.context = context;

        this.groups = groups;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_survey_group, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView groupTitle;

        CustomViewHolder(View itemView) {

            super(itemView);

            groupTitle = (TextView) itemView.findViewById(R.id.group_title);
        }

        void bind(int position) {
            groupTitle.setText(groups.get(position).toString());
        }
    }
}
