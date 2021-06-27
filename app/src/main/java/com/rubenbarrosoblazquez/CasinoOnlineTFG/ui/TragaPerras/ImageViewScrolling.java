package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.TragaPerras;

import android.animation.Animator;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.util.Random;


public class ImageViewScrolling extends FrameLayout {
    private static int ANIMATION_DUR = 300;
    private ImageView current_image,secondImage,thirdImage;
    private int results[]={0,0,0};
    public static int BAR[] = {0,2} , SEVEN[] ={3,4} , LEMON[] = {5,10},ORANGE[] = {11,16},TRIPLE[] = {17}, WATERMELON[]={18,21};
    private int tag;
    private int movement=1;
    IEventEnd eventEnd;
    public boolean isMovement=true;

    public IEventEnd getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(IEventEnd eventEnd) {
        this.eventEnd = eventEnd;
    }

    public ImageViewScrolling(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ImageViewScrolling(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setTag(int tag){
        this.tag=tag;
    }

     public void init(Context context){
         View v=LayoutInflater.from(context).inflate(R.layout.scrolling_views, this);
         current_image = (ImageView) v.findViewById(R.id.firstImage);
         secondImage = (ImageView) v.findViewById(R.id.secondImage);
         thirdImage = (ImageView) v.findViewById(R.id.thirdImage);

     }

     public void setValueRandom(int rotate_count) {
        isMovement=true;
        slotMachineMovement slotMachineMovement = new slotMachineMovement();
        slotMachineMovement.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,rotate_count);

     }
    private void setImage(ImageView imageView, int i) {


            if(i >= BAR[0] && i <=BAR[1]){
                imageView.setImageResource(R.drawable.bar_done);
            } else if(i >= SEVEN[0] && i <=SEVEN[1]){
                imageView.setImageResource(R.drawable.sevent_done);
            }else if(i >= LEMON[0] && i <=LEMON[1]){
                imageView.setImageResource(R.drawable.lemon_done);
            }else if(i >= ORANGE[0] && i <=ORANGE[1]){
                imageView.setImageResource(R.drawable.orange_done);
            }else if(i == TRIPLE[0]){
                imageView.setImageResource(R.drawable.triple_done);
            } else if(i >= WATERMELON[0] && i <=WATERMELON[1]){
                imageView.setImageResource(R.drawable.waternelon_done);
            }

            imageView.setTag(i);

    }

    public class slotMachineMovement extends AsyncTask<Integer, Integer, String> {
        public boolean salir = false;

        @Override
        protected String doInBackground(Integer... integers) {
                for (int i = 0; i < integers[0]; i++) {
                    try {
                        if(i != integers[0].intValue()-1){
                            publishProgress(i);
                        }else{
                            isMovement=false;
                            eventEnd.eventEnd(results,integers[0],tag);
                        }
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            return String.valueOf(integers[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            if (movement == 2){
                setImage(secondImage,new Random().nextInt(22));
                results[1]= (int) secondImage.getTag();
            }else if(movement==3){
                setImage(thirdImage,new Random().nextInt(22));
                results[2]= (int) thirdImage.getTag();
            }else{
                setImage(current_image,new Random().nextInt(22));
                results[0]= (int) current_image.getTag();
                movement = 1;
            }

            movement++;


        }
    }


}
