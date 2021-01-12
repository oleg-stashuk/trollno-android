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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetMostDiscusPosts;

import static android.content.Context.MODE_PRIVATE;
import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class DiscussPostFragment extends Fragment {

    private PrefUtils prefUtils;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public static DiscussPostFragment getInstance() {
        DiscussPostFragment tapeFragment = new DiscussPostFragment();
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
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        View view = inflater.inflate(R.layout.fragment_post_discuss, container, false);
        recyclerView = view.findViewById(R.id.discuss_recycler_tape);
        progressBar = view.findViewById(R.id.discuss_progress_bar_tape);

        makeLinerRecyclerView();
        return view;
    }

    private void makeLinerRecyclerView() {
        DiscussPostsAdapter adapter = new DiscussPostsAdapter((BaseActivity) getContext(), prefUtils, DataListFromApi.getInstance().getDiscussPostsList(), newsVideoItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        new Thread(() -> {
            GetMostDiscusPosts.makeGetNewPosts(getContext(), prefUtils, adapter, progressBar);
        }).start();

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    GetMostDiscusPosts.makeGetNewPosts(getContext(), prefUtils, adapter, progressBar);
                }).start(), 1000);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final DiscussPostsAdapter.OnItemClick<PostsModel.PostDetails> newsVideoItemListener = (item, position) -> {
        openPostActivity(getContext(), item, prefUtils, false);
    };
}
