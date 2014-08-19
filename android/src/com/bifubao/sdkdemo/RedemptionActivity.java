package com.bifubao.sdkdemo;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dearcoin.wallet.sdk.api.app.APIFactory;
import com.dearcoin.wallet.sdk.api.app.Redemption;

/**
 * 兑换币券
 * 
 * @author wanghb
 */
public class RedemptionActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redemption);
        findViewById(R.id.by_sdk).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.by_sdk:
                redeemBySdk();
                break;
        }
    }

    private void redeemBySdk() {
        try {
            APIFactory f = APIFactory.getInstance(this);
            Redemption r = f.createRedemption();
            TextView codeText = (TextView) findViewById(R.id.code);
            String code = codeText.getText().toString();
            r.addRedemptionCode(code);
            r.call(this, 0);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
