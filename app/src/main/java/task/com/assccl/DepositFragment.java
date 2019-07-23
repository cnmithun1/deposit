package task.com.assccl;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {

    private List<TransactionModel> transactionModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private static View view;


    public DepositFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deposit, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        transactionAdapter = new TransactionAdapter(transactionModelList);
        loadData();
        return view;
    }

    private void loadData(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration horizontalDivider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(horizontalDivider);
        recyclerView.setAdapter(transactionAdapter);
        prepareTransactData();
    }

    private void prepareTransactData(){
        TransactionModel model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        model = new TransactionModel("2 June 2016");
        transactionModelList.add(model);

        transactionAdapter.notifyDataSetChanged();
    }

}
