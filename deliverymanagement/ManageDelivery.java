package com.example.deliverymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliverymanagement.Models.DeliveryDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageDelivery extends AppCompatActivity {

    FloatingActionButton button;
    ListView listView;
    private List<DeliveryDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_delivery);

        button = (FloatingActionButton)findViewById(R.id.addbtn);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageDelivery.this, AddDelivery.class);
                startActivity(intent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("DeliveryDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    DeliveryDetails deliveryDetails = taskDatasnap.getValue(DeliveryDetails.class);
                    user.add(deliveryDetails);
                }

                MyAdapter adapter = new MyAdapter(ManageDelivery.this, R.layout.custom_delivery_items, (ArrayList<DeliveryDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        ImageButton imageButton1;
        ImageButton imageButton2;

    }

    class MyAdapter extends ArrayAdapter<DeliveryDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<DeliveryDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<DeliveryDetails> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_delivery_items, null);

                holder.COL1 = (TextView) view.findViewById(R.id.productidd);
                holder.COL2 = (TextView) view.findViewById(R.id.productprice);
                holder.COL3 = (TextView) view.findViewById(R.id.productuname);
                holder.COL4 = (TextView) view.findViewById(R.id.productcontact);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.delete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.edit);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Product ID: -"+user.get(position).getId());
            holder.COL2.setText("Product Price: -"+user.get(position).getProductPrice());
            holder.COL3.setText("User Name: -"+user.get(position).getUserName());
            holder.COL4.setText("User Contact: -"+user.get(position).getUserContact());

            System.out.println(holder);

            String idd = user.get(position).getId();

            holder.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this Delivery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String userid = user.get(position).getId();

                                    FirebaseDatabase.getInstance().getReference("DeliveryDetails").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_delivery,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.updname);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.updprice);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.updcusname);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.updcusaddress);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.updcontact);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.upsqty);
                    final Button button = (Button)view1.findViewById(R.id.update);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DeliveryDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("productName").getValue();
                            String price = (String) snapshot.child("productPrice").getValue();
                            String uname = (String) snapshot.child("userName").getValue();
                            String uaddress = (String) snapshot.child("userAddress").getValue();
                            String ucontact = (String) snapshot.child("userContact").getValue();
                            String qty = (String) snapshot.child("qty").getValue();

                            editText1.setText(name);
                            editText2.setText(price);
                            editText3.setText(uname);
                            editText4.setText(uaddress);
                            editText5.setText(ucontact);
                            editText6.setText(qty);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = editText1.getText().toString();
                            String price = editText2.getText().toString();
                            String uname =editText3.getText().toString();
                            String uaddress = editText4.getText().toString();
                            String ucontact = editText5.getText().toString();
                            String qty = editText6.getText().toString();


                            if (name.isEmpty()) {
                                editText1.setError("Product Name is required");
                            }  else if (price.isEmpty()) {
                                editText2.setError("Price is required");
                            } else if (uname.isEmpty()) {
                                editText3.setError("Name is required");
                            } else if (uaddress.isEmpty()) {
                                editText4.setError("Address is required");
                            } else if (ucontact.isEmpty()) {
                                editText5.setError("Contact is required");
                            }
                            else if (ucontact.length() > 10) {
                                editText5.setError("Valid contact is required");
                            }else if (qty.isEmpty()) {
                                editText6.setError("Quantity number is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("productName",name);
                                map.put("productPrice",price);
                                map.put("userName",uname);
                                map.put("userAddress",uaddress);
                                map.put("userContact",ucontact);
                                map.put("qty",qty);
                                reference.updateChildren(map);

                                Toast.makeText(ManageDelivery.this, "Delivery updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;
        }

    }
}