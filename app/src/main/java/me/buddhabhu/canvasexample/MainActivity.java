package me.buddhabhu.canvasexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No XML file; just one custom view created programmatically.
        MyCanvasView myCanvasView;
        myCanvasView = new MyCanvasView(this);

        // Request the full available screen for layout.
        myCanvasView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(myCanvasView);
    }
}