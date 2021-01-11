package com.apps.trollino.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity.makePostsByCategoryGridRecyclerViewForTapeActivity;

public class OtherCategoryPostFragment extends Fragment {

    private PrefUtils prefUtils;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public static OtherCategoryPostFragment getInstance() {
        OtherCategoryPostFragment tapeFragment = new OtherCategoryPostFragment();
        return tapeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefUtils = new PrefUtils(getActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));

        View view = inflater.inflate(R.layout.fragment_post_other_category, container, false);
        recyclerView = view.findViewById(R.id.other_category_recycler_tape);
        progressBar = view.findViewById(R.id.other_category_progress_bar_tape);

//        prefUtils.saveSelectedCategoryId(tab.getTag().toString());
//        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
//        makePostsByCategoryGridRecyclerViewForTapeActivity(getContext(), recyclerView, progressBar, prefUtils);

        return view;
    }
}
