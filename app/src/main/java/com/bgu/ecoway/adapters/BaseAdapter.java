package com.bgu.ecoway.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseAdapter.ViewHolder<T, B>> implements BindableAdapter<T> {
    protected List<? extends T> data = new ArrayList<>();

    public BaseAdapter() {
    }

    public List<? extends T> getData() {
        return data;
    }

    public synchronized void setData(List<? extends T> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T, B> holder, int position) {
        T item = data.get(position);
        holder.bindVariables(item);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static abstract class ViewHolder<T, B extends ViewDataBinding> extends RecyclerView.ViewHolder {
        protected B binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public abstract void setVariables(T item);

        public void bindVariables(T item) {
            setVariables(item);
            binding.executePendingBindings();
        }
    }
}
