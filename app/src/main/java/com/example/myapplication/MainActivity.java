package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.appsng.greendaoapp.db.DaoMaster;
import com.appsng.greendaoapp.db.DaoSession;
import com.appsng.greendaoapp.db.product;
import com.appsng.greendaoapp.db.productDao;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity  implements View.OnClickListener  {
    private Button buttonScan;
    boolean doubleBackToExitPressedOnce = false;
    private TextView textViewName,
            textViewCount,
            textViewPrice,
            view,
            textViewCode;
    private IntentIntegrator qrScan;
    private DaoSession daoSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"users-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
         daoSession = new DaoMaster(db).newSession();
        textViewName = (TextView) findViewById(R.id.name_edit_text);
        textViewCount = (TextView) findViewById(R.id.count_edit_text);
        textViewCode = (TextView) findViewById(R.id.code_edit_text);
        textViewPrice = (TextView) findViewById(R.id.price_edit_text);
        qrScan = new IntentIntegrator(this);
        buttonScan.setOnClickListener(this);
}
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "لطفا برای خروج دکمه برگشت را بزنید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String result1= result.getContents();
                textViewCode.setText(arabicToDecimal(result1));
                QueryBuilder<product> qb =daoSession.getProductDao().queryBuilder();

            List<product> productList= qb.where(productDao.Properties.Code.eq(result1)).list();
            if(!productList.isEmpty()) {

                 Toast.makeText(this, productList.get(0).getName(), Toast.LENGTH_SHORT).show();
                  new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme)
                          .setTitle("")
            .setMessage("نام محصول : "  + productList.get(0).getName()+ "\n"+  "قیمت : " +productList.get(0).getPrice()+ "\n" + " کد  : "
            +productList.get(0).getCode()+"\n"+  "تعداد : "+ productList.get(0).getCount())
            .setPositiveButton("Ok", null)
            .show();
            }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.setCaptureActivity(CustomScannerActivity.class);
        qrScan.initiateScan();
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
    public void insert(View view){
       List<product> p= daoSession.getProductDao().queryBuilder().where(productDao.Properties.Code.eq(arabicToDecimal(textViewCode.getText().toString()))).list();

       if(p.isEmpty()){
        product product=new product();
        product.setName(textViewName.getText().toString());
        product.setCount(arabicToDecimal( textViewCount.getText().toString()));
        product.setPrice(arabicToDecimal(textViewPrice.getText().toString()));
        product.setCode(arabicToDecimal(textViewCode.getText().toString()));
        daoSession.insert(product);
        Toast.makeText(this,"ثبت با موفقیت انجام شد",Toast.LENGTH_LONG).show();}
       else {
           Toast.makeText(this,"این محصول هم اکنون وجود دارد لطفا اول ان را حذف کنید",Toast.LENGTH_LONG).show();
       }
    }
    public void delete(View view){
        final DeleteQuery<product> tableDeleteQuery  = daoSession.getProductDao().queryBuilder().where(productDao.Properties.Code.eq(arabicToDecimal(textViewCode.getText().toString()))).buildDelete();

        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
     Toast.makeText(this,"حذف با موفقیت انجام شد",Toast.LENGTH_LONG).show();
    }
    public  void plus(View view)
    {
      List<product> listp=  daoSession.getProductDao().queryBuilder().where(productDao.Properties.Code.eq(arabicToDecimal(textViewCode.getText().toString()))).list();
      if(!listp.isEmpty()){
          product p=new product();
          p.setCount(""+(Integer.parseInt(listp.get(0).getCount())+1));
          p.setId(listp.get(0).getId());
          p.setCode(listp.get(0).getCode());
          p.setPrice(listp.get(0).getPrice());

         daoSession.insertOrReplace(p);
      }
      Toast.makeText(this, "آپدیت با موفقیت انجام شد" ,Toast.LENGTH_LONG).show();
    }
    public void negetive(View view){
        List<product> listp=  daoSession.getProductDao().queryBuilder().where(productDao.Properties.Code.eq(textViewCode.getText().toString())).list();
        if(!listp.isEmpty()){
            product p=new product();
            p.setCount(""+(Integer.parseInt(listp.get(0).getCount())-1));
            p.setId(listp.get(0).getId());
            p.setCode(listp.get(0).getCode());
            p.setPrice(listp.get(0).getPrice());

            daoSession.insertOrReplace(p);
        }
        Toast.makeText(this, "آپدیت با موفقیت انجام شد" ,Toast.LENGTH_LONG).show();
    }
public void getexcell(View view){
    String Fnamexls="excelSheet"+System.currentTimeMillis()+ ".xls";
    File sdCard = Environment.getExternalStorageDirectory();
    File directory = new File (sdCard.getAbsolutePath() + "/newfolderfa");
    if (!directory.isDirectory()) {
        directory.mkdirs();

    }
   // directory.mkdirs();
    File file = new File(directory, Fnamexls);

    WorkbookSettings wbSettings = new WorkbookSettings();

    wbSettings.setLocale(new Locale("en", "EN"));
    try {
        FileWriter writer = new FileWriter(file);
        writer.flush();
        writer.close();
     WritableWorkbook workbook;
        workbook = Workbook.createWorkbook(file, wbSettings);
        WritableSheet sheet = workbook.createSheet("product", 0);
        sheet.addCell(new Label(0, 0, "نام"));
        sheet.addCell(new Label(1, 0, "کد"));
        sheet.addCell(new Label(2, 0, "قیمت"));
        sheet.addCell(new Label(3, 0, "تعداد"));
        List<product> p= daoSession.getProductDao().queryBuilder().list();
        if (!p.isEmpty() ) {
            for (int i=0;i<=p.size()-1;i++) {
                sheet.addCell(new Label(0, i+1, p.get(i).getName()));
                sheet.addCell(new Label(1, i+1, p.get(i).getCode()));
                sheet.addCell(new Label(2, i+1, p.get(i).getPrice()));
                sheet.addCell(new Label(3, i+1, p.get(i).getCount()));
            }
        }
        workbook.write();
        workbook.close();
        Toast.makeText(getApplication(),
                "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
    } catch(Exception e){
        Toast.makeText(getApplication(),e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
public void invoice(View view){
        Intent invoice=new Intent(this,ListViewWithCheckboxActivity.class);
        startActivity(invoice);
}
}
