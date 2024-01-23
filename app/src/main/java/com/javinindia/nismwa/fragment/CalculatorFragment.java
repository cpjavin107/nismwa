package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.utility.Utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by Ashish on 20-07-2017.
 */

public class CalculatorFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtSubmit, txtShape, txtThickness, txtThickness2, txtUnitThickness, txtUnitThickness2, txtThicknessValue, txtWidth, txtUnitWidth, txtLength, txtUnitLength, txtOutput, txtQuantityHead;
    AppCompatEditText etQuantity, etWidth, etLength, etThicknessValue2;
    ImageView imgBack;
    LinearLayout llThickness, llWidth, llLength, llThickness2;


    String plate[] = {"1", "2", "3", "4", "5", "6", "8", "10", "12", "14", "16", "18", "20", "22", "25", "32", "36", "40"};
    String chequeredPlate[] = {"3", "4", "5", "6", "7", "8", "10", "12"};
    String channels[] = {"75*40", "100*50", "125*65", "125*65(Sail)", "150*75(Sail)", "175*75(Sail)", "200*75(Sail)", "250*82(Sail)", "300*90(Sail)", "400*100(Sail)"};
    String round[] = {"6", "8", "10", "12", "14", "16", "18", "20", "22", "25", "28", "32", "36", "40", "42", "45", "50", "53", "56", "63", "71", "80", "90", "100", "110", "118", "125", "142", "156", "160", "168", "180", "190", "205"};
    String equalAngle[] = {"25*25*3", "35*35*3", "35*35*6", "40*40*5", "40*40*6", "45*45*4", "45*45*5", "45*45*6", "50*50*5", "50*50*6", "60*60*6", "60*60*8", "65*65*5", "65*65*6", "65*65*8", "65*65*10", "75*75*6", "75*75*8", "75*75*10", "80*80*6", "80*80*8", "80*80*12", "90*90*6", "90*90*8", "90*90*10", "90*90*12", "100*100*6(HSL)", "100*100*6", "100*100*8", "100*100*10(HSL)", "100*100*10", "110*110*8", "110*110*10", "130*130*10", "130*130*12", "150*150*10", "150*150*12", "150*150*16", "150*150*18", "150*150*20", "200*200*16", "200*200*20"};
    String breamsJoists[] = {"116*100", "125*70", "150*75", "150*150", "175*85(Sail)", "200*90", "200*100", "200*150", "200*200", "225*110", "250*125", "300*140", "350*140", "400*140", "450*150", "500*180", "600*220(Jindal)", "700*300(Jindal)", "600*210(Jindal)"};
    String tmtSteel[] = {"8", "10", "12", "16", "20", "25", "28", "32"};
    String flats[] = {"25*6", "25*10", "32*6", "32*10", "35*6", "35*10", "40*6", "40*8", "40*10", "40*12", "40*16", "40*20", "40*25", "50*6", "50*8", "50*10", "50*12", "50*16", "50*20", "65*8", "65*10", "65*12", "65*20", "65*25", "75*8", "75*10", "75*12", "75*20", "75*25", "75*32", "100*10", "100*12", "100*16", "100*20", "100*25", "125*10", "125*12", "125*16", "125*20", "125*25", "150*12", "150*16", "150*20", "150*25", "150*32", "150*40", "180*12", "180*16", "180*18", "180*20", "200*10", "200*12", "200*16", "200*20", "200*25", "220*20", "250*12", "250*20", "250*25", "300*12", "300*16", "300*20"};
    String squares[] = {"12", "16", "20", "25", "30", "32", "36", "40", "45", "50", "56", "63", "75", "100"};
    String rails[] = {"30", "40", "50", "60", "75", "90", "105", "120", "CR-80", "CR-100", "CR-120"};
    String pipe[] = {"1", "2", "3", "4", "5", "6", "8", "10", "12", "14", "16", "18", "20", "22", "25", "32", "36", "40"};
    String sheet[] = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32"};
    // String sheet[] = {"7/0", "6/0", "5/0", "4/0", "3/0", "2/0", "1/0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
    // String a[]        = { "4.064", "3.657", "3.251", "2.946", "2.641", "2.336", "2.032", "1.828", "1.625", "1.422", "1.219", "1.016", "0.914", "0.812", "0.711", "0.609", "0.558", "0.508", "0.457", "0.418", "0.375", "0.345", "0.314", "0.295", "0.274"};

    DecimalFormat decimalFormat = new DecimalFormat("####0.00");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void clickMethod(View view) {
        txtSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtShape.setOnClickListener(this);
        txtThicknessValue.setOnClickListener(this);
        txtUnitThickness.setOnClickListener(this);
        txtUnitWidth.setOnClickListener(this);
        txtUnitLength.setOnClickListener(this);
        txtUnitThickness.setOnClickListener(this);
        txtUnitThickness2.setOnClickListener(this);
    }

    private void initialize(View view) {
        llThickness2 =  view.findViewById(R.id.llThickness2);
        llThickness =  view.findViewById(R.id.llThickness);
        llWidth = view.findViewById(R.id.llWidth);
        llLength = view.findViewById(R.id.llLength);
        imgBack =  view.findViewById(R.id.imgBack);

        txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTitle.setText("Calculator");

        txtShape =  view.findViewById(R.id.txtShape);
        txtShape.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtThickness2 =  view.findViewById(R.id.txtThickness2);
        txtThickness2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUnitThickness2 =view.findViewById(R.id.txtUnitThickness2);
        txtUnitThickness2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etThicknessValue2 =  view.findViewById(R.id.etThicknessValue2);
        etThicknessValue2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtThickness =  view.findViewById(R.id.txtThickness);
        txtThickness.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUnitThickness =  view.findViewById(R.id.txtUnitThickness);
        txtUnitThickness.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtThicknessValue =  view.findViewById(R.id.txtThicknessValue);
        txtThicknessValue.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtWidth =  view.findViewById(R.id.txtWidth);
        txtWidth.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUnitWidth =  view.findViewById(R.id.txtUnitWidth);
        txtUnitWidth.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLength =  view.findViewById(R.id.txtLength);
        txtLength.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUnitLength = view.findViewById(R.id.txtUnitLength);
        txtUnitLength.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtQuantityHead =  view.findViewById(R.id.txtQuantityHead);
        txtQuantityHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etQuantity =  view.findViewById(R.id.etQuantity);
        etQuantity.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etWidth =  view.findViewById(R.id.etWidth);
        etWidth.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etLength = view.findViewById(R.id.etLength);
        etLength.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSubmit =  view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtOutput =  view.findViewById(R.id.txtOutput);
        txtOutput.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(activity);
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtSubmit:
                submit();
                break;
            case R.id.txtShape:
                methodShapePopup();
                break;
            case R.id.txtThicknessValue:
                if (!txtShape.getText().toString().equalsIgnoreCase("")) {
                    String shp = txtShape.getText().toString().trim();
                    if (shp.equalsIgnoreCase("PLATES")) {
                        methodThicknessPopup(plate, "PLATES");
                    } else if (shp.equalsIgnoreCase("CHEQUERED PLATE")) {
                        methodThicknessPopup(chequeredPlate, "CHEQUERED PLATE");
                    } else if (shp.equalsIgnoreCase("CHANNELS")) {
                        methodThicknessPopup(channels, "CHANNELS");
                    } else if (shp.equalsIgnoreCase("ROUNDS")) {
                        methodThicknessPopup(round, "ROUNDS");
                    } else if (shp.equalsIgnoreCase("ANGLE")) {
                        methodThicknessPopup(equalAngle, "ANGLE");
                    } else if (shp.equalsIgnoreCase("BREAMS & JOISTS")) {
                        methodThicknessPopup(breamsJoists, "BREAMS & JOISTS");
                    } else if (shp.equalsIgnoreCase("TMT STEEL")) {
                        methodThicknessPopup(tmtSteel, "TMT STEEL");
                    } else if (shp.equalsIgnoreCase("FLATS")) {
                        methodThicknessPopup(flats, "FLATS");
                    } else if (shp.equalsIgnoreCase("SQUARE")) {
                        methodThicknessPopup(squares, "SQUARE");
                    } else if (shp.equalsIgnoreCase("RAILS")) {
                        methodThicknessPopup(rails, "RAILS");
                    } else if (shp.equalsIgnoreCase("PIPE")) {
                        methodThicknessPopup(pipe, "PIPE");
                    } else if (shp.equalsIgnoreCase("SHEET")) {
                        methodThicknessPopup(sheet, "SHEET");
                    }
                } else {
                    showToastMethod("please select shape");
                }
                break;
            case R.id.txtUnitThickness2:
                methodCheckThicknesstype();
                break;
            case R.id.txtUnitThickness:
                methodCheckThicknesstype();
                break;
            case R.id.txtUnitWidth:
                if (!txtShape.getText().toString().equalsIgnoreCase("")) {
                    methodUnitPopup(txtShape.getText().toString(), "W");
                } else {
                    showToastMethod("please select shape");
                }
                break;
            case R.id.txtUnitLength:
                if (!txtShape.getText().toString().equalsIgnoreCase("")) {
                    methodUnitPopup(txtShape.getText().toString(), "L");
                } else {
                    showToastMethod("please select shape");
                }
                break;
            default:
                break;
        }
    }

    private void methodCheckThicknesstype() {
        if (txtShape.getText().toString().equalsIgnoreCase("SHEET")) {
            final String TypeArray[] = {"MILLIMETER", "GAUGE"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    txtUnitThickness2.setText(TypeArray[item].trim());
                    txtUnitThickness.setText(TypeArray[item].trim());
                    if (TypeArray[item].trim().equalsIgnoreCase("MILLIMETER")) {
                        llThickness.setVisibility(View.GONE);
                        llThickness2.setVisibility(View.VISIBLE);
                        txtThicknessValue.setText("");
                    } else {
                        llThickness2.setVisibility(View.GONE);
                        llThickness.setVisibility(View.VISIBLE);
                        etThicknessValue2.setText("");
                    }
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        } else {

        }
    }

    private void submit() {
        String shape = txtShape.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();
        String thick = txtThicknessValue.getText().toString().trim();
        String thickUnit = txtUnitThickness.getText().toString().trim();
        String width = etWidth.getText().toString().trim();
        String widthUnit = txtUnitWidth.getText().toString().trim();
        String length = etLength.getText().toString().trim();
        String lengthUnit = txtUnitLength.getText().toString().trim();

        String thick2 = etThicknessValue2.getText().toString().trim();
        String thickUnit2 = txtUnitThickness2.getText().toString().trim();

        if (!TextUtils.isEmpty(shape)) {
            if (!TextUtils.isEmpty(quantity)) {
                if (shape.equalsIgnoreCase("Plates")) {
                    if (validationPlates(thick, thickUnit, width, widthUnit, length, lengthUnit)) {
                        double wid = converter(Double.parseDouble(width), widthUnit);
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double quen = Double.parseDouble(quantity);
                        double th = Double.parseDouble(thick);
                        double output = wid * len * th * quen * 7.85;
                        String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Width</strong>&nbsp;:&nbsp;" + width + " " + widthUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("CHEQUERED PLATE")) {
                    if (validationPlates(thick, thickUnit, width, widthUnit, length, lengthUnit)) {
                        String a[] = {"22.8", "31.7", "42.4", "50.0", "61.10", "66.75", "84.60", "11.30"};
                        int index = Arrays.asList(chequeredPlate).indexOf(thick);
                        String value = a[index];
                        double wid = converter(Double.parseDouble(width), widthUnit);
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double quen = Double.parseDouble(quantity);
                        double th = Double.parseDouble(value);
                        double output = wid * len * th * quen;
                        String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Width</strong>&nbsp;:&nbsp;" + width + " " + widthUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("CHANNELS")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "thickness")) {
                        String a[] = {"7.1", "9.6", "13.1", "12.8", "17.0", "19.6", "22.3", "34.2", "36.3", "50.00"};
                        int index = Arrays.asList(channels).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Section</strong>&nbsp;:&nbsp;" + thick + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("ROUNDS")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "Diameter")) {
                        String a[] = {"0.2", "0.4", "0.6", "0.9", "1.2", "1.6", "2.0", "2.5", "3.0", "3.85", "4.83", "6.3", "8.0", "9.9", "10.88", "12.49", "15.41", "17.32", "19.34", "24.5", "31.08", "36.5", "49.94", "61.66", "74.60", "85.00", "96.34", "123.00", "148.00", "157.84", "172.00", "198.00", "220.00", "256.00"};
                        int index = Arrays.asList(round).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double quen = Double.parseDouble(quantity);
                        double th = Double.parseDouble(value);
                        double output = len * th * quen;
                        String result = "<font><strong>Diameter</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("ANGLE")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "Section")) {
                        String a[] = {"1.1", "2.6", "3.0", "3.0", "3.5", "2.7", "3.4", "4.0", "3.8", "4.5", "4.5", "5.4", "4.5", "5.8", "7.7", "9.4", "6.8", "8.9", "11.0", "7.3", "9.6", "14.0", "8.2", "10.8", "13.4", "15.8", "8.9", "9.2", "12.1", "12.9", "14.2", "13.4", "16.5", "19.7", "23.5", "22.8", "27.2", "35.5", "39.9", "44.12", "48.5", "60.0"};
                        int index = Arrays.asList(equalAngle).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Section</strong>&nbsp;:&nbsp;" + thick + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }

                } else if (shape.equalsIgnoreCase("BREAMS & JOISTS")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "Section")) {
                        String a[] = {"23.00", "13.20", "15.00", "34.30", "19.90", "25.2", "25.4", "52.09", "49.4", "31.2", "37.3", "46.1", "52.9", "61.6", "72.4", "87.7", "122.1", "149.0", "122.6"};
                        int index = Arrays.asList(breamsJoists).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Section</strong>&nbsp;:&nbsp;" + thick + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }

                } else if (shape.equalsIgnoreCase("TMT STEEL")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "thickness")) {
                        String a[] = {"0.395", "0.617", "0.888", "1.580", "2.470", "3.850", "4.830", "6.310"};
                        int index = Arrays.asList(tmtSteel).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double quen = Double.parseDouble(quantity);
                        double th = Double.parseDouble(value);
                        double output = len * th * quen;
                        String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("FLATS")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "Section")) {
                        String a[] = {"1.2", "2.0", "1.5", "2.5", "1.6", "2.8", "1.9", "2.5", "3.1", "3.8", "5.0", "6.33", "7.8", "2.4", "3.1", "3.9", "4.7", "6.3", "7.8", "4.1", "5.1", "6.1", "10.2", "12.8", "4.7", "5.9", "7.1", "11.8", "14.7", "18.8", "7.8", "9.4", "12.6", "15.7", "19.6", "9.8", "11.8", "15.7", "19.7", "24.6", "14.1", "18.8", "23.6", "29.4", "37.7", "42.7", "17.0", "22.6", "25.4", "28.3", "15.7", "18.8", "25.1", "31.4", "39.2", "34.5", "23.6", "39.2", "39.1", "28.3", "37.7", "47.1"};
                        int index = Arrays.asList(flats).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Section</strong>&nbsp;:&nbsp;" + thick + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }

                } else if (shape.equalsIgnoreCase("SQUARE")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "thickness")) {
                        String a[] = {"1.13", "2.01", "3.14", "4.91", "9.42", "10.05", "11.30", "12.56", "14.13", "19.62", "21.98", "31.16", "43.80", "78.00"};
                        int index = Arrays.asList(squares).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }

                } else if (shape.equalsIgnoreCase("RAILS")) {
                    if (validationChannel(thick, thickUnit, length, lengthUnit, "Section")) {
                        String a[] = {"14.88", "19.80", "24.80", "29.76", "37.13", "44.61", "52.09", "60.00", "64.20", "89.00", "118.00"};
                        int index = Arrays.asList(rails).indexOf(thick);
                        String value = a[index];
                        double len = converter(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(value);
                        double quen = Double.parseDouble(quantity);
                        double output = len * th * quen;
                        String result = "<font><strong>Section</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (shape.equalsIgnoreCase("PIPE")) {
                    if (validationPlates(thick, thickUnit, width, widthUnit, length, lengthUnit)) {
                        // String a[] = {"14.88", "19.80", "24.80", "29.76", "37.13", "44.61", "52.09", "60.00", "64.20", "89.00", "118.00"};
                        int index = Arrays.asList(rails).indexOf(thick);
                        // String value = a[index];
                        double wid = converterInMM(Double.parseDouble(width), widthUnit);
                        double len = converterInMM(Double.parseDouble(length), lengthUnit);
                        double th = Double.parseDouble(thick);
                        double quen = Double.parseDouble(quantity);
                        double output = (wid - th) * len * th * quen * 0.0075 * 3.3;
                        output = output / 1000;
                        // Log.e("output= ", output + "" + " wid=" + wid + " th=" + th + " len=" + len);
                        String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>OD</strong>&nbsp;:&nbsp;" + width + " " + widthUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";

                        // String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + thick + " " + thickUnit + "<br><strong>OD</strong>&nbsp;:&nbsp;" + wid + " " + widthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(output) + " KG</strong></font>";
                        dialogMethod(result, shape);
                    }
                } else if (!TextUtils.isEmpty(quantity)) {
                    if (shape.equalsIgnoreCase("SHEET")) {
                        double th = 0.0;
                        String newThick = "";
                        if (thickUnit.equalsIgnoreCase("GAUGE")) {
                            newThick = thick;
                        } else {
                            newThick = thick2;
                        }
                        if (validationPlates(newThick, thickUnit, width, widthUnit, length, lengthUnit)) {
                            if (thickUnit.equalsIgnoreCase("GAUGE")) {
                                String a[] = {"4.064", "3.657", "3.251", "2.946", "2.641", "2.336", "2.032", "1.828", "1.625", "1.422", "1.219", "1.016", "0.914", "0.812", "0.711", "0.609", "0.558", "0.508", "0.457", "0.418", "0.375", "0.345", "0.314", "0.295", "0.274"};
                                int index = Arrays.asList(sheet).indexOf(thick);
                                String value = a[index];
                                th = Double.parseDouble(value);
                                // th = Double.parseDouble(thick) * Double.parseDouble(thick) * 0.7853981633974;
                            } else {
                                th = Double.parseDouble(thick2);
                            }
                            double wid = converter(Double.parseDouble(width), widthUnit);
                            double len = converter(Double.parseDouble(length), lengthUnit);
                            double quen = Double.parseDouble(quantity);

                            double output = wid * len * th * quen * 7.85;
                            BigDecimal bd = new BigDecimal(output);
                            // long val = bd.longValue();
                            //  Log.e("result", bd + "");
                            String result = "<font><strong>Thickness</strong>&nbsp;:&nbsp;" + newThick + " " + thickUnit + "<br><strong>Width</strong>&nbsp;:&nbsp;" + width + " " + widthUnit + "<br><strong>Length</strong>&nbsp;:&nbsp;" + length + " " + lengthUnit + "<br><strong>Quantity</strong>&nbsp;:&nbsp;" + quantity + "<br><br><strong>TOTAL WEIGHT&nbsp;:&nbsp;" + decimalFormat.format(bd) + " KG</strong></font>";
                            dialogMethod(result, shape);
                        }
                    }
                }
            } else {
                showToastMethod("please enter quantity");
            }
        } else {
            showToastMethod("please select shape");
        }
    }

    private void dialogMethod(String thick, String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_popup_layout);

        AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.text_dialog);
        text.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        text.setText(Utility.fromHtml(thick));
        AppCompatTextView tit = (AppCompatTextView) dialog.findViewById(R.id.txtTit);
        tit.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        tit.setText(Utility.fromHtml(title));

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private boolean validationChannel(String thick, String thickUnit, String length, String lengthUnit, String type) {
        if (TextUtils.isEmpty(thick)) {
            showToastMethod("You have not entered " + type);
            return false;
        } else if (TextUtils.isEmpty(thickUnit)) {
            showToastMethod("please select thickness unit.");
            return false;
        } else if (TextUtils.isEmpty(length)) {
            showToastMethod("You have not entered length.");
            return false;
        } else if (TextUtils.isEmpty(lengthUnit)) {
            showToastMethod("please select length unit.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.calculator_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    //-----------------------------shape----------------------------//
    private void methodShapePopup() {
        final String TypeArray[] = {"PLATES", "CHEQUERED PLATE", "CHANNELS", "ROUNDS", "ANGLE", "BREAMS & JOISTS", "TMT STEEL", "FLATS", "SQUARE", "RAILS", "PIPE", "SHEET"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                txtShape.setText(TypeArray[item].trim());
                if (TypeArray[item].equalsIgnoreCase("PLATES")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Thickness");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.VISIBLE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("CHEQUERED PLATE")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Thickness");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.VISIBLE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("CHANNELS")) {
                    txtUnitThickness.setText("SECTION");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("ROUNDS")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Diameter");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("ANGLE")) {
                    txtUnitThickness.setText("SECTION");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("BREAMS & JOISTS")) {
                    txtUnitThickness.setText("SECTION");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("TMT STEEL")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Diameter");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("FLATS")) {
                    txtUnitThickness.setText("SECTION");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("SQUARE")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Thickness");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("RAILS")) {
                    txtUnitThickness.setText("LBS.");
                    txtThickness.setText("SECTION");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.GONE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("PIPE")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtThickness.setText("Thickness");
                    llThickness2.setVisibility(View.GONE);
                    llThickness.setVisibility(View.VISIBLE);
                    llWidth.setVisibility(View.VISIBLE);
                    llLength.setVisibility(View.VISIBLE);
                } else if (TypeArray[item].equalsIgnoreCase("SHEET")) {
                    txtUnitThickness.setText("MILLIMETER");
                    txtUnitThickness2.setText("MILLIMETER");
                    txtThickness.setText("Thickness");
                    llThickness2.setVisibility(View.VISIBLE);
                    llThickness.setVisibility(View.GONE);
                    llWidth.setVisibility(View.VISIBLE);
                    llLength.setVisibility(View.VISIBLE);
                }

                if (TypeArray[item].equalsIgnoreCase("PIPE")) {
                    txtWidth.setText("Outer Diameter");
                } else {
                    txtWidth.setText("Width");
                }


                txtThicknessValue.setText("");
                etWidth.setText("");
                etLength.setText("");
                txtUnitLength.setText("");
                txtUnitWidth.setText("");
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    //-----------------------------unit----------------------------//
    private void methodThicknessPopup(final String[] plate, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(plate, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                txtThicknessValue.setText(plate[item].trim());
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    //-----------------------------unit----------------------------//
    private void methodUnitPopup(final String type, final String unit) {
        // INCHES , FEET, YARD, MILLIMETER, CENTIMETER, METER
        final String TypeArray[] = {"INCHES", "FEET", "YARD", "MILLIMETER", "CENTIMETER", "METER"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (unit.equalsIgnoreCase("T")) {
                    txtUnitThickness.setText(TypeArray[item].trim());
                } else if (unit.equalsIgnoreCase("L")) {
                    txtUnitLength.setText(TypeArray[item].trim());
                } else if (unit.equalsIgnoreCase("W")) {
                    txtUnitWidth.setText(TypeArray[item].trim());
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    //---------------------------unit converter-----------------------------//
    public double converter(double value, final String type) {
        if (type == "CENTIMETER")
            value = value * 0.01;
        if (type == "METER")
            value = value;
        if (type == "MILLIMETER")
            value = value * 0.001;
        if (type == "FEET")
            value = value * 0.3048;
        if (type == "YARD")
            value = value * 0.9144;
        if (type == "INCHES")
            value = value * 0.0254;

        return value;
    }

    public double converterInMM(double value, final String type) {
        if (type == "CENTIMETER")
            value = value * 10;
        if (type == "METER")
            value = value * 1000;
        if (type == "MILLIMETER")
            value = value;
        if (type == "FEET")
            value = value * 304.8;
        if (type == "YARD")
            value = value * 914.4;
        if (type == "INCHES")
            value = value * 25.4;

        return value;
    }

    //----------------------------validation plates-----------------------//
    private boolean validationPlates(String thick, String tUnit, String width, String wUnit, String length, String lUnit) {
        if (TextUtils.isEmpty(thick)) {
            showToastMethod("You have not entered thickness.");
            return false;
        } else if (TextUtils.isEmpty(tUnit)) {
            showToastMethod("please select thickness unit.");
            return false;
        } else if (TextUtils.isEmpty(width)) {
            showToastMethod("You have not entered width.");
            return false;
        } else if (TextUtils.isEmpty(wUnit)) {
            showToastMethod("please select width unit.");
            return false;
        } else if (TextUtils.isEmpty(length)) {
            showToastMethod("You have not entered length.");
            return false;
        } else if (TextUtils.isEmpty(lUnit)) {
            showToastMethod("please select length unit.");
            return false;
        } else {
            return true;
        }
    }
}
