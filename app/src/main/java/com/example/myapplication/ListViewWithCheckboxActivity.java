package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.appsng.greendaoapp.db.DaoMaster;
import com.appsng.greendaoapp.db.DaoSession;
import com.appsng.greendaoapp.db.product;
import com.appsng.greendaoapp.db.productDao;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ListViewWithCheckboxActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnscan;
    private BaseFont bfBold;
    TextView totalprice;
    List<ListViewItemDTO> ret = new ArrayList<ListViewItemDTO>();
    private IntentIntegrator qrScan;
    private DaoSession daoSession;
    ListViewItemCheckboxBaseAdapter  listViewDataAdapter;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_with_checkbox);
        btnscan=(Button)findViewById(R.id.list_add_scan);
        totalprice=(TextView)findViewById(R.id.totalprice);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"users-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        ctx=getApplicationContext();
        daoSession = new DaoMaster(db).newSession();
        // Get listview checkbox.
       final ListView listViewWithCheckbox = (ListView)findViewById(R.id.list_view_with_checkbox);

        final List<ListViewItemDTO> initItemList = this.getInitViewItemDtoList();
        listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(), ret);
        listViewDataAdapter.notifyDataSetChanged();
        btnscan.setOnClickListener(this);
        qrScan = new IntentIntegrator(this);
        totalprice.setText("مجموع : 0 ");
        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        // When list view item is clicked.
        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }

                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

        // Click this button to select all listview items with checkbox checked.
        Button selectAllButton = (Button)findViewById(R.id.list_select_all);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);
                    dto.setChecked(true);
                }
                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to disselect all listview items with checkbox unchecked.
        Button selectNoneButton = (Button)findViewById(R.id.list_select_none);
        selectNoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);
                    dto.setChecked(false);
                }

                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to reverse select listview items.
      //  Button selectReverseButton = (Button)findViewById(R.id.list_select_reverse);
    //    selectReverseButton.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          int size = initItemList.size();
       //         for(int i=0;i<size;i++)
       ///         {
        //            ListViewItemDTO dto = initItemList.get(i);

        //            if(dto.isChecked())
        //            {
         //               dto.setChecked(false);
         //           }else {
          //              dto.setChecked(true);
          //          }
          //      }
//
         //       listViewDataAdapter.notifyDataSetChanged();
         //   }
       // });
        // Click this button to remove selected items from listview.
        Button selectRemoveButton = (Button)findViewById(R.id.list_remove_selected_rows);
        selectRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(ListViewWithCheckboxActivity.this).create();
                alertDialog.setMessage("Are you sure to remove selected listview items?");
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = initItemList.size();
                        for(int i=0;i<size;i++)
                        {
                            ListViewItemDTO dto = initItemList.get(i);

                            if(dto.isChecked())
                            {
                                initItemList.remove(i);
                                i--;
                                size = initItemList.size();
                            }
                        }
                        double price=0;
                        for (int i=0;i<ret.size();i++){
                            price=price+    Double.parseDouble(ret.get(i).getItemPrice())*Integer.parseInt(ret.get(i).getItemCount());
                        }
                        totalprice.setText("مجموع : "+price);
                        listViewDataAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
            }
        });
    }
    private List<ListViewItemDTO> getInitViewItemDtoList()
    {

        return  ret;
    }
    public void getprice(){
        double price=0;
       for (int i=0;i<ret.size();i++){
       price=price+    Double.parseDouble(ret.get(i).getItemPrice())*Integer.parseInt(ret.get(i).getItemCount());
       }
       totalprice.setText("مجموع : "+price);
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String result1= result.getContents();

                QueryBuilder<product> qb =daoSession.getProductDao().queryBuilder();

            final     List<product> productList= qb.where(productDao.Properties.Code.eq(arabicToDecimal(result1))).list();
                if(!productList.isEmpty()) {


                    final ListViewItemDTO dto = new ListViewItemDTO();
                    dto.setChecked(false);
                    dto.setItemText(productList.get(0).getName());

                    dto.setItemPrice(productList.get(0).getPrice());
                    dto.setItemBarcode(productList.get(0).getCode());
                    final EditText txtUrl = new EditText(this);

// Set the default text to a link of the Queen
                    //txtUrl.setInputType(InputType.TYPE_CLASS_NUMBER);
                    txtUrl.setHint("تعداد");
                    txtUrl.setHeight(200);

                    new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme)
                            .setTitle("")
                           .setView(txtUrl)

                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dto.setItemCount( arabicToDecimal( txtUrl.getText().toString()));
                                    Iterator<ListViewItemDTO> iterator = ret.iterator();
                                    int i=0;

                                    while (iterator.hasNext()) {

                                        ListViewItemDTO dto2 = iterator.next();
                                        if (dto2.getItemText().equals( productList.get(0).getName())) {
                                           ret.remove(i);
                                            listViewDataAdapter.notifyDataSetChanged();
                                           }
                                        else {
                                        }
                                        ++i;
                                    }
                                    ret.add(dto);
                                    listViewDataAdapter.notifyDataSetChanged();
                                    Toast.makeText(ListViewWithCheckboxActivity.this,"ایتم  آپدیت شد",Toast.LENGTH_SHORT).show();
                                    getprice();
                                }} )
                            .show();


                   // Toast.makeText(this, productList.get(0).getName(), Toast.LENGTH_SHORT).show();
                  //  new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme)
                    //        .setTitle("")
//
                   //         .setMessage("نام محصول : "  + productList.get(0).getName()+ "\n"+  "قیمت : " +productList.get(0).getPrice()+ "\n" + " کد  : "
                    ///                +productList.get(0).getCode()+"\n"+  "تعداد : "+ productList.get(0).getCount())
                     //       .setPositiveButton("Ok", null)
                    //       .show();
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }            }
            this.getprice();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
            qrScan.setCaptureActivity(CustomScannerActivity.class);
            qrScan.initiateScan();
    }
    public void excellinvoice(View view){
       Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
            File sd = Environment.getExternalStorageDirectory();
            String csvFile = ts+"myData.xls";
            File directory = new File(sd.getAbsolutePath());
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
            try {
                File file = new File(directory, csvFile);
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook;
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("product", 0);
                sheet.addCell(new Label(0, 0, "نام"));
                sheet.addCell(new Label(1, 0, "قیمت"));
                sheet.addCell(new Label(2, 0, "تعداد"));
               // List<product> p= daoSession.getProductDao().queryBuilder().list();
                Double price=0.0;
                int count=0;
                if (!ret.isEmpty() ) {
                    price=0.0;
                    for (int i=0;i<=ret.size()-1;i++) {
                        price=price+Double.parseDouble(ret.get(i).getItemPrice());
                        count=count+Integer.parseInt(ret.get(i).getItemCount());
                        sheet.addCell(new Label(0, i+1, ret.get(i).getItemText()));
                        sheet.addCell(new Label(1, i+1, ret.get(i).getItemPrice()));
                        sheet.addCell(new Label(2, i+1, ret.get(i).getItemCount()));
                    }
                }
                sheet.addCell(new Label(0, ret.size()+1, "مجموع"));
                sheet.addCell(new Label(1, ret.size()+1, ""+price));
                sheet.addCell(new Label(2, ret.size()+1, ""+count));
                workbook.write();
                workbook.close();
                Toast.makeText(getApplication(),
                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
            } catch(Exception e){
                Toast.makeText(getApplication(),
                        "error", Toast.LENGTH_SHORT).show();
            }
        }
    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
    public void createandDisplayPdf(View view) {

        //sta00rt
        Document document = new Document();
// Location to save
try {
        String dest=Environment.getExternalStorageDirectory()+"/mypdf.pdf";
   PdfWriter docWriter=     PdfWriter.getInstance(document, new FileOutputStream(dest));
    initializeFonts();
        document.open();
    PdfContentByte cb = docWriter.getDirectContent();
    BaseFont urName = BaseFont.createFont("assets/fonts/BNAZANIN.TTF",BaseFont.IDENTITY_H , false);

       Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
  Chunk mOrderDetailsTitleChunk = new Chunk("ادی",mOrderDetailsTitleFont);

         document.add(mOrderDetailsTitleChunk);
            document.close();
    }
    catch (Exception er){
Toast.makeText(this,er.getMessage(),Toast.LENGTH_LONG).show();
    }}
    private void initializeFonts(){


        try {
            bfBold  = BaseFont.createFont("assets/fonts/BNAZANIN.TTF",BaseFont.IDENTITY_H , false);

            //bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createHeadings(PdfContentByte cb, float x, float y, String text){

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }
    }