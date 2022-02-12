package com.chandru.mymusicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewsong);
        runtimePermission();
    }

    public void runtimePermission(){

        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaysongs();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }

    public ArrayList<File> findsong (File file){

        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
         for (File singleFile : files){
             if (singleFile.isDirectory() && !singleFile.isHidden()){
                 arrayList.addAll(findsong(singleFile));
             }else{
                 if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                     arrayList.add(singleFile);
                 }
             }
         }
         return arrayList;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public ArrayList<File> findsong (File file){
//
//        ArrayList<File> arraylist = new ArrayList<>();
//
//        File f = file;
//
//        File[] files = null;
//
//        try {
//            files = f.listFiles();
//
//            if (files.length > 0) {
//                for (File singleFile : files) {
//
//                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
//                        arraylist.addAll(findsong(singleFile));
//                    } else {
//                        if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
//
//                            arraylist.add(singleFile);
//                        }
//                    }
//                }
//            } else {
//                Path path = Paths.get("/storage/emulated/0/Music");
//                File filePath = path.toFile();
//                arraylist.add(filePath);
//            }
//        } catch (NullPointerException e) {
////            toastMsg("Error : " + e + " " + e.getStackTrace()[0].getLineNumber());
//        }
//
//        return arraylist;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void displaysongs(){

        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mysongs.size()];
        for (int i=0;i< mysongs.size(); i++){
            items[i] = mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
//        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
//        listView.setAdapter(myadapter);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textSong = view.findViewById(R.id.txtxsongName);
            textSong.setSelected(true);
            textSong.setText(items[position]);

            return view;
        }
    }
}
