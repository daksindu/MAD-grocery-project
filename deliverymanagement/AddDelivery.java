package com.example.deliverymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deliverymanagement.Models.DeliveryDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDelivery extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        editText1=(EditText)findViewById(R.id.id);
        editText2=(EditText)findViewById(R.id.name);
        editText3=(EditText)findViewById(R.id.price);
        editText4=(EditText)findViewById(R.id.uname);
        editText5=(EditText)findViewById(R.id.uaddress);
        editText6=(EditText)findViewById(R.id.ucontact);
        editText7=(EditText)findViewById(R.id.uquantity);
        button=(Button)findViewById(R.id.addnow);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("DeliveryDetails");

                String id = editText1.getText().toString();
                String name = editText2.getText().toString();
                String price = editText3.getText().toString();
                String uname = editText4.getText().toString();
                String uaddress = editText5.getText().toString();
                String ucontact = editText6.getText().toString();
                String uquantity = editText7.getText().toString();

                if (id.isEmpty()) {
                    editText1.setError("Product ID is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Product Name is required");
                }  else if (price.isEmpty()) {
                    editText3.setError("Price is required");
                } else if (uname.isEmpty()) {
                    editText4.setError("Name is required");
                } else if (uaddress.isEmpty()) {
                    editText5.setError("Address is required");
                } else if (ucontact.isEmpty()) {
                    editText6.setError("Contact is required");
                }
                else if (ucontact.length() > 10) {
                    editText6.setError("Valid contact is required");
                }else if (uquantity.isEmpty()) {
                    editText7.setError("Quantity number is required");
                }else {

                    Integer pricee = Integer.valueOf(editText3.getText().toString());
                    Integer qtyy = Integer.valueOf(editText7.getText().toString());

                    String total = String.valueOf(pricee * qtyy);

                    DeliveryDetails deliveryDetails = new DeliveryDetails(id,name,total,uname,uaddress,ucontact,uquantity);
                    reference.child(id).setValue(deliveryDetails);

                    Toast.makeText(AddDelivery.this, "Delivery added successfully", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}