package com.example.bouncingrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EdgeEffect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val list = List(20) { i -> "RecyclerView $i" }
        val myAdapter = RecyclerAdapter(list)
        val lm = LinearLayoutManager(this)

        recyclerView.apply {
            adapter = myAdapter
            layoutManager = lm
        }

        recyclerView.adapter = myAdapter
        recyclerView.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                return object : EdgeEffect(applicationContext) {

                    val OVER_SCROLL_COEF = 0.4f  // リスト端にスクロールしたときにどのくらいまでリスト外までスクロールさせるかを決める比率係数
                    val OVER_FLICK_COEF = 0.4f   // リスト端にフリックしたときに程度リスト外までスクロールさせるかを決める比率係数

                    // リストの端に行ったときに呼び出される
                    override fun onPull(deltaDistance: Float, displacement: Float) {
                        super.onPull(deltaDistance, displacement)
                        // deltaDistance 0~1f 前回からの変化した割合
                        // displacement 0~1f タップした位置の画面上の相対位置
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        val deltaY =
                            sign * view.height * deltaDistance * OVER_SCROLL_COEF
                        for (i in 0 until view.childCount) {
                            view.apply {
                                val holder =
                                    getChildViewHolder(getChildAt(i)) as RecyclerAdapter.CustomViewHolder
                                holder.springAnimY.cancel()
                                holder.itemView.translationY += deltaY
                            }
                        }
                    }


                    // 指を離したとき
                    override fun onRelease() {
                        super.onRelease()
                        for (i in 0 until view.childCount) {
                            view.apply {
                                val holder =
                                    getChildViewHolder(getChildAt(i)) as RecyclerAdapter.CustomViewHolder
                                holder.springAnimY.start()
                            }
                        }
                    }


                    // リストをフリックして画面端に行ったとき
                    override fun onAbsorb(velocity: Int) {
                        super.onAbsorb(velocity)
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        val translationVelocity = sign * velocity * OVER_FLICK_COEF
                        for (i in 0 until view.childCount) {
                            view.apply {
                                val holder =
                                    getChildViewHolder(getChildAt(i)) as RecyclerAdapter.CustomViewHolder
                                holder.springAnimY
                                    .setStartVelocity(translationVelocity)
                                    .start()
                            }
//                            view.forEachVisibleHolder<RecyclerAdapter.CustomViewHolder> {holder->
//                                holder.springAnimY
//                                    .setStartVelocity(translationVelocity)
//                                    .start()
//                            }
                        }
                    }
                }
            }
        }
    }

    // 拡張関数
    inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
        action: (T) -> Unit
    ) {
        for (i in 0 until childCount) {
            action(getChildViewHolder(getChildAt(i)) as T)
        }
    }
}