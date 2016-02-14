package com.miniproj.rushi.analytics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
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
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

public class Graph_Bar extends Activity {
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries, series;
    private XYSeriesRenderer mCurrentRenderer;
    private Button mNewSeries;
    private Button mAdd;
    private EditText mX;
    private EditText mY, etText;
    private GraphicalView mChartView;
    String title;
    int temp1, temp2;
    Global gd = new Global();
    double x, y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bargraph_xml);
            mX = (EditText) findViewById(R.id.bXVal);
            mY = (EditText) findViewById(R.id.bYVal);
            mAdd = (Button) findViewById(R.id.bAddData);
            mNewSeries = (Button) findViewById(R.id.bNSeries);

            if (mChartView == null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.bLLayout);
                mChartView = ChartFactory.getBarChartView(
                        Graph_Bar.this,
                        mDataset, mRenderer,
                        BarChart.Type.DEFAULT
                );
                mRenderer.setApplyBackgroundColor(true);
                mRenderer.setChartTitle("Graph");
                mRenderer.setLabelsTextSize(10);
                mRenderer.setXLabelsColor(Color.BLACK);
                mRenderer.setYLabelsColor(0, Color.BLACK);
                mRenderer.setYLabelsPadding(0);
                mRenderer.setLabelsColor(Color.RED);
                mRenderer.setBackgroundColor(Color.LTGRAY);
                mRenderer.setGridColor(Color.GRAY);
                mRenderer.setMarginsColor(Color.WHITE);
                mRenderer.setAxisTitleTextSize(20);
                mRenderer.setChartTitleTextSize(20);
                mRenderer.setShowGrid(true);
                mRenderer.setGridColor(Color.GRAY);
               /* mRenderer.setZoomLimits(new double[]{0, 1000, 0, 1000});
                mRenderer.setPanLimits(new double[]{0, 1000, 0, 1000});*/
                mRenderer.setFitLegend(true);
                mRenderer.setDisplayValues(true);
                mRenderer.setBarWidth(50);
                layout.addView(mChartView, new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT
                ));
            }

            declarepoints();

            mNewSeries.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Graph_Bar.this);
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
                                    setting(title);
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();

                }
            });

            mAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DatabaseOperations db = new DatabaseOperations(Graph_Bar.this);
                    try {
                        x = Double.parseDouble(mX.getText().toString());
                        y = Double.parseDouble(mY.getText().toString());
                        db.putBarPoints(db, x, y);
                    } catch (Exception e) {
                        Toast.makeText(Graph_Bar.this,
                                "Enter the values of x and y",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    try {
                        Cursor C2 = db.getBarCount(db);
                        C2.moveToLast();
                        temp2 = C2.getInt(C2.getColumnIndex(TableInfo.Bar_KEY_ID));
                        temp1 = C2.getInt(C2.getColumnIndex(TableInfo.Bar_PointsPerSeries));
                        temp1++;
                        SQLiteDatabase SQ = db.getWritableDatabase();
                        ContentValues CV = new ContentValues();
                        CV.put(TableInfo.Bar_PointsPerSeries, temp1);
                        SQ.update(TableInfo.Bar_Count_Table, CV, TableInfo.Bar_KEY_ID + " = " + temp2, null);
                        mCurrentSeries.add(x, y);
                        mX.setText("");
                        mY.setText("");
                        mX.requestFocus();
                        mChartView.repaint();
                    } catch (CursorIndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(),"NEW SERIES",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void setting(String title2) {
        series = new XYSeries(title2);

        DatabaseOperations db = new DatabaseOperations(Graph_Bar.this);
        db.putBarCount(db, title, 0);
        gd.barCounter = 0;
        mCurrentSeries = series;
        mDataset.addSeries(mCurrentSeries);
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        Random r = new Random();
        int color = Color.argb(100, r.nextInt(255), r.nextInt(255), r.nextInt(255));
        renderer.setColor(color);
        renderer.setDisplayChartValues(true);
        mCurrentRenderer = renderer;
        setSeriesWidgetsEnabled(true);
    }

    private void declarepoints() {
        DatabaseOperations dop2 = new DatabaseOperations(Graph_Bar.this);
        Cursor CR2 = dop2.getBarCount(dop2);
        gd.totalPoints = 0;
        if (CR2.moveToFirst()) {
            do {
                String SerName = CR2.getString(1);
                int val = Integer.parseInt(CR2.getString(2));
                series = new XYSeries(SerName);
                mCurrentSeries = series;
                mDataset.addSeries(mCurrentSeries);
                XYSeriesRenderer renderer = new XYSeriesRenderer();
                mRenderer.addSeriesRenderer(renderer);
                Random random = new Random();
                int color = Color.argb(150, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                renderer.setColor(color);
                renderer.setDisplayChartValues(true);
                renderer.setPointStrokeWidth(15);
                mCurrentRenderer = renderer;
                for (int i = 0; i < val; i++) {
                    DatabaseOperations dop = new DatabaseOperations(Graph_Bar.this);
                    Cursor CR = dop.getBarPoints(dop);
                    if (CR.moveToPosition(gd.totalPoints)) {
                        try {
                            x = Double.parseDouble(CR.getString(0));
                            y = Double.parseDouble(CR.getString(1));
                            mCurrentSeries.add(x, y);
                            gd.totalPoints++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    CR.close();
                }
            } while (CR2.moveToNext());
        }
        CR2.close();
    }

    private void setSeriesWidgetsEnabled(boolean enabled) {
        mX.setEnabled(enabled);
        mY.setEnabled(enabled);
        mAdd.setEnabled(enabled);
    }
}