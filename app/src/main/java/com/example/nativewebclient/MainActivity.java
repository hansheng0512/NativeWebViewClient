package com.example.nativewebclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nativewebclient.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private WebView webView;

    private ValueCallback<Uri[]> uploadMessage;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        // Enable JavaScript (optional, if you need it)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Enable LocalStorage and SessionStorage
        webSettings.setDomStorageEnabled(true);

        // Set a WebViewClient to open links within the WebView
        webView.setWebViewClient(new WebViewClient());

        // Create a WebChromeClient for file selection
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);

                return true;
            }
        });

        // Load "https://google.com"
        webView.loadUrl("https://www.redcash.com.my");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == FILE_CHOOSER_RESULT_CODE) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    uploadMessage = null;
                }
            }
        }
    }

    // Your WebViewClient
    private class MyWebViewClient extends WebViewClient {
        // Implement any WebViewClient methods as needed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}