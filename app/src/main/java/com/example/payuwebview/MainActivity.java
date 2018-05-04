package com.example.payuwebview;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//private Button button;
	
	private static final String TAG = "MainActivity";
	WebView webviewPayment;
	WebView mwebview;
	TextView  txtview;
	/*
	protected  void writeStatus(String str){
		txtview.setText(str);
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//button=(Button)findViewById(R.id.button1);
		
		
		
		webviewPayment = (WebView) findViewById(R.id.webView1);
		webviewPayment.getSettings().setJavaScriptEnabled(true);
		webviewPayment.getSettings().setDomStorageEnabled(true);
		webviewPayment.getSettings().setLoadWithOverviewMode(true);
		webviewPayment.getSettings().setUseWideViewPort(true);
		webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webviewPayment.getSettings().setSupportMultipleWindows(true);
		webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
		//webviewPayment.loadUrl("http://www.google.com");
		/*webviewPayment
				.loadUrl("128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=10");*/

		
	//	webviewPayment.loadUrl("http://timesofindia.com/");
		StringBuilder url_s = new StringBuilder();
		//http://merirakhi.com/processor/payment/endpoint?order_id=aAbBcC&amount=10&currency=USD
		url_s.append("https://test.payu.in/_payment");
		
		Log.e(TAG, "call url " + url_s);
		
		//webviewPayment.loadUrl(url_s.toString());
		//String postData = "username=my_username&password=my_password";
		webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(getPostString(), "utf-8"));
		
	//	webviewPayment.loadUrl("http://128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=0.10&currency=USD");
		
		webviewPayment.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@SuppressWarnings("unused")
			public void onReceivedSslError(WebView view) {
				Log.e("Error", "Exception caught!");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
	}
	
	private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        
        public void success( long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {

                	Toast.makeText(MainActivity.this, "Status is txn is success "+" payment id is "+paymentId, 8000).show();
                	//String str="Status is txn is success "+" payment id is "+paymentId;
                  // new MainActivity().writeStatus(str);
                	
                	TextView  txtview;
                	txtview = (TextView) findViewById(R.id.textView1);
                	txtview.setText("Status is txn is success "+" payment id is "+paymentId);
                	
                }
            });
        }
   
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String getPostString()
	{
		String key  = "fB7m8s";
		String salt  = "eRis5Chv";
		String txnid = "TXN_1";
		String amount = "1";
		String firstname = "Monica";
		String email = "goel.monica1@gmail.com";
		String productInfo = "Product1"; 
		
		StringBuilder post = new StringBuilder();
		post.append("key=");
		post.append(key);
		post.append("&");
		post.append("txnid=");
		post.append(txnid);
		post.append("&");
		post.append("amount=");
		post.append(amount);
		post.append("&");
		post.append("productinfo=");
		post.append(productInfo);
		post.append("&");
		post.append("firstname=");
		post.append(firstname);
		post.append("&");
		post.append("email=");
		post.append(email);
		post.append("&");
		post.append("phone=");
		post.append("8904896130");
		post.append("&");
		post.append("surl=");
		post.append("https://payu.herokuapp.com/success");
		//https://www.payumoney.com/mobileapp/payumoney/success.php
		//https://www.payumoney.com/mobileapp/payumoney/failure.php
		post.append("&");
		post.append("furl=");
		post.append("https://payu.herokuapp.com/failure");
		post.append("&");
	
		StringBuilder checkSumStr = new StringBuilder();
		/* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
	    MessageDigest digest=null;
	    String hash;
	    try {
	        digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");
	        
	        checkSumStr.append(key);
	        checkSumStr.append("|");
	        checkSumStr.append(txnid);
	        checkSumStr.append("|");
	        checkSumStr.append(amount);
	        checkSumStr.append("|");
	        checkSumStr.append(productInfo);
	        checkSumStr.append("|");
	        checkSumStr.append(firstname);
	        checkSumStr.append("|");
	        checkSumStr.append(email);
	        checkSumStr.append("|||||||||||");
	        checkSumStr.append(salt);
	        
	        digest.update(checkSumStr.toString().getBytes());

	        hash = bytesToHexString(digest.digest());
	    	post.append("hash=");
	        post.append(hash);
	        post.append("&");
	        Log.i(TAG, "SHA result is " + hash);
	    } catch (NoSuchAlgorithmException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
		post.append("service_provider=");
		post.append("payu_paisa");
		return post.toString();	
	}
	
	private JSONObject getProductInfo()
	{
		try {
			//create payment part object
			JSONObject productInfo = new JSONObject();
			
			JSONObject jsonPaymentPart = new JSONObject();
			jsonPaymentPart.put("name", "TapFood");
			jsonPaymentPart.put("description", "Lunchcombo");
			jsonPaymentPart.put("value", "500");
			jsonPaymentPart.put("isRequired", "true");
			jsonPaymentPart.put("settlementEvent", "EmailConfirmation");
			
			//create payment part array
			JSONArray jsonPaymentPartsArr = new JSONArray();
			jsonPaymentPartsArr.put(jsonPaymentPart);
			
			//paymentIdentifiers
			JSONObject jsonPaymentIdent = new JSONObject();
			jsonPaymentIdent.put("field", "CompletionDate");
			jsonPaymentIdent.put("value", "31/10/2012");
			
			//create payment part array
			JSONArray jsonPaymentIdentArr = new JSONArray();
			jsonPaymentIdentArr.put(jsonPaymentIdent);
			
			productInfo.put("paymentParts", jsonPaymentPartsArr);
			productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);
			
			Log.e(TAG, "product Info = " + productInfo.toString());
			return productInfo;
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
	
	
	
	
}
