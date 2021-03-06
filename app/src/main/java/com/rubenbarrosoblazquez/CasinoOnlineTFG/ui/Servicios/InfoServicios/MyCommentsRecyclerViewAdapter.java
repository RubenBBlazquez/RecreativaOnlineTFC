package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.InfoServicios;

import android.util.Log;
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
    private final InfoServicioFragment rview;
    public MyCommentsRecyclerViewAdapter(InfoServicioFragment rview,List<Comments> items) {
        mValues = items;
        this.rview=rview;
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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rview.openDialogComment();
            }
        });
        holder.comment.setText(holder.mItem.getComment());
        holder.nombre.setText(holder.mItem.getEmail());
        holder.avatar.setImageBitmap(holder.mItem.getDirImg());


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