package com.book.novel.adapter

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 1:44
 */
interface ItemTouchHelperAdapter {
    //数据交换
    fun onItemMove(fromPosition: Int, toPosition: Int)

    //数据删除
    fun onItemDissmiss(position: Int)
}