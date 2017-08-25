package com.bytelicious.phramazing.home;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytelicious.phramazing.R;
import com.bytelicious.phramazing.model.Phramaze;
import com.bytelicious.phramazing.utils.DateUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author ylyubenov
 */

public class ProfileRecyclerAdapter extends RealmRecyclerViewAdapter<Phramaze,
        ProfileRecyclerAdapter.PhramazeProfileViewHolder> {

    protected OnPhramazeAction onPhramazeAction;

    protected ProfileRecyclerAdapter(@Nullable OrderedRealmCollection<Phramaze> data) {
        super(data, true);
    }

    public void setOnPhramazeAction(OnPhramazeAction onPhramazeAction) {
        this.onPhramazeAction = onPhramazeAction;
    }

    public interface OnPhramazeAction {
        void onPhramazeDeletion(String phramazeId);
        void onPhramazeEdit(String phramazeId);
    }

    @Override
    public PhramazeProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PhramazeProfileViewHolder(inflater.inflate(R.layout.item_phramaze_profile, parent,
                false));
    }

    @Override
    public void onBindViewHolder(PhramazeProfileViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    void onItemRemove(final RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        if (onPhramazeAction != null) {
            //noinspection ConstantConditions
            onPhramazeAction.onPhramazeDeletion(getItem(adapterPosition).getId());
        }
        notifyItemRemoved(adapterPosition);
    }

    protected class PhramazeProfileViewHolder extends RecyclerView.ViewHolder {

        private TextView phramazeText;
        private TextView phramazeDate;
        private RelativeLayout layout;

        public PhramazeProfileViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.parent_relative_layout);
            phramazeText = (TextView) itemView.findViewById(R.id.phramaze_text_text_view);
            phramazeDate = (TextView) itemView.findViewById(R.id.phramaze_creation_date_text_view);
        }

        protected void bind(Phramaze phramaze) {
            phramazeText.setText(phramaze.getPhrase());
            phramazeDate.setText(DateUtils.formatPhramazeDate(phramaze.getCreationDate()));
            layout.setOnClickListener(v -> {
                if (onPhramazeAction != null) {
                    onPhramazeAction.onPhramazeEdit(phramaze.getId());
                }
            });
        }

    }

}
