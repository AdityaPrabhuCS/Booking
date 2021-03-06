package com.main.test4.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.test4.R;
import com.main.test4.adapter.TicketAdapter;
import com.main.test4.model.Ticket;
import com.main.test4.model.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Fragment created to display ticket information inside a DialogFragment
public class TicketFragment extends DialogFragment {

    private static final String ARG_TICKET = "ticket";

    private Ticket mTicket;

    public static TicketFragment newInstance(Ticket ticket) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_TICKET, ticket);

        TicketFragment ticketFragment = new TicketFragment();
        ticketFragment.setArguments(arguments);
        ticketFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return ticketFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTicket = (Ticket) getArguments().getSerializable(ARG_TICKET);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setTitle(R.string.ticket_dialog_title);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_ticket, container, false);

        TextView tvTableName = (TextView) root.findViewById(R.id.tableName);
        tvTableName.setText(String.format(getString(R.string.table_number_string), mTicket.getTableNumber()));

        TextView tvDate = (TextView) root.findViewById(R.id.ticketDate);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date now = Calendar.getInstance().getTime();
        tvDate.setText(df.format(now));

        TextView tvTableTotal = (TextView) root.findViewById(R.id.tableTotal);
        tvTableTotal.setText(Utils.formatDoubleToPrice(mTicket.getTotal()));

        RecyclerView mList = (RecyclerView) root.findViewById(R.id.coursesList);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(new TicketAdapter(getActivity(), mTicket.getRows()));

        Button cancelBtn = (Button) root.findViewById(R.id.ticket_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                dismiss();
            }
        });

        Button closeTableBtn = (Button) root.findViewById(R.id.ticket_close);
        closeTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                dismiss();
            }
        });

        return root;
    }
}
