package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.ProductsFragment;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.ServiceRuletaFragment;

import java.util.ArrayList;

public class ProfileViewPager  extends Fragment implements TabLayout.OnTabSelectedListener {

    private viewPager2Adapter adapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_view_pager_profile, container, false);

        ViewPager2 viewPager = root.findViewById(R.id.pagerProfile);

        arrayList.add(new ServiceRuletaFragment());
        arrayList.add(new ProductsFragment());

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        adapter = new viewPager2Adapter(getActivity().getSupportFragmentManager(), getLifecycle(), root);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new MarginPageTransformer(1500));

        tabLayout = root.findViewById(R.id.tab_layoutProfile);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.ic_perfil_white);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.ic_round_shopping_cart_24_white);
                            break;
                    }
                }).attach();

        tabLayout.addOnTabSelectedListener(this);

        return root;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition() == 0){
            tabLayout.setBackgroundColor(Color.parseColor("#F44336"));;
        }else if(tab.getPosition() == 1){
            tabLayout.setBackgroundColor(Color.parseColor("#DE000000"));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class viewPager2Adapter extends FragmentStateAdapter {
        private View v;

        public viewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, View v) {
            super(fragmentManager, lifecycle);
            this.v = v;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new BasketFragment();

            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }
}
