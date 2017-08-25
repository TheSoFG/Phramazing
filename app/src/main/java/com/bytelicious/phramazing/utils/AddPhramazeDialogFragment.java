package com.bytelicious.phramazing.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bytelicious.phramazing.R;
import com.bytelicious.phramazing.model.Phramaze;

import org.parceler.Parcels;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * @author ylyubenov
 */

public class AddPhramazeDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = AddPhramazeDialogFragment.class.getSimpleName();
    private static final String PHRAMAZE_ARG = "AddPhramazeDialogFragment.phramaze";

    private Phramaze phramaze;

    private TextInputEditText phramazeEditText;
    private ProgressBar loadingIndicator;
    private AutoCompleteTextView authorEditText;
    private Button cancelButton;
    private Button saveDataButton;

    public static AddPhramazeDialogFragment newInstance() {
        return new AddPhramazeDialogFragment();
    }

    public static AddPhramazeDialogFragment newInstance(Phramaze phramaze) {
        Bundle args = new Bundle();
        args.putParcelable(PHRAMAZE_ARG, Parcels.wrap(phramaze));
        AddPhramazeDialogFragment fragment = new AddPhramazeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface PhramazeCreationListener {
        void onPhramazeCreated(String text, String author);

        void onPhramazeEdited(String id, String text, String author);
    }

    private PhramazeCreationListener phramazeCreationListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Object host = getTargetFragment();
        if (host instanceof PhramazeCreationListener) {
            phramazeCreationListener = (PhramazeCreationListener) host;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            phramaze = Parcels.unwrap(args.getParcelable(PHRAMAZE_ARG));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_phramaze, container, false);
        initViews(view);
        setupViews();
        return view;
    }

    private void initViews(View view) {
        phramazeEditText = (TextInputEditText) view.findViewById(R.id.phramaze_text_edittext);
        authorEditText = (AutoCompleteTextView) view.findViewById(R.id.author_edittext);
        loadingIndicator = (ProgressBar) view.findViewById(R.id.loading_indicator);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        saveDataButton = (Button) view.findViewById(R.id.save_button);
    }

    private void setupViews() {
        if (phramaze != null) {
            authorEditText.setText(phramaze.getAuthor());
            authorEditText.setSelection(phramaze.getAuthor().length());
            phramazeEditText.setText(phramaze.getPhrase());
            phramazeEditText.setSelection(phramaze.getPhrase().length());
            saveDataButton.setText(R.string.edit);
        }

        try (Realm realmInstance = Realm.getDefaultInstance()) {
            Observable.fromIterable(realmInstance.where(Phramaze.class)
                    .distinct("author"))
                    .map(Phramaze::getAuthor)
                    .toList()
                    .subscribe(rs -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                rs.toArray(new String[0])); // https://shipilev.net/blog/2016/arrays-wisdom-ancients/
                        authorEditText.setAdapter(adapter);
                    });
        }

        cancelButton.setOnClickListener(this);
        saveDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                if (phramaze == null) {
                    if (phramazeCreationListener != null) {
                        phramazeCreationListener.onPhramazeCreated(phramazeEditText.getText().toString(),
                                authorEditText.getText().toString());
                    }
                } else {
//                    phramaze.setAuthor(authorEditText.getText().toString());
//                    phramaze.setPhrase(phramazeEditText.getText().toString());
                    if (phramazeCreationListener != null) {
                        phramazeCreationListener.onPhramazeEdited(phramaze.getId(),
                                phramazeEditText.getText().toString(),
                                authorEditText.getText().toString());
                    }
                }
            case R.id.cancel_button:
                dismiss();
                break;
        }
    }
}