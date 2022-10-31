package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class CustomerViewVehicleDetails extends AppCompatActivity implements httpDataManager {

    private TextView txtCusVehReg,txtCusVehBrand,txtCusVehModal,txtCusVehEngine,txtCusVehChassis;
    private ImageView imageView;
    private Vehicle vehicle;
    private PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_vehicle_details);
        pieChartView = findViewById(R.id.chart);
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
        getRemainingQuota();
    }

    private void getRemainingQuota() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "remaining_quota");
        param.put("vid", String.valueOf(vehicle.getVid()));

        Backgroundworker backgroundworker = new Backgroundworker(CustomerViewVehicleDetails.this);
        backgroundworker.execute(param);
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

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            Log.i("Error",retrievedData.get());
            try {
                JSONObject jsonObj = new JSONObject(retrievedData.get());
                int allowed_quota = jsonObj.getInt("allowed_quota");
                int total_amount = jsonObj.getInt("total_amount");
                int usedPercentage = total_amount*100/allowed_quota;
                List pieData = new ArrayList<SliceValue>();
                pieData.add(new SliceValue(usedPercentage, Color.rgb(255,8,12)).setLabel("Used"));
                pieData.add(new SliceValue(100-usedPercentage, Color.rgb(0, 255, 148)).setLabel("Remaining"));
                view_piechart(pieData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void view_piechart(List pieData){
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setHasLabelsOutside(true).setValueLabelsTextColor(Color.BLACK);
        pieChartData.setHasCenterCircle(true)
                .setCenterText1("hhhh")
                .setCenterText1FontSize(15)
                .setCenterText1Color(Color.parseColor("#212A51"));
        pieChartView.setPieChartData(pieChartData);

    }
}