package com.bytelicious.phramazing.trending;

import com.bytelicious.phramazing.home.HomeFragment;
import com.bytelicious.phramazing.home.ProfileRecyclerAdapter;
import com.bytelicious.phramazing.model.Phramaze;

import io.realm.RealmResults;

/**
 * @author ylyubenov
 */

public class TrendingFragment extends HomeFragment {

    public static final String TAG = TrendingFragment.class.getSimpleName();

    public static TrendingFragment newInstance() {
        return new TrendingFragment();
    }

    @Override
    protected ProfileRecyclerAdapter getAdapter() {
        return new TrendingRecyclerAdapter(getPhramazes());
    }

    @Override
    protected RealmResults<Phramaze> getPhramazes() {
        return realmInstance
                .where(Phramaze.class)
                .findAllSorted("creationDate");
    }
}