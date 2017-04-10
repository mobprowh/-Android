package com.example.andre.qrcodeapp2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static android.R.attr.format;
import static android.R.id.content;

public class MainActivity extends AppCompatActivity {

    private View btnScan;
    private TextView content;
    private TextView format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (View) findViewById(R.id.btn_scan);
        content = (TextView) findViewById(R.id.content);
        format = (TextView) findViewById(R.id.format);

        btnScan.setOnClickListener(onClickListener());
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();

                    //if you haven't install barcodeScanner app, download it from Google Play
                    downloadScanBarcode();
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                format.setText(data.getStringExtra("SCAN_RESULT_FORMAT"));
                content.setText(data.getStringExtra("SCAN_RESULT"));
            } else if (resultCode == RESULT_CANCELED) {
                format.setText("Press a button to start a scan.");
                content.setText("Scan cancelled.");
            }
        }
    }

    /**
            * Go to GooglePlay Store and down load "ScanBarCode" app
    */
    private void downloadScanBarcode() {
        Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
