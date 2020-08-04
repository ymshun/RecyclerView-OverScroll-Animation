package com.example.bouncingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_item.view.*

class RecyclerAdapter(private val customList: List<String>) :
    RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>() {

    // View Holder
    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.content

        // view holderにspringAnimationオブジェクト追加
        val springAnimY: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
            .setSpring(SpringForce().apply {
                finalPosition = 0f          //ばねの静止位置の設定
                dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY  //ばねの減衰比(0~1f)
                stiffness = SpringForce.STIFFNESS_VERY_LOW           //ばねの剛性
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.cell_item, parent, false)

        return CustomViewHolder(item)
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int {
        return customList.size
    }

    // ViewHolderに表示する画像とテキストを挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.content.text = customList[position]
    }
}