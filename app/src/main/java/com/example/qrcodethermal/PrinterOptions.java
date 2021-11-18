package com.example.qrcodethermal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class PrinterOptions {
    String commandSet="";

    public String initialize()
    {
        final byte[] Init={27,64};

        commandSet+=new String(Init);
        setTab();
        return new String(Init);
    }

    public String chooseFont(int Options)
    {
        String s="";
        final byte[] ChooseFontA={27,77,0}; //Font A 12
        final byte[] ChooseFontB={27,77,1}; //Font B 9
        final byte[] ChooseFontC={27,77,48};//Font A 24
        final byte[] ChooseFontD={27,77,49};//Font B 17

        switch(Options)
        {
            case 1:
                s=new String(ChooseFontA);
                break;

            case 2:
                s=new String(ChooseFontB);
                break;

            case 3:
                s=new String(ChooseFontC);
                break;

            case 4:
                s=new String(ChooseFontD);
                break;

            default:
                s=new String(ChooseFontB);
        }
        commandSet+=s;
        return new String(s);
    }

    public String feedBack(byte lines)
    {
        final byte[] Feed={27,74,lines};//27,101
        String s=new String(Feed);
        commandSet+=s;
        return s;
    }

    public String feed(byte lines)
    {
        final byte[] Feed={27,100,lines};
        String s=new String(Feed);
        commandSet+=s;
        return s;
    }

    public String alignLeft()
    {
        final byte[] AlignLeft={27, 97,48};


        String s=new String(AlignLeft);
        commandSet+=s;
        return s;
    }

    public String alignCenter()
    {
        final byte[] AlignCenter={27, 97,49};


        String s=new String(AlignCenter);
        commandSet+=s;
        return s;
    }

    public String alignRight()
    {
        final byte[] AlignRight={27, 97,50};


        String s=new String(AlignRight);
        commandSet+=s;
        return s;
    }

    public String newLine()
    {
        final  byte[] LF={10};


        String s=new String(LF);
        commandSet+=s;
        return s;
    }

    public String reverseColorMode(boolean enabled)
    {
        final byte[] ReverseModeColorOn={29 ,66,1};
        final byte[] ReverseModeColorOff={29 ,66,0};

        String s="";
        if(enabled)
        {
            s=new String(ReverseModeColorOn);
        }
        else
        {
            s=new String(ReverseModeColorOff);
        }

        commandSet+=s;

        return s;
    }

    public String doubleStrike(boolean enabled)
    {
        final byte[] DoubleStrikeModeOn={27, 71,1};
        final byte[] DoubleStrikeModeOff={27, 71,0};

        String s="";
        if(enabled)
        {
            s=new String(DoubleStrikeModeOn);
        }
        else
        {
            s=new String(DoubleStrikeModeOff);
        }

        commandSet+=s;
        return s;
    }

    public String doubleHeight(boolean enabled)
    {
        final byte[] DoubleHeight={27,33,16};
        final byte[] UnDoubleHeight={27,33,0};
        String s="";
        if(enabled)
        {
            s=new String(DoubleHeight);
        }
        else
        {
            s=new String(UnDoubleHeight);
        }


        commandSet+=s;
        return s;
    }

    public String doubleWidth(boolean enabled)
    {
        final byte[] DoubleWidth={27,33,32};
        final byte[] UnDoubleWidth={27,33,0};
        String s="";
        if(enabled) {
            s=new String(DoubleWidth);
        }
        else {
            s=new String(UnDoubleWidth);
        }
        commandSet+=s;
        return s;
    }

    public String bold(boolean enabled)
    {
        final byte[] Bold={27,33,8};
        final byte[] unBold={27,33,0};
        String s="";
        if(enabled) {
            s=new String(Bold);
        }
        else {
            s=new String(unBold);
        }
        commandSet+=s;
        return s;
    }

    public String small(boolean enabled)
    {
        final byte[] Small={27,33,1};
        final byte[] unSmall={27,33,0};
        String s="";
        if(enabled) {
            s=new String(Small);
        }
        else {
            s=new String(unSmall);
        }
        commandSet+=s;
        return s;
    }

    public String emphasized(boolean enabled)
    {
        final byte[] EmphasizedOff={27 ,0};
        final byte[] EmphasizedOn={27 ,1};

        String s="";
        if(enabled)
        {
            s=new String(EmphasizedOn);
        }
        else
        {
            s=new String(EmphasizedOff);
        }


        commandSet+=s;
        return s;
    }

    public String underLine(int Options)
    {
        final byte[] UnderLine2Dot={27 ,45,50};
        final byte[] UnderLine1Dot={27 ,45,49};
        final byte[] NoUnderLine={27 ,45,48};

        String s="";
        switch(Options)
        {
            case 0:
                s=new String(NoUnderLine);
                break;

            case 1:
                s=new String(UnderLine1Dot);
                break;

            default:
                s=new String(UnderLine2Dot);
        }
        commandSet+=s;
        return new String(s);
    }

    public String setTab()
    {
        final byte[] tab={27 ,68, 3,14,24,28,32,0};

        String s="";
        s = new String(tab);
        commandSet+=s;
        return new String(s);
    }

    public String setCustomTab(byte[]intTabChar)
    {
        final byte[] tab={27 ,68, 3,14,24,28,32,0};

        tab[2] = ((byte)(intTabChar[0]|0x3 ));
        tab[3] = ((byte)(intTabChar[1]|0x14));
        tab[4] = ((byte)(intTabChar[2]|0x24));
        tab[5] = ((byte)(intTabChar[3]|0x28));
        tab[6] = ((byte)(intTabChar[4]|0x32));

        String s="";
        s = new String(tab);
        commandSet+=s;
        return new String(s);
    }

    public String color(int Options)
    {

        final byte[] ColorRed={27,114,49};
        final byte[] ColorBlack={27,114,48};
        String s="";
        switch(Options)
        {
            case 0:
                s=new String(ColorBlack);
                break;

            case 1:
                s=new String(ColorRed);
                break;

            default:
                s=new String(ColorBlack);
        }

        commandSet+=s;
        return s;
    }

    public String finit()
    {
        final byte[] FeedAndCut={29,'V',66,0};


        String s=new String(FeedAndCut);

        final byte[] DrawerKick={27,70,0,60,120};


        s+=new String(DrawerKick);

        commandSet+=s;
        return s;
    }

    public String addLineSeperator()
    {
        String lineSpace="--------------------------------";
        commandSet+=lineSpace;
        return lineSpace;
    }

    public String addLineSeperatorCut()
    {
        String lineSpace="================================";
        commandSet+=lineSpace;
        return lineSpace;
    }

    public void resetAll()
    {
        commandSet="";
    }

    public void setText(String s)
    {
        commandSet+=s;
    }

    public void write(byte[] b){
        commandSet +=b;
    }

    public String finalCommandSet()
    {
        return commandSet;
    }

    public void PrintHeader()
    {
        alignCenter();
        bold(true);
        setText("PT Indomobil Sukses Internasional Tbk");
        bold(false);
        alignCenter();
        newLine();
        setText("Jl. MT Haryono Kav. 8");
        newLine();
        setText("Jakarta 13330 Indonesia");
        newLine();
        setText("Telp Hunting: ");
        newLine();
        setText("62-21 8564850 / 8564860");
        newLine();
        addLineSeperator();
    }
    public void PrintHeader(Bundle DataHeader)
    {
        alignCenter();
        bold(true);
        setText(DataHeader.getString("CompanyName"));
        bold(false);
        alignCenter();
        newLine();
        setText(DataHeader.getString("CompanyAddress1"));
        newLine();
        setText(DataHeader.getString("CompanyAddress2"));
        newLine();
        setText("Telp : "+DataHeader.getString("CompanyTelp"));
        newLine();
        //setText(DataHeader.getString("CompanyTelp"));
        //newLine();
        addLineSeperator();
        newLine();
        setText(DataHeader.getString("DocTitle"));
        newLine();
        addLineSeperator();

    }
}
