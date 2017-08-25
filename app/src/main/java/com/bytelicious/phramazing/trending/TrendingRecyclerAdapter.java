package com.bytelicious.phramazing.trending;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytelicious.phramazing.R;
import com.bytelicious.phramazing.home.ProfileRecyclerAdapter;
import com.bytelicious.phramazing.model.Phramaze;
import com.bytelicious.phramazing.utils.DateUtils;

import io.realm.OrderedRealmCollection;

/**
 * @author ylyubenov
 */

public class TrendingRecyclerAdapter extends ProfileRecyclerAdapter {

    TrendingRecyclerAdapter(@Nullable OrderedRealmCollection<Phramaze> data) {
        super(data);
    }

    @Override
    public ProfileRecyclerAdapter.PhramazeProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PhramazeTrendingViewHolder(inflater.inflate(R.layout.item_phramaze_trending, parent,
                false));
    }

    class PhramazeTrendingViewHolder extends ProfileRecyclerAdapter.PhramazeProfileViewHolder {

        private TextView phramazeText;
        private TextView phramazeDate;
        private TextView phramazeAuthor;
        private RelativeLayout layout;

        public PhramazeTrendingViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.parent_relative_layout);
            phramazeText = (TextView) itemView.findViewById(R.id.phramaze_text_text_view);
            phramazeDate = (TextView) itemView.findViewById(R.id.phramaze_author_text_view);
            phramazeAuthor = (TextView) itemView.findViewById(R.id.phramaze_creation_date_text_view);
        }

        protected void bind(Phramaze phramaze) {
            phramazeText.setText(phramaze.getPhrase());
            phramazeAuthor.setText(phramaze.getAuthor());
            phramazeDate.setText(DateUtils.formatPhramazeDate(phramaze.getCreationDate()));
            layout.setOnClickListener(v -> {
                if (onPhramazeAction != null) {
                    onPhramazeAction.onPhramazeEdit(phramaze.getId());
                }
            });
        }

    }
}
