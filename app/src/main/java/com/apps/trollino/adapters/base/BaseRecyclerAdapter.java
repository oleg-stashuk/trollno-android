package com.apps.trollino.adapters.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.ui.base.BaseActivity;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseRecyclerAdapter<I> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    BaseActivity baseActivity;

    private BaseItem selectedView;
    private int selectedItemPosition = -1;
    private List<I> items;
    private OnItemClick<I> onItemClick;

    public void setOnItemClick(BaseRecyclerAdapter.OnItemClick<I> onItemClick) {
        this.onItemClick = onItemClick;
    }

    protected BaseRecyclerAdapter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        this.items = new LinkedList<>();
    }

    protected BaseRecyclerAdapter(BaseActivity baseActivity, List<I> items) {
        this.baseActivity = baseActivity;
        this.items = items;
    }

    protected BaseRecyclerAdapter(BaseActivity baseActivity, List<I> items, OnItemClick<I> onItemClick) {
        this.baseActivity = baseActivity;
        this.items = items;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public BaseItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return createViewHolder(
                LayoutInflater.from(baseActivity).inflate(getCardLayoutID(), viewGroup, false));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseItem, int i) {
        ((BaseItem) baseItem).beforeBind();
        ((BaseItem) baseItem).bind(items.get(i));
    }

    private I getItem(int position) {
        return items.get(position);
    }

    protected abstract int getCardLayoutID();

    protected abstract  BaseItem createViewHolder(View view);

//    public void addItems(List<I> items) {
//        this.items.clear();
//        this.items.addAll(items);
//        notifyDataSetChanged();
//    }

    private void onItemClick(int position) {
        if (onItemClick != null) {
            onItemClick.onItemClick(getItem(position), position);
//            onItemClick.onItemLongClick(getItem(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }





    protected abstract class BaseItem extends RecyclerView.ViewHolder {
        protected BaseItem(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(getAdapterPosition());
                    if(selectedItemPosition!=getAdapterPosition()){
                        onItemSelected();
                        if (selectedView != null) {
                            selectedView.onItemUnSelected();
                        }
                        selectedItemPosition = getAdapterPosition();
//                        selectedView = this;
                        selectedView = selectedView;
                    }
                }
            });
        }

        /*
            protected abstract class BaseItem extends RecyclerView.ViewHolder {
        protected BaseItem(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                onItemClick(getAdapterPosition());
                if(selectedItemPosition!=getAdapterPosition()){
                    onItemSelected();
                    if (selectedView != null) {
                        selectedView.onItemUnSelected();
                    }
                    selectedItemPosition = getAdapterPosition();
                    selectedView = this;
                }
            });
        }
         */

        public abstract void bind(I item);

        void onItemSelected() {

        }

        void onItemUnSelected() {

        }

        private void beforeBind(){
            if (selectedItemPosition == getAdapterPosition()) {
                onItemSelected();
            } else {
                onItemUnSelected();
            }
        }
    }

    public interface OnItemClick<I> {
        void onItemClick(I item, int position);
//        void onItemLongClick(I item, int position);
    }
}