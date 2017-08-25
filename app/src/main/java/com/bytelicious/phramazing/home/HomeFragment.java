package com.bytelicious.phramazing.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytelicious.phramazing.R;
import com.bytelicious.phramazing.app.PhramazingFragment;
import com.bytelicious.phramazing.model.Phramaze;
import com.bytelicious.phramazing.model.User;
import com.bytelicious.phramazing.utils.AddPhramazeDialogFragment;

import io.realm.RealmResults;

/**
 * @author ylyubenov
 */

public class HomeFragment extends PhramazingFragment implements
        NestedScrollView.OnScrollChangeListener, AddPhramazeDialogFragment.PhramazeCreationListener,
        ProfileRecyclerAdapter.OnPhramazeAction {

    private RecyclerView profileRecyclerView;
    private FloatingActionButton addPhramaze;
    private NestedScrollView nestedScrollView;
    private CoordinatorLayout coordinatorLayout;

    private ProfileRecyclerAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
        attachTouchHelper(profileRecyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        removeReferences();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profileRecyclerView = null;
        coordinatorLayout = null;
        nestedScrollView = null;
        addPhramaze = null;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {
            addPhramaze.hide();
        } else {
            addPhramaze.show();
        }
    }

    @Override
    public void onPhramazeCreated(String phrase, String author) {
        realmInstance.executeTransaction(realm -> {
            Phramaze phramaze = new Phramaze();
            phramaze.setAuthor(author);
            phramaze.setPhrase(phrase);
            realm.where(User.class).findFirst().getPhramazes().add(phramaze);
            realm.insertOrUpdate(phramaze);
        });
    }

    @Override
    public void onPhramazeEdited(String id, String phrase, String author) {
        realmInstance.executeTransaction(realm -> {
            Phramaze oldPhramaze = realmInstance.where(Phramaze.class)
                    .equalTo("id", id)
                    .findFirst();
            oldPhramaze.setPhrase(phrase);
            oldPhramaze.setAuthor(author);
        });
    }

    @Override
    public void onPhramazeDeletion(String phramazeId) {
        realmInstance.executeTransaction(realm -> realm.where(Phramaze.class)
                .equalTo("id", phramazeId)
                .findFirst()
                .deleteFromRealm());
    }

    @Override
    public void onPhramazeEdit(String phramazeId) {
        Phramaze phramaze = realmInstance.where(Phramaze.class)
                .equalTo("id", phramazeId)
                .findFirst();
        if (phramaze != null) {
            AddPhramazeDialogFragment dialogFragment = AddPhramazeDialogFragment
                    .newInstance(phramaze);
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getActivity().getSupportFragmentManager(),
                    AddPhramazeDialogFragment.TAG);
        }
    }

    protected RealmResults<Phramaze> getPhramazes() {
        return realmInstance
                .where(Phramaze.class)
                .findAll()
                .where()
                .equalTo("users.username", "Dhaka")
                .findAll();

//        return realmInstance
//                .where(Phramaze.class)
//                .findAll()
//                .where()
//                .equalTo("author", "Dhaka")
//                .findAllSorted("creationDate");
    }

    protected ProfileRecyclerAdapter getAdapter() {
        return new ProfileRecyclerAdapter(getPhramazes());
    }

    private void initViews(View view) {
        addPhramaze = (FloatingActionButton) view.findViewById(R.id.add_phramaze);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        profileRecyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        // fixing FAB random top left anchoring
        nestedScrollView.post(() -> nestedScrollView.requestLayout());
    }

    private void initAdapter() {
        adapter = getAdapter();
        adapter.setOnPhramazeAction(this);
        itemTouchHelper = new ItemTouchHelper(new PhramazeAdapterTouchHelper(adapter,
                profileRecyclerView));
        profileRecyclerView.setAdapter(adapter);
    }

    private void setupViews() {
        addPhramaze.setOnClickListener(v -> {
            AddPhramazeDialogFragment dialogFragment = AddPhramazeDialogFragment.newInstance();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getActivity().getSupportFragmentManager(),
                    AddPhramazeDialogFragment.TAG);
        });
        nestedScrollView.setOnScrollChangeListener(this);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL, false));
        profileRecyclerView.setNestedScrollingEnabled(false);
    }

    private void removeReferences() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
        adapter.setOnPhramazeAction(null);
        attachTouchHelper(null);
    }

    private void attachTouchHelper(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}