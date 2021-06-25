package aeonlabs.common.libraries.InternalMessages;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.data.LoadedRecord;
import aeonlabs.common.libraries.R;

public class InternalMessagesViewAdapter
        extends RecyclerView.Adapter<InternalMessagesViewAdapter.MyViewHolder>
        {

    private LayoutInflater inflater;
    private int selectedPos = RecyclerView.NO_POSITION;
    private boolean IsSelected = false;
    private Activity activity;
    public static ArrayList<LoadedRecord> recordArrayList;
    private View view;

    public InternalMessagesViewAdapter(Activity _activity, ArrayList<LoadedRecord> _recordArrayList){
        this.activity=_activity;
        inflater = LayoutInflater.from(_activity);
        this.recordArrayList = _recordArrayList;

    }

    @Override
    public InternalMessagesViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.adapter_common_internal_messages, parent, false);
        InternalMessagesViewAdapter.MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LoadedRecord recordItem = recordArrayList.get(position);

        holder.description.setText(recordItem.getRecord(1));

        holder.itemView.setSelected(selectedPos == position);
        holder.cardView.setBackgroundColor(recordItem.isSelected() ? Color.CYAN : Color.WHITE);

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView cb = (ImageView) v;
                int idv = cb.getId();
                ((ActivityBase) activity).eventMsg.deleteMessage(position);
                recordArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,recordArrayList.size());
            }
        } );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);

                //TODO DIALOG WITH LONG DESCRIPTION
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public ImageView del;
        View view;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            cardView = itemView.findViewById(R.id.adapter_internal_card_file);
            description = itemView.findViewById(R.id.adapter_internal_msg_description);
            del = itemView.findViewById(R.id.adapter_internal_msg_del);
            del.setClickable(true);
        }
    }
}
