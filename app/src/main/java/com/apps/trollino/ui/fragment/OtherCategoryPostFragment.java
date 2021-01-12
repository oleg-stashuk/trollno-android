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
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetPostsByCategory;

import static android.content.Context.MODE_PRIVATE;
import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

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

//        makeGridRecycler();
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        return view;
    }

    public void makeGridRecycler() {
        PostListAdapter adapter = new PostListAdapter((BaseActivity) getContext(), prefUtils, PostListByCategoryFromApi.getInstance().getPostListByCategory(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(PostListByCategoryFromApi.getInstance().getPostListByCategory().isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> new Thread(() -> {
                    GetPostsByCategory.getPostsByCategory(getContext(), prefUtils, adapter, progressBar);
            }).start(), 1000);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                        GetPostsByCategory.getPostsByCategory(getContext(), prefUtils, adapter, progressBar);
                }).start(), 1000);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(getContext(), item, prefUtils, true);
    };
}
