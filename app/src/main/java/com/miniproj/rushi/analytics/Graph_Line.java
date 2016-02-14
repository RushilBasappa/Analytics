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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Rushi on 7/8/2015.
 */
public class Graph_Line extends Activity {
    DatabaseOperations db = new DatabaseOperations(Graph_Line.this);
    EditText xValue, yValue;
    Button mAdd, nSer;
    XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    TimeSeries series, currentseries;
    XYSeriesRenderer currentRenderer, renderer;
    CheckBox cb;
    GraphicalView mChart;
    String title = "";
    EditText etText;
    Date minDate, maxDate;
    double min, max;
    double value, d;
    Date date;
    Global gd = new Global();
    int temp1, temp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linegraph_xml);
        xValue = (EditText) findViewById(R.id.lXVal);
        yValue = (EditText) findViewById(R.id.lYVal);
        mAdd = (Button) findViewById(R.id.lAddData);
        nSer = (Button) findViewById(R.id.lNSeries);
        initialize();
        declarepoints();

        nSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Graph_Line.this);
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
            @Override
            public void onClick(View v) {

                String a, b;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date dates = format.parse(xValue.getText().toString());
                    date = dates;
                    a = format.format(dates);
                    b = yValue.getText().toString();
                    d = Double.parseDouble(b);
                    db.putLinePoints(db, a, b);
                    updating();
                    currentseries.add(date, d);
                } catch (NullPointerException e) {
                    Toast.makeText(Graph_Line.this,
                            "Click on new series",
                            Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    Toast.makeText(Graph_Line.this,
                            "Enter the values of x and y",
                            Toast.LENGTH_SHORT).show();
                }

                xValue.setText("");
                yValue.setText("");
                xValue.requestFocus();
            }
        });
    }

    private void initialize() {
        if (mChart == null) {
            LinearLayout linear = (LinearLayout) findViewById(R.id.lLLayout);
            mChart = ChartFactory.getTimeChartView(Graph_Line.this, mDataset, mRenderer, "MM/dd/yyyy");
            mRenderer.setApplyBackgroundColor(true);
            mRenderer.setXTitle("X Values");
            mRenderer.setYTitle("Y Values");
            mRenderer.setChartTitle("Graph");
            mRenderer.setPointSize(15);
            mRenderer.setLabelsTextSize(10);
            mRenderer.setXLabelsColor(Color.BLACK);
            mRenderer.setYLabelsColor(0, Color.BLACK);
            mRenderer.setClickEnabled(true);
            mRenderer.setYLabelsPadding(0);
            mRenderer.setLabelsColor(Color.RED);
            mRenderer.setBackgroundColor(Color.LTGRAY);
            mRenderer.setShowGrid(true);
            mRenderer.setFitLegend(true);
            mRenderer.setGridColor(Color.GRAY);
            mRenderer.setMarginsColor(Color.WHITE);
            /*mRenderer.setPanLimits(new double[]{min, max, 0, 10000});
            mRenderer.setZoomLimits(new double[]{min, max, 0, 10000});*/
            SimpleDateFormat SDformat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                minDate = SDformat.parse("1-1-1000");
                maxDate = SDformat.parse("1-1-3000");

                min = Double.valueOf(minDate.getTime());
                max = Double.valueOf(maxDate.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }


            mChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection selec = mChart.getCurrentSeriesAndPoint();
                    if (selec != null) {
                        Toast.makeText(
                                Graph_Line.this,
                                "Character element in series " + selec.getSeriesIndex() +
                                        " data point index " + selec.getPointIndex() +
                                        " x axis = " + selec.getXValue() +
                                        " y axis = " + selec.getValue(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            linear.addView(
                    mChart,
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.FILL_PARENT
                    )
            );
            setWidgetsEnable(true);
        }
    }

    public void updating() {
        try {
            Cursor C2 = db.getLineCount(db);
            C2.moveToLast();
            temp2 = C2.getInt(C2.getColumnIndex(TableInfo.Line_KEY_ID));
            temp1 = C2.getInt(C2.getColumnIndex(TableInfo.Line_PointsPerSeries));
            temp1++;

            SQLiteDatabase SQ = db.getWritableDatabase();
            ContentValues CV = new ContentValues();
            CV.put(TableInfo.Line_PointsPerSeries, temp1);
            SQ.update(TableInfo.Line_Count_Table, CV, TableInfo.Line_KEY_ID + " = " + temp2, null);
        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "NEW SERIES", Toast.LENGTH_SHORT).show();
        }
    }

    private void declarepoints() {
        DatabaseOperations dop2 = new DatabaseOperations(Graph_Line.this);
        Cursor CR2 = dop2.getLineCount(dop2);
        gd.totalPoints = 0;
        if (CR2.moveToFirst()) {
            do {
                String SerName = CR2.getString(1);
                int val = Integer.parseInt(CR2.getString(2));
                series = new TimeSeries(SerName);
                currentseries = series;
                mDataset.addSeries(currentseries);
                renderer = new XYSeriesRenderer();
                mRenderer.addSeriesRenderer(renderer);
                Random random = new Random();
                int color = Color.argb(150, random.nextInt(255),
                        random.nextInt(255), random.nextInt(255));
                renderer.setFillPoints(true);
                renderer.setColor(color);
                renderer.setPointStyle(PointStyle.CIRCLE);
                renderer.setDisplayChartValues(true);
                renderer.setFillBelowLine(true);
                renderer.setFillBelowLineColor(color);
                renderer.setPointStrokeWidth(15);
                currentRenderer = renderer;
                for (int i = 0; i < val; i++) {
                    SimpleDateFormat SDformat = new SimpleDateFormat("dd-MM-yyyy");
                    DatabaseOperations dop = new DatabaseOperations(Graph_Line.this);
                    Cursor CR = dop.getLinePoints(dop);
                    if (CR.moveToPosition(gd.totalPoints)) {
                        try {
                            date = SDformat.parse(CR.getString(0));
                            value = Double.parseDouble(CR.getString(1));
                            currentseries.add(date, value);
                            gd.totalPoints++;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    CR.close();
                }
            } while (CR2.moveToNext());
        }
        CR2.close();
    }

    public void setting(String title) {
        series = new TimeSeries(title);

        DatabaseOperations db = new DatabaseOperations(Graph_Line.this);
        db.putLineCount(db, title, 0);
        gd.counter = 0;

        currentseries = series;
        mDataset.addSeries(currentseries);
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        int color = Color.RED;
        renderer.setFillPoints(true);
        renderer.setColor(color);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setDisplayChartValues(true);
        renderer.setFillBelowLine(true);
        renderer.setFillBelowLineColor(color);
        renderer.setPointStrokeWidth(15);
        currentRenderer = renderer;
        setWidgetsEnable(true);
    }

    private void setWidgetsEnable(boolean enabled) {
        xValue.setEnabled(enabled);
        yValue.setEnabled(enabled);
        mAdd.setEnabled(true);
    }

}
