package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;


public class ServiciosFragment extends Fragment {

    private  viewPager2Adapter adapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_servicios, container, false);

        ViewPager2 viewPager=root.findViewById(R.id.pager);

        arrayList.add(new ServiceRuletaFragment());
        arrayList.add(new ProductsFragment());

        adapter=new viewPager2Adapter(getActivity().getSupportFragmentManager(),getLifecycle(),root);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new MarginPageTransformer(1500));

        TabLayout tabLayout = root.findViewById(R.id.tab_layout);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) ->{
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.ic_ruleta_de_casino_icon_white);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.ic_slot_machine_white);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.ic_black_jack_white);
                            break;

                    }
                }).attach();




        return root;
    }

    public class viewPager2Adapter extends FragmentStateAdapter {
        private View v;
        public viewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,  View v){
            super(fragmentManager, lifecycle);
            this.v=v;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ServiceRuletaFragment();
                case 1:
                    return new ServiceTragaperrasFragment();
                case 2:
                    return new ServiceBlackJackFragment();

            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }

    }
}