package com.example.qrcodethermal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.sql.Timestamp;

public class PrintQrOut {
    public PrinterOptions PrintOutFormat = null;
    Bundle HeaderData = null;


    public PrintQrOut(boolean isReprint, boolean isFuelPage){

        PrintOutFormat = new PrinterOptions();

        PrintOutFormat.setText("Hello");
        PrintOutFormat.newLine();
        PrintOutFormat.feed((byte)1); //Adding space for manual cut

    }

}
