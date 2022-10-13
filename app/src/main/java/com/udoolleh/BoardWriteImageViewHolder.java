package com.udoolleh;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardWriteImageViewHolder extends RecyclerView.ViewHolder {
    ImageView boardWrite_image_item;
    public BoardWriteImageViewHolder(@NonNull View itemView) {
        super(itemView);

        boardWrite_image_item = itemView.findViewById(R.id.boardWrite_Image_item);
    }

    public void onBind(BoardWriteImageItem boardWriteImageItem) {

    }
}
