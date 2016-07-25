package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */

import android.app.Dialog;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.webkit.WebView;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.view.Display;


public class InstagramLoginDialog  extends Dialog{

    public static float[] DIALOG_DIMENSIONS_LANDSCAPE = {460, 260};
    public static final float[] DIALOG_DIMENSIONS_PORTRAIT = {280, 420};
    public static FrameLayout.LayoutParams MATCH_PARENT = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static final int dialog_margins = 2;
    public static final int dialog_padding = 2;
    private LinearLayout  dialogContent;
    private WebView dialogWebView;

    private String authorizationToken;
    private String authenticationUrl;
    private TextView dialogTitle;



    public Helper.PostAsyncTaskCallback mOAuthDialogListener;
    private  ProgressDialog progrssSpinner;

    public InstagramLoginDialog(Context context, String url, Helper.PostAsyncTaskCallback listener)
    {
        super(context);
        this.authenticationUrl = url;
        this.mOAuthDialogListener =  listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(PuzzleActivity.TAG, "Created the instagram dialog");
        progrssSpinner  = new ProgressDialog(this.getContext());
        progrssSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progrssSpinner.setMessage("Loading. Please wait...");
        dialogContent  = new LinearLayout(this.getContext());
        dialogContent.setOrientation(LinearLayout.VERTICAL);
        this.setUpDialogTitle();
        this.setUpDialogWebView();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        final float scalingFactor = this.getContext().getResources().getDisplayMetrics().density;
        float[]  dialog_dimensions = (display.getWidth() < display.getHeight()) ? DIALOG_DIMENSIONS_PORTRAIT
                                        : DIALOG_DIMENSIONS_LANDSCAPE;
        addContentView(this.dialogContent, new FrameLayout.LayoutParams(
                (int)(dialog_dimensions[0]  * scalingFactor),
                (int)(dialog_dimensions[1] * scalingFactor)
        ));
    }




    private void setUpDialogTitle()
    {
        dialogTitle =  new TextView(this.getContext());
        dialogTitle.setText("Instagram");
        dialogTitle.setTextColor(Color.WHITE);
        dialogTitle.setTypeface(Typeface.DEFAULT_BOLD);
        dialogTitle.setBackgroundColor(Color.BLACK);
        dialogTitle.setPadding(dialog_margins + dialog_padding, dialog_padding  + dialog_margins,
                                dialog_margins, dialog_margins);

        dialogContent.addView(dialogTitle);
    }

    private void setUpDialogWebView()
    {
        this.dialogWebView = new WebView(this.getContext());
        dialogWebView.setHorizontalScrollBarEnabled(false);
        dialogWebView.setVerticalScrollBarEnabled(false);
        dialogWebView.setWebViewClient(new AuthWebViewClient());
        dialogWebView.getSettings().setJavaScriptEnabled(true);
        dialogWebView.loadUrl(authenticationUrl);
        dialogWebView.setLayoutParams(MATCH_PARENT);
        dialogContent.addView(dialogWebView);
    }

    private class AuthWebViewClient extends WebViewClient
    {
        // Allow the host  to deal with the new url that is being loaded.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            Log.d(PuzzleActivity.TAG, "Receive a redirect to = " + url);

            if(url.startsWith(InstagramLoginDialog.this.getContext().getString(R.string.INSTAGRAM_REDIRECT_URL)))
            {
                Log.d(PuzzleActivity.TAG, "Got the result with URL = " + url);
                String[] urls = url.split("=");
                authorizationToken = urls[1];
                Log.d(PuzzleActivity.TAG, "The token = " + authorizationToken);
                mOAuthDialogListener.onComplete(new String[]{authorizationToken});
                InstagramLoginDialog.this.dismiss();;
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView  webView, String url, Bitmap icon)
        {
            Log.d(PuzzleActivity.TAG, "Loading URL = " + url);
            progrssSpinner.show();
        }


        @Override
        public void onReceivedError(WebView webView, WebResourceRequest request, WebResourceError error)
        {
            Log.d(PuzzleActivity.TAG, "There was an error loading");
            mOAuthDialogListener.onError(error.getDescription().toString());
            progrssSpinner.dismiss();
        }

        @Override
        public void onPageFinished(WebView webView, String url)
        {
            Log.d(PuzzleActivity.TAG, "Finished loading the url = " + url);
            progrssSpinner.dismiss();

        }


    }

}
