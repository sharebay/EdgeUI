package com.sharebay.vam.edgeui._modules.part_recyclerview;

import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sharebay.vam.edgeui.R;
import com.sharebay.vam.edgeui.commons.recyclerview.CommonAdapterHelper.RecyclerViewCommonAdapter;
import com.sharebay.vam.edgeui.commons.recyclerview.CommonAdapterHelper.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DroppableRecyclerActivity extends AppCompatActivity {

    private static final String TAG = "DroppableRecyclerActivi";
    private String[] titles = {"美食", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "机票/火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "小吃快餐", "面膜", "洗浴/汗蒸", "母婴亲子", "生活服务", "婚纱摄影", "学习培训", "家装", "结婚", "全部分配"};

    RecyclerView recyclerDrag;
    RecyclerViewCommonAdapter recyclerAdapter;
    List<String> datas;


    private ItemTouchHelper mItemTouchHelper;
    private boolean isShowGrid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droppable_recycler);
        initData();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"列表显示");
        menu.add(0,1,0,"网格显示");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = item.getItemId();
        switch (index){
            case 0:
                isShowGrid = false;
                updateViewStyle();
                Toast.makeText(this, "列表显示", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                isShowGrid = true;
                updateViewStyle();
                Toast.makeText(this, "网格显示", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        recyclerDrag = (RecyclerView) findViewById(R.id.recyclerDrag);

        //列状、网状布局。
        if (!isShowGrid){
            recyclerDrag.setLayoutManager(new LinearLayoutManager(this));
            recyclerDrag.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else{
            recyclerDrag.setLayoutManager(new GridLayoutManager(this, 4));
            recyclerDrag.addItemDecoration(new DividerGridItemDecoration(this));
        }

        recyclerAdapter = new RecyclerViewCommonAdapter<String>(this, R.layout.recycler_item_linear, datas) {
            @Override
            public void convert(ViewHolder holder, String title) {
                holder.setText(R.id.tvName,title);
                Log.e(TAG, "convert: !!!");
            }
        };
        recyclerDrag.setAdapter(recyclerAdapter);

        /*
        * 短按和长按事件
        * */
        recyclerDrag.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerDrag) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Toast.makeText(DroppableRecyclerActivity.this, "vh.position="+vh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                //界面的跳转  intent传递vh中的参数值到下一个activity
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                mItemTouchHelper.startDrag(vh);

                //获取系统震动服务
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                vib.vibrate(70);
            }
        });

        /*
        * 滑动事件
        * */
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，
             * 如果是网格类RecyclerView 则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(datas, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(datas, i, i - 1);
                    }
                }
                recyclerAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        /*
        * 用来绑定 mItemTouchHelper 和 RecycleView的吗？
        * */
        mItemTouchHelper.attachToRecyclerView(recyclerDrag);
    }

    private void initData() {
        datas = new ArrayList<>();
        datas = Arrays.asList(titles);
    }

    private void updateViewStyle(){
        //列状、网状布局。
        if (!isShowGrid){
            recyclerDrag.setLayoutManager(new LinearLayoutManager(this));
            //修饰效果是叠加的
            // recyclerDrag.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else{
            recyclerDrag.setLayoutManager(new GridLayoutManager(this, 4));
            //recyclerDrag.addItemDecoration(new DividerGridItemDecoration(this));
        }
    }
}
