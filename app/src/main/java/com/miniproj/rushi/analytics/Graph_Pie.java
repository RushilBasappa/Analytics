package com.miniproj.rushi.analytics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class Graph_Pie extends Activity {
    private static int[] COLORS = new int[]
            {Color.rgb(148, 178, 254),
                    Color.rgb(253, 205, 156),
                    Color.rgb(96, 223, 229),
                    Color.rgb(105, 255, 105),
                    Color.rgb(230, 140, 128),
                    Color.rgb(102, 255, 153),
                    Color.rgb(201, 160, 220)};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private Button mAdd;
    private EditText mValue;
    private GraphicalView mChartView;
    int pievalue = 0;
    String title;
    EditText etText;
    Global gd = new Global();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.piegraph_xml);
            gd.ColorCount = 0;
            if (mChartView == null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.pLLayout);
                mChartView = ChartFactory.getPieChartView(Graph_Pie.this, mSeries, mRenderer);
                mRenderer.setClickEnabled(true);
                mRenderer.setStartAngle(180);
                mRenderer.setDisplayValues(true);
                mRenderer.setPanEnabled(false);
                mRenderer.setLegendTextSize(20);
                mRenderer.setLabelsTextSize(20);
                mRenderer.setLabelsColor(Color.BLACK);
                mRenderer.setFitLegend(true);
                mChartView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                        if (seriesSelection != null) {
                            Toast.makeText(
                                Graph_Pie.this,
                                "Character element in series " + seriesSelection.getSeriesIndex() +
                                        " data point index " + seriesSelection.getPointIndex() +
                                        " x axis = " + seriesSelection.getValue(),
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
            }

            declarepoints();
            mValue = (EditText) findViewById(R.id.pXVal);
            mAdd = (Button) findViewById(R.id.pAddData);
            mAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        pievalue = Integer.parseInt(mValue.getText().toString());
                    } catch (NumberFormatException e) {
                        mValue.requestFocus();
                        return;
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Graph_Pie.this);
                    View view = getLayoutInflater().inflate(R.layout.custom, null);
                    view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    dialog.setView(view);

                    etText = (EditText) view.findViewById(R.id.etSeriesName);
                    dialog
                            .setTitle("Series Name")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    title = etText.getText().toString();
                                    setting(title, pievalue);
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                    renderer.setColor(COLORS[++gd.ColorCount]);
                    mRenderer.addSeriesRenderer(renderer);
                    mValue.setText("");
                    mValue.requestFocus();
                }
            });
        }
    }

    public void setting(String title2, Integer pieVal) {
        DatabaseOperations db = new DatabaseOperations(Graph_Pie.this);
        db.putPiePoints(db, title2, pieVal);
        mSeries.add(title2, pieVal);
    }

    private void declarepoints() {

        DatabaseOperations dop2 = new DatabaseOperations(Graph_Pie.this);
        Cursor CR2 = dop2.getPiePoints(dop2);

        if (CR2.moveToFirst()) {
            do {
                String SerName = CR2.getString(0);
                int val = Integer.parseInt(CR2.getString(1));
                mSeries.add(SerName, val);
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(COLORS[++gd.ColorCount]);
                mRenderer.addSeriesRenderer(renderer);
            } while (CR2.moveToNext());
        }
    }
}
