package com.foodi.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.DeliveryRequest;
import com.foodi.view.ViewDeliveryRequestDriverFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DeliveryRequest} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyViewDeliveryRequestDriverRecyclerViewAdapter extends RecyclerView.Adapter<MyViewDeliveryRequestDriverRecyclerViewAdapter.ViewHolder> {

    private final List<DeliveryRequest> mRequests;              //DeliveryRequest Objects
    private final List<String> mRequestKeys;                    //DeliveryRequest Key
    private final List<String> mOfferKeys;                      //DeliveryOffer key
    private final List<DeliveryOffer> mDeliveryOffers;          //DeliveryOffer Objects
    private final OnListFragmentInteractionListener mListener;  //Listener to communicate with MainMenuActivity

    public MyViewDeliveryRequestDriverRecyclerViewAdapter(List<String> keys, List<DeliveryRequest> deliveryRequests,
                                                          List<String> offerkeys, List<DeliveryOffer> deliveryOffers,
                                                          OnListFragmentInteractionListener listener) {
        mRequestKeys = keys;
        mRequests = deliveryRequests;
        mOfferKeys = offerkeys;
        mDeliveryOffers = deliveryOffers;
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
        //Bind data to the View Holder
        holder.mRequestKey=mRequestKeys.get(position);
        holder.mOfferKey=mOfferKeys.get(position);
        holder.mDeliveryRequest = mRequests.get(position);
        holder.mDeliveryOffer = mDeliveryOffers.get(position);
        holder.mRestaurantAddressCityView.setText(mRequests.get(position).getRestaurantAddressCity());
        holder.mDeliveryAddressCityView.setText(mRequests.get(position).getDeliveryAddressCity());

        if(mOfferKeys.get(position) != "") {
            holder.mOfferPriceView.setText(mDeliveryOffers.get(position).getOfferPrice().toString());
            holder.mOfferStatusView.setText(mDeliveryOffers.get(position).getOfferStatus());
        }
        else{
            holder.mOfferPriceView.setText("");
            holder.mOfferStatusView.setText("");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if(holder.mOfferKey == ""){
                        mListener.onViewDeliveryRequestFragmentInteraction(holder.mRequestKey, holder.mDeliveryRequest,holder.mOfferKey,null);
                    }
                    else{
                        mListener.onViewDeliveryRequestFragmentInteraction(holder.mRequestKey, holder.mDeliveryRequest, holder.mOfferKey,holder.mDeliveryOffer);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRestaurantAddressCityView;       //TextView to store the restaurant address city
        public final TextView mDeliveryAddressCityView;         //TextView to store delivery address City
        public final TextView mOfferPriceView;                  //TextView to store the offer price from driver
        public final TextView mOfferStatusView;                 //TextView to store the offer status
        public DeliveryRequest mDeliveryRequest;                //Delivery request
        public DeliveryOffer mDeliveryOffer;                    //Delivery offer
        public String mRequestKey;                              //Delivery request key
        public String mOfferKey;                                //delivery offer key

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //setup UI references
            mRestaurantAddressCityView = (TextView) view.findViewById(R.id.restaurant_address_city);
            mDeliveryAddressCityView = (TextView) view.findViewById(R.id.delivery_address_city);
            mOfferPriceView = (TextView) view.findViewById(R.id.offer_price);
            mOfferStatusView = (TextView) view.findViewById(R.id.offer_status);
        }
    }
}
