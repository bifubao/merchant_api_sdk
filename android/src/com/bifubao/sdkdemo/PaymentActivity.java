package com.bifubao.sdkdemo;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bifubao.sdkdemo.helper.OrderHelper;
import com.dearcoin.wallet.sdk.api.app.APIFactory;
import com.dearcoin.wallet.sdk.api.app.Payment;

public class PaymentActivity extends BaseActivity implements OnClickListener {
    private String orderHashId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        findViewById(R.id.request_order).setOnClickListener(this);
        findViewById(R.id.pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_order:
                requestOrder();
                return;
            case R.id.pay:
                if (TextUtils.isEmpty(orderHashId)) {
                    Toast.makeText(this, "request order first",
                            Toast.LENGTH_SHORT).show();
                } else {
                    pay();
                }
                break;
        }
    }

    private void pay() {
        try {
            APIFactory f = APIFactory.getInstance(this);
            Payment pay = f.createPayment();
            pay.setOrderHashId(orderHashId);
            // 当手机上没有安装币付宝或者安装的币付宝不支持sdk时是否使用浏览器操作
            // pay.setCallBrowserIfAppUnavaliable(true);
            // 自定义币付宝安装包各种状况的行为
            // pay.setPackageNotFoundHandler(new PackageErrorHandler() {
            //
            // @Override
            // public void onPackageNotFound() {
            // // TODO Auto-generated method stub
            //
            // }
            //
            // @Override
            // public void onPackageNeedUpgrade() {
            // // TODO Auto-generated method stub
            //
            // }
            //
            // @Override
            // public void onPackageInvalid() {
            // // TODO Auto-generated method stub
            //
            // }
            // });
            pay.call(this, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 强烈建议在Server端运行以下代码，以保证pid和key的安全
     */
    private void requestOrder() {
        // 登录www.bifubao.com，在商家集成/支付API页面申请商家编号pid和密钥key
        String pid = "55696553157";
        String key = "8f62a7778a5c934fdcea0af40c53f093";
        String requestOrderUrl = "https://api.bifubao.com/v00002/order/createexternal/";
        String postBody = null;
        try {
            OrderHelper helper = new OrderHelper(pid, key);
            // 必填，商家系统提供的order_id，不能重复
            helper.setOrderId(String.valueOf(System.currentTimeMillis()));
            helper.setPriceBtc("0.01");
            // 订单以人民币定价的价格，单位是元，支付时会根据实时汇率折算成比特币支付,price_btc和price_cny二者只能选择其一
            // api.setPriceCny("1");
            // 必填，订单标题
            helper.setDisplayName("Ukulele");
            // 可选，订单描述
            helper.setDisplayDesc("Sing a song");
            // 可选，订单额外信息（不会显示在支付页面内，用于商户向订单添加自定义信息）
            helper.setExternalInfo("ukulele");
            // 可选，订单完成后通知商户处理业务逻辑的URL，必须在线可访问
            helper.setExternalCallbackUrl("http://www.your-callback.com");
            postBody = helper.buildPostParameter();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final ProgressDialog dialog = ProgressDialog.show(this, null, null);
        final String body = postBody;
        Listener<String> listener = new Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                TextView info = (TextView) findViewById(R.id.info);
                info.setText(arg0);
                dialog.dismiss();
                L.i("onResponse ", arg0);
                try {
                    JSONObject json = new JSONObject(arg0);
                    int code = json.optInt("error_no", -1);
                    if (code != 0) {
                        L.e(json.optString("error_msg"));
                        return;
                    }
                    JSONObject data = json.getJSONObject("result");
                    orderHashId = data.optString("order_hash_id");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        ErrorListener errorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                L.e("onErrorResponse ", arg0);
                dialog.dismiss();
            }
        };
        StringRequest req = new StringRequest(Method.POST, requestOrderUrl,
                listener, errorListener) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.getBytes();
            }

        };
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }
}
