package com.example.recyclerviewactions

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.math.min

class SimpleSwipeHelperCallback(private val mAdapter: MyAdapter) :
    ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT or RIGHT) {
    private var currentDx = 0f                  // 현재 x 값
    private var clamp = 0f                      // 고정시킬 크기

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter.swapData(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    //    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val pos = viewHolder.layoutPosition
//        val data = mAdapter.dataAt(pos)
//
//        mAdapter.removeDataAt(viewHolder.layoutPosition)
//        Snackbar.make(viewHolder.itemView, "Item Removed.", Snackbar.LENGTH_LONG).setAction("Undo") {
//            mAdapter.insertDataAt(pos, data)
//        }.show()
//    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val isClamped = getTag(viewHolder)      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
            val newX = clampViewPositionHorizontal(
                dX,
                isClamped,
                isCurrentlyActive
            )  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

            // 고정시킬 시 애니메이션 추가
            if (newX == -clamp) {
                viewHolder.itemView.animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView, newX, dY, actionState, isCurrentlyActive)
        }
    }

    // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정
    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        // RIGHT 방향으로 swipe 막기
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isClamped) {
            // 현재 swipe 중이면 swipe되는 영역 제한
            if (isCurrentlyActive)
            // 오른쪽 swipe일 때
                if (dX < 0) dX / 3 - clamp
                // 왼쪽 swipe일 때
                else dX - clamp
            // swipe 중이 아니면 고정시키기
            else -clamp
        }
        // 고정할 수 없으면 newX는 스와이프한 만큼
        else dX / 6

        // newX가 0보다 작은지 확인
        return min(newX, max)
    }

    // isClamped를 view의 tag로 관리
    // isClamped = true : 고정, false : 고정 해제
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean =
        viewHolder.itemView.tag as? Boolean ?: false
}