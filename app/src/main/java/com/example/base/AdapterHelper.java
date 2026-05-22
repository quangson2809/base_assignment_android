package com.example.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterHelper extends RecyclerView.Adapter<AdapterHelper.ViewHolder> {
    private List<Item> list;
    private List<Item> baseList;
    private Context context;
    private static int selectedPosition = RecyclerView.NO_POSITION;
    private static OnItemLongClickListener longClickListener;
    private static OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
        void onMenuClick(int actionId, int position);

    }

    public AdapterHelper(List<Item> list, Context context, OnItemLongClickListener longClickListener,OnItemClickListener onClickListener) {
        this.list = list;
        this.baseList = new ArrayList<>(list);
        this.context = context;
        this.longClickListener = longClickListener;
        this.clickListener = onClickListener;
        Log.d("AdapterHelper", "AdapterHelper: "+list.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);
        holder.tvId.setText(String.valueOf(item.getId()));
        holder.tvName.setText(item.makeName());

        //checkbox
        holder.cbItem.setOnCheckedChangeListener(null);
        holder.cbItem.setChecked(item.isChecked());
        holder.cbItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setIsCheck(isChecked ? 1 : 0);
            Toast.makeText(context, "Set check item: " + position, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setData(List<Item> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public Item getItem(int position){
        return list.get(position);
    }
    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
    public void removeSelectedItems(){
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isChecked()) {
                list.remove(i);
            }
        }
        notifyDataSetChanged();// refresh toàn bộ
    }

    public void addItem(Item item){
        list.add(item);
        notifyItemInserted(list.size()-1);
    }

    public void updateItem(int position, Item item){
        list.set(position,item);
        notifyItemChanged(position);
    }

    public Item getSelectedItem(){
        return list.get(selectedPosition);
    }

    public List<Integer> getSelectedIds(){
        List<Integer> ids = new ArrayList<>();
        for (Item item : list) {
            if (item.isChecked()) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    public int getSize(){
        return list.size();
    }

    public void filter(String query){
        list.clear();
        if (query == null || query.trim().isEmpty()) {
            list.addAll(baseList);
        } else {
            String key = query.toLowerCase().trim();
            for (Item item : baseList) {
                if (item.getName().toLowerCase().contains(key)) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void sort(){
        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvId;
        private CheckBox cbItem;
        private ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            cbItem = itemView.findViewById(R.id.cbItem);
            imageButton = itemView.findViewById(R.id.imageButton);

            itemView.setOnLongClickListener(view -> {
                selectedPosition = getAdapterPosition();
                if (selectedPosition != RecyclerView.NO_POSITION && longClickListener != null) {
                    contextMenu(view);
                    longClickListener.onItemLongClick(view, selectedPosition);
                }
                return true;
            });

            itemView.setOnClickListener(view -> {
                selectedPosition = getAdapterPosition();
                if(selectedPosition != RecyclerView.NO_POSITION && clickListener != null ){
                    clickListener.onItemClick(view,selectedPosition);
                }
            });
        }

        public void contextMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.getMenu().add(0,1,0,"Xóa");
            popupMenu.getMenu().add(0,2,1,"Sửa");
            popupMenu.getMenu().add(0,3,2,"Sắp xếp");
//          popupMenu.getMenuInflater().inflate(R.menu.context_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item ->{
                longClickListener.onMenuClick(item.getItemId(),selectedPosition);
                return true;
            });
            popupMenu.show();
        }
    }
}
