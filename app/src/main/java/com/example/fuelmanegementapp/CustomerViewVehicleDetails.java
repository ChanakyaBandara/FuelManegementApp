package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fuelmanegementapp.models.Vehicle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CustomerViewVehicleDetails extends AppCompatActivity {

    private TextView txtCusVehReg,txtCusVehBrand,txtCusVehModal,txtCusVehEngine,txtCusVehChassis;
    ImageView imageView;
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_vehicle_details);

        imageView = (ImageView)findViewById(R.id.vehicleQrView);
        vehicle = (Vehicle) getIntent().getSerializableExtra("Extra_rec");

        txtCusVehReg = (TextView) findViewById(R.id.txtCusVehReg);
        txtCusVehBrand = (TextView) findViewById(R.id.txtCusVehBrand);
        txtCusVehModal = (TextView) findViewById(R.id.txtCusVehModal);
        txtCusVehEngine = (TextView) findViewById(R.id.txtCusVehEngine);
        txtCusVehChassis = (TextView) findViewById(R.id.txtCusVehChassis);

        txtCusVehReg.setText(vehicle.getReg_no());
        txtCusVehBrand.setText(vehicle.getBrand());
        txtCusVehModal.setText(vehicle.getModel());
        txtCusVehEngine.setText(vehicle.getEngine_no());
        txtCusVehChassis.setText(vehicle.getChassis_no());

        generateQRCode(vehicle.getQr());
    }

    private void generateQRCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}