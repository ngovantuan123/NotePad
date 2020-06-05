package com.tuangh.notepad;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lvNote;
    ArrayList<NoteItem> arrayNote;
    NoteItemAdapter adapter;
    ImageButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();

        btnAdd=findViewById(R.id.btnThem);
        lvNote = (ListView) findViewById(R.id.listviewNoiDung);
        arrayNote=XuLyFileXML.readByDOM("note.xml");
        registerForContextMenu(lvNote);

        adapter = new NoteItemAdapter(this,R.layout.dong_noi_dung,arrayNote);
        lvNote.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateNote();

            }
        });
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfor(position);

            }
        });



    }

    private void showInfor(int position) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_showinfor);
        TextView txtNoiDung=dialog.findViewById(R.id.txtshownoidung);
        TextView txtDate=dialog.findViewById(R.id.textshowdate);
        Button btnHuy=dialog.findViewById(R.id.btnHuyEdit);
        NoteItem item=arrayNote.get(position);
        txtNoiDung.setText(item.getContent());
        txtDate.setText(item.getCreatedDate());
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_context,menu);
        menu.setHeaderTitle("Hành động");

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.menuSua:
                addOrUpdateNote(info.position);

                return true;
            case  R.id.menuXoa:
                deleteItem(info.position);

                return true;
            default:
              return   super.onContextItemSelected(item);
        }
    }
    public void deleteItem(int position)
    {
        this.arrayNote.remove(position);
        XuLyFileXML.saveData("note.xml",this.arrayNote);
        adapter.notifyDataSetChanged();

    }
    public void addOrUpdateNote(final Object... parameters)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_noi_dung);
        final ArrayList<NoteItem> temp=this.arrayNote;


        final EditText edtTieuDe = dialog.findViewById(R.id.editTextTieude);
        final EditText edtNoiDung = dialog.findViewById(R.id.editTextNoiDung);
        TextView txtThem=dialog.findViewById(R.id.textThem);
        Button btnThem = dialog.findViewById(R.id.btnThem);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        if(parameters.length==1)
        {
            txtThem.setText("Sửa nội dung");
            btnThem.setText("Sửa");
            NoteItem noteItem=this.arrayNote.get((int) parameters[0]);
            edtTieuDe.setText(noteItem.getTitle());
            edtNoiDung.setText(noteItem.getContent());
        }


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtNoiDung = edtNoiDung.getText().toString();
                String txtTieuDe=edtTieuDe.getText().toString();
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String txtDate = formatter.format(date);
                String message="";

                if(txtNoiDung.equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(parameters.length==0)
                    {
                        NoteItem note=new NoteItem();
                        if (txtTieuDe.equals("")) txtTieuDe = "No title";
                        note.setTitle(txtTieuDe);
                        note.setContent(txtNoiDung);
                        note.setCreatedDate(txtDate);
                        temp.add(note);
                        message="Thêm thành công";
                    }else {
                        if (txtTieuDe.equals("")) txtTieuDe = "No title";
                        temp.get((int)parameters[0]).setTitle(txtTieuDe);
                        temp.get((int)parameters[0]).setContent(txtNoiDung);
                        adapter.notifyDataSetChanged();
                        message="Sửa thành công";
                    }


                    XuLyFileXML.saveData("note.xml",temp);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
public void loadData()
{
    this.arrayNote=XuLyFileXML.readByDOM("note.xml");
    adapter.notifyDataSetChanged();
}


    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    }
