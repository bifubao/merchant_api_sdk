package com.bifubao.sdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dearcoin.wallet.sdk.api.common.Utils;
import com.dearcoin.wallet.sdk.exception.InvalidSignatureException;
import com.dearcoin.wallet.sdk.exception.PackageVersionUnmathException;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.redemption).setOnClickListener(this);
        findViewById(R.id.payment).setOnClickListener(this);
        TextView t = (TextView) findViewById(R.id.info);
        try {
            Utils.testPackage(this);
        } catch (NameNotFoundException e) {
            t.setText(R.string.bifubao_package_not_installed_hint);
            e.printStackTrace();
        } catch (InvalidSignatureException e) {
            t.setText(R.string.bifubao_package_error_hint);
            e.printStackTrace();
        } catch (PackageVersionUnmathException e) {
            t.setText(R.string.bifubao_package_unavaliable_hint);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.redemption:
                startActivity(new Intent(this, RedemptionActivity.class));
                break;
            case R.id.payment:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
        }
    }

}
