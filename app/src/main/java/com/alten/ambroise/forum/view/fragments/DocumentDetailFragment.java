package com.alten.ambroise.forum.view.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Document;
import com.alten.ambroise.forum.view.activity.DocumentDetailActivity;
import com.alten.ambroise.forum.view.activity.DocumentListActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A fragment representing a single document detail screen.
 * This fragment is either contained in a {@link DocumentListActivity}
 * in two-pane mode (on tablets) or a {@link DocumentDetailActivity}
 * on handsets.
 */
public class DocumentDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    /**
     * The dummy title this fragment is presenting.
     */
    private Document mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DocumentDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy title specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load title from a title provider.
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.document_detail, container, false);

        // Show the dummy title as text in a TextView.
        if (mItem != null) {
            PDFView pdfView = rootView.findViewById(R.id.pdfv);
            WebView webview = rootView.findViewById(R.id.document_detail);
            if (mItem.getUri().substring(mItem.getUri().lastIndexOf(".") + 1).equals("pdf")) {
                webview.setVisibility(View.GONE);
                pdfView.fromAsset("documents/" + mItem.getTitle()).load();
            } else {
                pdfView.setVisibility(View.GONE);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setSupportZoom(true);
                webview.loadUrl(mItem.uri);
                webview.getSettings().setBuiltInZoomControls(true);
                webview.requestFocus();

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(true);
                progressDialog.show();

                webview.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                webview.setWebChromeClient(new WebChromeClient());
            }
        }
        return rootView;
    }
}
