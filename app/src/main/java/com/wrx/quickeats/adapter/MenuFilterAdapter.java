package com.wrx.quickeats.adapter;

///**
// * Created by mobulous51 on 12/9/17.
// */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CategoryListResponse;
import com.wrx.quickeats.databinding.FilterSingleRowBinding;

import java.util.ArrayList;

public class MenuFilterAdapter  extends RecyclerView.Adapter<MenuFilterAdapter.FilterVHolder>{

    private FilterListener listener;
    private ArrayList<CategoryListResponse> arrayList;
    private LayoutInflater inflater;
    private Context context;

    public MenuFilterAdapter(Context context, ArrayList<CategoryListResponse> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public FilterVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            this.inflater = LayoutInflater.from(parent.getContext());

        FilterSingleRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.filter_single_row, parent, false);

        return new FilterVHolder(binding);
    }

    @Override
    public void onBindViewHolder(FilterVHolder holder, int position) {

        CategoryListResponse categoryListResponse = arrayList.get(position);
        String name = categoryListResponse.getCategory_name();
        //String num = filterBean.getNumber();

        holder.binding.tvFilterName.setText(name);
        holder.binding.tvNo.setText(categoryListResponse.getNumber());


        if(categoryListResponse.isSelected())
        {
            holder.binding.tvFilterName.setTextColor(ContextCompat.getColor(context,R.color.home_header_color));
            holder.binding.tvNo.setTextColor(ContextCompat.getColor(context,R.color.home_header_color));
        }
        else {
            holder.binding.tvFilterName.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.binding.tvNo.setTextColor(ContextCompat.getColor(context,R.color.black));

        }

//        if(filterBean.getFilterName().equalsIgnoreCase("Most Popular"))
//        {
//            holder.binding.tvFilterName.setTextColor(ContextCompat.getColor(context,R.color.home_header_color));
//            holder.binding.tvNo.setTextColor(ContextCompat.getColor(context,R.color.home_header_color));
//        }

    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        else
            return 0;
    }


    public void setUpFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    //    CLASS: VIEW HOLDER
    class FilterVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FilterSingleRowBinding binding;

        public FilterVHolder(FilterSingleRowBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                default:
                    listener.onItemClick(binding.getRoot(),getAdapterPosition());
                    break;

            }
        }
    }


    //    INTERFACE: LISTENER
    public interface FilterListener {

        void onItemClick(View view, int pos);

    }
}
