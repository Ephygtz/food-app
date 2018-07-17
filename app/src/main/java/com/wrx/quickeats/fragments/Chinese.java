package com.wrx.quickeats.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wrx.quickeats.R;
import com.wrx.quickeats.adapter.MenuAdapter;
import com.wrx.quickeats.util.BeanClass;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chinese extends Fragment {

    View view;
    RecyclerView rv_chnese_menu;
    private BeanClass beanClass = new BeanClass();
    private ArrayList<BeanClass> menu = new ArrayList<BeanClass>();

    MenuAdapter menuAdapter;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chinese, container, false);

        rv_chnese_menu=(RecyclerView)view.findViewById(R.id.rv_chnese_menu);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rv_chnese_menu.setLayoutManager(linearLayoutManager);


        for (int i = 0; i < 4; i++) {

            beanClass.setMenu_dish_name("Chicken Curry");

            menu.add(beanClass);
        }

        menuAdapter = new MenuAdapter(getContext(), menu);
        rv_chnese_menu.setAdapter(menuAdapter);




        return view;
    }

}
