package com.apps.trollino.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PrefUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.networking.main_group.GetNewPosts.makeGetNewPosts;

public class FreshPostFragment extends Fragment {

    private PrefUtils prefUtils;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public static FreshPostFragment getInstance() {
        FreshPostFragment freshPostFragment = new FreshPostFragment();
        return freshPostFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefUtils = new PrefUtils(getActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        View view = inflater.inflate(R.layout.fragment_post_fresh, container, false);
        recyclerView = view.findViewById(R.id.fresh_recycler_tape);
        progressBar = view.findViewById(R.id.fresh_progress_bar_tape);

        makeNewPostsRecyclerView();
        return view;
    }

    private void makeNewPostsRecyclerView() {
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        PostListAdapter adapter = new PostListAdapter((BaseActivity) getContext(), prefUtils, DataListFromApi.getInstance().getNewPostsList(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(DataListFromApi.getInstance().getNewPostsList().isEmpty()) {
            new Thread(() -> {
                makeGetNewPosts(getContext(), prefUtils, adapter, progressBar);
            }).start();
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    makeGetNewPosts(getContext(), prefUtils, adapter, progressBar);
                }).start(), 1000);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(getContext(), item, prefUtils, false);
    };
}
