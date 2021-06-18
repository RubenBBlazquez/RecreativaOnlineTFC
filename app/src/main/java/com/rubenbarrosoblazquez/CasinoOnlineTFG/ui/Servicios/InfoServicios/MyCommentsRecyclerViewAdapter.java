package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.InfoServicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.Comments;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.ArrayList;
import java.util.List;

public class MyCommentsRecyclerViewAdapter extends RecyclerView.Adapter<MyCommentsRecyclerViewAdapter.ViewHolder> {

    private final List<Comments> mValues;
    public MyCommentsRecyclerViewAdapter(List<Comments> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.comment.setText(holder.mItem.getComment());
        holder.nombre.setText(holder.mItem.getEmail());

        int random_avatar=(int)(Math.random()*3);
        ArrayList<Integer> avatarDrawables=new ArrayList<>();
        avatarDrawables.add(R.drawable.avatar_1);
        avatarDrawables.add(R.drawable.avatar_2);
        avatarDrawables.add(R.drawable.avatar_3);
        avatarDrawables.add(R.drawable.avatar_4);
        avatarDrawables.add(R.drawable.avatar_5);

        holder.avatar.setBackgroundResource(avatarDrawables.get(random_avatar));


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView comment;
        public final TextView nombre;
        public final ImageView avatar;
        public Comments mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatar=(ImageView) view.findViewById(R.id.CommentAvatar);
            comment=(TextView) view.findViewById(R.id.Comment);
            nombre=(TextView)view.findViewById(R.id.emailComment);

        }
    }
}