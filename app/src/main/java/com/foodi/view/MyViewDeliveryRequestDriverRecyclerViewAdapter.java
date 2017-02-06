package com.foodi.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryRequest;
import com.foodi.view.ViewDeliveryRequestDriverFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyViewDeliveryRequestDriverRecyclerViewAdapter extends RecyclerView.Adapter<MyViewDeliveryRequestDriverRecyclerViewAdapter.ViewHolder> {

    private final List<DeliveryRequest> mValues;
    private final List<String> mKeys;
    private final OnListFragmentInteractionListener mListener;

    public MyViewDeliveryRequestDriverRecyclerViewAdapter(List<String> keys, List<DeliveryRequest> items, OnListFragmentInteractionListener listener) {
        mKeys = keys;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_del_req_driver, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mKey=mKeys.get(position);
        holder.mItem = mValues.get(position);
        holder.mOrderNumberView.setText(mValues.get(position).getOrderNumber());
        holder.mRestaurantNameView.setText(mValues.get(position).getRestaurantName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    int offerPrice = Integer.parseInt(holder.mOfferPriceView.getText().toString());
                    mListener.onViewDeliveryRequestFragmentInteraction(holder.mKey,holder.mItem,offerPrice);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mOrderNumberView;
        public final TextView mRestaurantNameView;
        public final EditText mOfferPriceView;
        public final TextView mUpdatePriceView;
        public DeliveryRequest mItem;
        public String mKey;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderNumberView = (TextView) view.findViewById(R.id.order_number);
            mRestaurantNameView = (TextView) view.findViewById(R.id.restaurant_name);
            mOfferPriceView = (EditText) view.findViewById(R.id.offer_price);
            mUpdatePriceView = (TextView) view.findViewById(R.id.update_price);
        }
    }
}
