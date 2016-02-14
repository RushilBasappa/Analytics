package com.miniproj.rushi.analytics;

import android.provider.BaseColumns;

/**
 * Created by Rushil on 28-Jun-15.
 */
public class TableInfo implements BaseColumns {
    static final String Database_name = "Analytics";

    static final String Line_KEY_ID = "LRowId";
    static final String Bar_KEY_ID = "BRowId";

    static final String Line_data_Table = "LineData";
    static final String Line_xaxis = "Date";
    static final String Line_yaxis = "Value";

    static final String Line_Count_Table = "LineCount";
    static final String Line_PointsPerSeries = "LineCountPerSer";
    static final String Line_ser_name = "LineseriesNames";

    static final String Bar_data_Table = "BarData";
    static final String Bar_xaxis = "X_Value";
    static final String Bar_yaxis = "Y_Value";

    static final String Bar_Count_Table = "BarCount";
    static final String Bar_PointsPerSeries = "BarCountPerSer";
    static final String Bar_ser_name = "BarSeriesNames";

    static final String Pie_table = "Pie";
    static final String Pie_ser_name = "PieSerName";
    static final String Pie_ser_value = "PieSerValue";


}