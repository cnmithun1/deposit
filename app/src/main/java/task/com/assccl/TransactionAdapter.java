package task.com.assccl;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionModel> transactionList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView transactText;

        public MyViewHolder(View view) {
            super(view);
            transactText = (TextView) view.findViewById(R.id.transactText);
        }
    }

    public TransactionAdapter(List<TransactionModel> transactionList){
        this.transactionList = transactionList;
    }


    @Override
    public void onBindViewHolder(TransactionAdapter.MyViewHolder holder, int position) {
        TransactionModel t = transactionList.get(position);
        holder.transactText.setText(transactionList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transactions,viewGroup, false);
        return new MyViewHolder(v);
    }
}
