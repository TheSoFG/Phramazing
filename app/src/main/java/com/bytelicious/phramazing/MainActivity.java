package com.bytelicious.phramazing;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bytelicious.phramazing.app.PhramazingActivity;
import com.bytelicious.phramazing.home.HomeFragment;
import com.bytelicious.phramazing.model.User;
import com.bytelicious.phramazing.trending.TrendingFragment;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends PhramazingActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static final int REQUEST_WRITE_STORAGE = 1337;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    exportRealm();
                } else {
                    Toast toast = Toast.makeText(this, "Write permission to the external storage is needed for this app", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                return;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.export_db:
                boolean hasPermission = (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                } else {
                    exportRealm();
                }
                return true;
            case R.id.import_db:
                importDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportRealm() {
        try (Realm realm = Realm.getDefaultInstance()) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(new Date());
            String fileName = timestamp + "_Phramazing.realm";
            File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/" + fileName));//"/default.realm"));
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            realm.writeCopyTo(file);
            Toast.makeText(MainActivity.this, "Success export realm file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();
                setTitle(realmInstance.where(User.class).findFirst().getUsername());
                return true;
            case R.id.navigation_trending:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TrendingFragment.newInstance())
                        .commit();
                setTitle(R.string.title_trending);
                return true;
            case R.id.navigation_notifications:
                Toast.makeText(this, "Under construction", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }


    private void importDb() {

        DialogProperties properties = new DialogProperties();

        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"realm"};
        FilePickerDialog dialog = new FilePickerDialog(MainActivity.this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener((DialogSelectionListener) files -> {
            try {
                File file = new File(files[0]);

                FileOutputStream outputStream = new FileOutputStream(Realm.getDefaultConfiguration().getPath());

                FileInputStream inputStream = new FileInputStream(new File(files[0]));

                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, bytesRead);
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dialog.show();

    }
}