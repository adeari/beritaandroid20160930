package com.example.ade.beritasurabaya;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ade.beritasurabaya.data.Alamat;
import com.example.ade.beritasurabaya.data.BaseItem;
import com.example.ade.beritasurabaya.data.CustomDataProvider;
import com.example.ade.beritasurabaya.data.PesanUser;
import com.example.ade.beritasurabaya.views.AlamatKantorList;
import com.example.ade.beritasurabaya.views.LevelBeamView;
import com.example.ade.beritasurabaya.views.PesanList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private MainActivity mainActivity;
    private ProgressBar populerProgressBar;
    private ProgressBar progressBarPesanUser;
    private ProgressBar kirimPesanprogressBar;
    private ListView populerListView;
    private DrawerLayout drawer;
    private ListImageText _listImageText;
    private ListImageTextRalat _listImageTextRalat;

    private ListView listPesan;
    private PesanList pesanList;
    private ArrayList<PesanUser> pesanUsers;

    private ListView listAlamatKantor;
    private AlamatKantorList alamatKantorList;
    private ArrayList<Alamat> alamats;

    private ProgressBar beritaAddProgressbar;
    private ProgressBar beritaDetailProgressBar;
    private ProgressBar progressbarKomentarAdd;
    private ProgressBar loginProgressbar;
    private ProgressBar profileProgressBar;

    private ImageView beritaAddImageimageView;
    private File fileUpload;
    private SecureRandom secureRandom;

    private ImageView imageberitadetail;
    private ImageView beritadetailkomentarimageview;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String viewLayout;
    private String beritaShow;
    private String imageAction;
    private String idKomentarDeleted;

    private Button beritAddHapusGambar;
    private Button bertiaDetailAddKomentar;
    private Button populerTambahBeritaButton;
    private Button buttonShareBerita;

    private ImageView imageViewAccess;
    private CircleImageView profileImage;

    private MultiLevelListView multiLevelListView;

    private LinearLayout linerlayoutVerticalDAta;

    private List<BaseItem> baseItemList;

    private Spinner beritaAddSpinner;

    private com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText loginpassword;

    private LocationManager locationManager;
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private Timer timer;
    private MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secureRandom = new SecureRandom();
        mainActivity = this;
        beritaShow = "";
        imageAction = "";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        populerListView = (ListView) findViewById(R.id.listPopuler);

        populerProgressBar = (ProgressBar) findViewById(R.id.progressBarPopuler);
        populerProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);

        progressBarPesanUser = (ProgressBar) findViewById(R.id.progressBarPesanUser);
        progressBarPesanUser.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBarPesanUser.setVisibility(View.GONE);

        kirimPesanprogressBar = (ProgressBar) findViewById(R.id.kirimPesanprogressBar);
        kirimPesanprogressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        kirimPesanprogressBar.setVisibility(View.GONE);

        beritaAddProgressbar = (ProgressBar) findViewById(R.id.beritaAddProgressbar);
        beritaAddProgressbar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaAddProgressbar.getLayoutParams().height = 200;
        beritaAddProgressbar.setVisibility(View.GONE);

        progressbarKomentarAdd = (ProgressBar) findViewById(R.id.progressbarKomentarAdd);
        progressbarKomentarAdd.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressbarKomentarAdd.setVisibility(View.GONE);

        loginProgressbar = (ProgressBar) findViewById(R.id.loginprogressbar);

        profileProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        profileProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        profileProgressBar.setVisibility(View.GONE);

        beritaDetailProgressBar = (ProgressBar) findViewById(R.id.beritadetailProgressBar);
        beritaDetailProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaDetailProgressBar.setVisibility(View.GONE);

        beritaAddImageimageView = (ImageView) findViewById(R.id.beritaAddImage);
        beritaAddImageimageView.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        beritaAddImageimageView.setVisibility(View.GONE);

        profileImage = (CircleImageView) findViewById(R.id.profileUserImage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        _arrayImageTexts = new ArrayList<ImageText>();
        _listImageText = new ListImageText(mainActivity, _arrayImageTexts);
        _listImageTextRalat = new ListImageTextRalat(mainActivity, _arrayImageTexts);

        pesanUsers = new ArrayList<PesanUser>();
        pesanList = new PesanList(mainActivity, pesanUsers);
        listPesan = (ListView) findViewById(R.id.listpesan);
        listPesan.setAdapter(pesanList);

        alamats = new ArrayList<Alamat>();
        alamats.add(new Alamat("Surabaya", "Jl. Ketintang no 334", "045446464", "cs@surabayadigitalcity.net"));
        alamatKantorList = new AlamatKantorList(mainActivity, alamats);
        listAlamatKantor = (ListView) findViewById(R.id.listalamatkantor);
        listAlamatKantor.setAdapter(alamatKantorList);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{"Umum", "Acara", "Pengaduan"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beritaAddSpinner = (Spinner) findViewById(R.id.beritaaddSpinner);
        beritaAddSpinner.setAdapter(dataAdapter);

        ((Button) findViewById(R.id.beritaaddGaleriButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }
                }
        );

        ((Button) findViewById(R.id.bertiadetailgallerikomentarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 12);
                    }
                }
        );

        ((ImageButton) findViewById(R.id.profilegalerybutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 41);
                    }
                }
        );

        ((Button) findViewById(R.id.beritaaddKameraButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7777);
                    }
                }
        );

        ((Button) findViewById(R.id.bertiadetailkamerakomentarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 13);
                    }
                }
        );

        populerTambahBeritaButton = ((Button) findViewById(R.id.populerTambahBeritaButton));
        populerTambahBeritaButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tambahBeritaPrepare();
                    }
                }
        );

        ((ImageButton) findViewById(R.id.profilekamerabutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 42);
                    }
                }
        );

        beritAddHapusGambar = (Button) findViewById(R.id.beritaddhapusgambar);
        beritAddHapusGambar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageAction = "hapus";
                        beritaAddImageimageView.setVisibility(View.GONE);
                    }
                }
        );

        ((Button) findViewById(R.id.beritaaddSimpan)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((EditText) findViewById(R.id.beritaaddJudulBerita)).getText().length() == 0) {
                            showAlert("Tulis Judul Berita");
                        } else if (((EditText) findViewById(R.id.beritaaddDeskripsi)).getText().length() == 0) {
                            showAlert("Tulis Deskripsi Berita");
                        } else {
                            new PostBerita().execute();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.daftarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.userregister), View.VISIBLE);
                        findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
                    }
                }
        );
        ((Button) findViewById(R.id.daftarsimpanbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwordRegister = ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.daftarpassword)).getText().toString();
                        String passwordreRegister = ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.daftarrepassword)).getText().toString();
                        String usernamenik = ((EditText) findViewById(R.id.daftarusernamenik)).getText().toString();
                        if (usernamenik.length() == 0) {
                            showAlert("Tulis User Name / NIK");
                        } else if (passwordRegister.length() == 0) {
                            showAlert("Tulis Password");
                        } else if (!passwordRegister.equals(passwordreRegister)) {
                            showAlert("Password dan Re Password harus sama");
                        } else {
                            new registeruser().execute();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.loginbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new loginuser().execute();
                    }
                }
        );

        bertiaDetailAddKomentar = (Button) findViewById(R.id.bertiadetailaddkomentar);
        bertiaDetailAddKomentar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.beritadetailkomentarform).setVisibility(View.VISIBLE);
                        beritadetailkomentarimageview.setVisibility(View.GONE);
                        ((EditText) findViewById(R.id.beritadetailkomentar)).setText("");
                        fileUpload = null;
                        imageAction = "";
                        bertiaDetailAddKomentar.setVisibility(View.GONE);
                        ((ScrollView) findViewById(R.id.beritadetail)).requestChildFocus(findViewById(R.id.beritadetaillastcomponent),
                                findViewById(R.id.beritadetaillastcomponent));
                    }
                }
        );

        ((Button) findViewById(R.id.beritadetailsimpankomentar)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((EditText) findViewById(R.id.beritadetailkomentar)).getText().length() == 0) {
                            showAlert("Tulis komentar");
                        } else {
                            new PostKomentar().execute();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.profileEditButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((EditText) findViewById(R.id.profileNama)).getText().length() == 0) {
                            showAlert("Isikan nama");
                        } else if (((EditText) findViewById(R.id.profileNik)).getText().length() == 0) {
                            showAlert("Isikan NIK");
                        } else if (((EditText) findViewById(R.id.profileEmail)).getText().length() == 0) {
                            showAlert("Isikan Email");
                        } else {
                            new EditMyProfile().execute();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.profileGantiPasswordButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String profilePassword = ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.profilePassword)).getText().toString();
                        if (profilePassword.length() == 0) {
                            showAlert("Isikan Password");
                        } else if (!profilePassword.equals(((EditText) findViewById(R.id.profileRePassword)).getText().toString())) {
                            showAlert("Re password harus sama dengan password");
                        } else {
                            new GantiPasswordProfileProfile().execute();
                        }
                    }
                }
        );
        ((Button) findViewById(R.id.buttonforsendpesan)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean cansave = true;
                        String dataText = ((EditText) findViewById(R.id.emailTextKirimPesan)).getText().toString();
                        if (dataText.isEmpty()) {
                            showAlert("Isikan Email");
                            cansave = false;
                        }

                        dataText = ((EditText) findViewById(R.id.judulTextKirimPesan)).getText().toString();
                        if (dataText.isEmpty()) {
                            showAlert("Isikan Judul Pesan");
                            cansave = false;
                        }

                        dataText = ((EditText) findViewById(R.id.pesanTextKirimPesan)).getText().toString();
                        if (dataText.isEmpty()) {
                            showAlert("Isikan Pesan");
                            cansave = false;
                        }

                        if (cansave) {
                            new KirimPesanFromUser().execute();
                        }
                    }
                }
        );

        sharedPreferences = getApplicationContext().getSharedPreferences("adamymas", 0); // 0 - for private mode
        editor = sharedPreferences.edit();
        confMenu();

        imageberitadetail = (ImageView) findViewById(R.id.imageberitadetail);
        imageberitadetail.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;;
        beritadetailkomentarimageview = (ImageView) findViewById(R.id.beritadetailkomentarimageview);
        beritadetailkomentarimageview.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        linerlayoutVerticalDAta = (LinearLayout) findViewById(R.id.linerlayoutVerticalDAta);

        closeLayoutsFirst();
        new GetPopulerData().execute();
        setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        timer = new Timer();
        myTimerTask = new MyTimerTask();
//        timer.schedule(myTimerTask, 0, 60000); // every minutes
        timer.schedule(myTimerTask, 0, 300000); // every minutes

        loginpassword = (com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.loginpassword);

        buttonShareBerita = (Button) findViewById(R.id.buttonShareBerita);

        ((ImageButton) findViewById(R.id.buttonSearchBeritaList)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetPopulerData().execute();
                    }
                }
        );
    }

    @Override
    public void onLocationChanged(Location location1) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private void confMenu() {
        multiLevelListView = (MultiLevelListView) findViewById(R.id.multiLevelMenu);

        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();

        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);
        if (baseItemList != null) {
            baseItemList.clear();
        }
        baseItemList = CustomDataProvider.getInitialItems(sharedPreferences);
        listAdapter.setDataItems(baseItemList);
    }

    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
            ImageView iconMenu;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mainActivity).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                viewHolder.iconMenu = (ImageView) convertView.findViewById(R.id.iconmenu);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            viewHolder.iconMenu.setBackgroundResource(((BaseItem) object).getImageMenu());

            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_expand_less : R.drawable.ic_expand_more);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }
    }

    private void tambahBeritaPrepare() {
        closeLayouts();
        beritaAddProgressbar.setVisibility(View.GONE);
        beritaAddImageimageView.setVisibility(View.GONE);
        fileUpload = null;
        imageAction = "";

        ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
        ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
        ((TextView) findViewById(R.id.idberitaedit)).setText("");
        beritAddHapusGambar.setVisibility(View.GONE);
        setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);

        int position = 0;
        while (position < beritaAddSpinner.getAdapter().getCount()) {
            if (beritaAddSpinner.getAdapter().getItem(position).toString().equalsIgnoreCase(beritaShow)) {
                beritaAddSpinner.setSelection(position);
                break;
            }
            position++;
        }
    }
    private void showPesanUser() {
        closeLayouts();
        setViewLayout((View) findViewById(R.id.pesanuser), View.VISIBLE);
        mainActivity.setTitle("Pesan");
        new GetPesanUser().execute();
    }
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            BaseItem baseItem = (BaseItem) object;
            if (baseItem.getFunctionName() != null) {
                setTitle(baseItem.getName());
                switch (baseItem.getFunctionName()) {
                    case "Populer" : showPopulerBeritaList("populer"); break;
                    case "Terbaru" : showPopulerBeritaList("terbaru"); break;

                    case "Umum" : showPopulerBeritaList("umum"); break;
                    case "Acara" : showPopulerBeritaList("acara"); break;
                    case "Pengaduan" : showPopulerBeritaList("pengaduan"); break;

                    case "Saya" :
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.profileuser), View.VISIBLE);
                        fileUpload = null;
                        imageAction = "";
                        new GetMyProfile().execute();
                        break;
                    case "Berita Saya" : showPopulerBeritaList("beritasaya"); break;
                    case "Tambah Berita" :
                        tambahBeritaPrepare();
                        break;
                    case "Pesan" : showPesanUser();
                        break;
                    case "Log Out" :
                        editor.clear();
                        editor.commit();
                        confMenu();
                        showPopulerBeritaList("populer");
                        setTitle("Populer");
                        break;
                    case "Log In" :
                        ((EditText) findViewById(R.id.loginusernamenik)).setText("");
                        loginpassword.setText("");

                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
                        loginProgressbar.setVisibility(View.GONE);
                        break;
                    case "Alamat Kami" :
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.alamatkantor), View.VISIBLE);
                        break;
                    case "Hubungi Kami" :
                        closeLayouts();
                        ((EditText) findViewById(R.id.emailTextKirimPesan)).setText("");
                        ((EditText) findViewById(R.id.judulTextKirimPesan)).setText("");
                        ((EditText) findViewById(R.id.pesanTextKirimPesan)).setText("");
                        setViewLayout((View) findViewById(R.id.kirimpesanform), View.VISIBLE);
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("viewlayout", viewLayout);
        savedInstanceState.putString("beritaShow", beritaShow);
    }

    private class PostBerita extends AsyncTask<String, Void, String> {
        private String judulBerita;
        private String deskripsi;
        private String kategori;
        private String idBerita;
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            if (fileUpload != null ) {
                String extension = "";
                int i = fileUpload.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileUpload.getName().substring(i + 1);
                    if (extension.length() > 0) {
                        BufferedReader inputStream = null;
                        URL myurl = null;
                        FTPClient con = null;
                        FileInputStream in = null;

                        try {

                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension)
                                    .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                                    .concat("&password=").concat(sharedPreferences.getString("password", "")));
                            URLConnection dc = myurl.openConnection();
                            inputStream = new BufferedReader(new InputStreamReader(
                                    dc.getInputStream()));
                            fileNameForUpload = inputStream.readLine();
                            if (fileNameForUpload.length() > 0) {
                                Bitmap bitmapImage = BitmapFactory.decodeFile(fileUpload.getAbsolutePath());
                                int height = bitmapImage.getHeight();
                                int width = bitmapImage.getWidth();
                                if (width > 277) {
                                    height = 277 * height / width;
                                    width = 277;
                                }
                                Bitmap bitmapReady = Bitmap.createScaledBitmap(bitmapImage, width, height, true);
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, fileNameForUpload);

                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(dest);
                                    bitmapReady.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    fileUpload = dest;
                                    con = new FTPClient();
                                    con.connect(PropertiesData.ftpdomain);
                                    if (con.login(PropertiesData.ftpusername, PropertiesData.ftppassword)) {
                                        con.enterLocalPassiveMode(); // important!
                                        con.setFileType(FTP.BINARY_FILE_TYPE);
                                        in = new FileInputStream(fileUpload);
                                        con.storeFile(fileUpload.getName(), in);
                                        con.logout();
                                    }
                                    dest.delete();
                                    out = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                    if (con != null) {
                                        con.disconnect();
                                    }
                                    if (in != null) {
                                        in.close();
                                    }
                                }
                            }

                        } catch (MalformedURLException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/berita-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("judul", judulBerita));
            nameValuePairs.add(new BasicNameValuePair("deskripsi", deskripsi));
            nameValuePairs.add(new BasicNameValuePair("kategori", kategori));
            nameValuePairs.add(new BasicNameValuePair("idberita", idBerita));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("filename", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            beritaAddProgressbar.setVisibility(View.GONE);
            if (idBerita.length() == 0) {
                ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
                ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
                beritaAddImageimageView.setVisibility(View.GONE);
            }
            fileUpload = null;
            imageAction = "";
            showAlert("Tersimpan");
        }

        @Override
        protected void onPreExecute() {
            beritaAddProgressbar.setVisibility(View.VISIBLE);
            judulBerita = ((EditText) findViewById(R.id.beritaaddJudulBerita)).getText().toString();
            deskripsi = ((EditText) findViewById(R.id.beritaaddDeskripsi)).getText().toString();
            kategori = beritaAddSpinner.getSelectedItem().toString();
            idBerita = ((TextView) findViewById(R.id.idberitaedit)).getText().toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, beritaAddImageimageView);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                    if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                        imageAction = "ubah";
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, beritaAddImageimageView);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                    if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                        imageAction = "ubah";
                    }
                }
                break;
            case 12:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, beritadetailkomentarimageview);
                    beritadetailkomentarimageview.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 13:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    beritadetailkomentarimageview.setImageBitmap(selectedImage);
                    beritadetailkomentarimageview.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                }
                break;
            case 7777:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    beritaAddImageimageView.setImageBitmap(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                }
                break;
            case 221:
                if(resultCode == RESULT_OK){
                    Uri imageUri = imageReturnedIntent.getData();
                    setImageFromGaleri(imageUri, imageViewAccess);
                    imageViewAccess.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(imageUri));
                    imageAction = "ubah";
                }
                break;
            case 222:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageViewAccess.setImageBitmap(selectedImage);
                    imageViewAccess.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                    imageAction = "ubah";
                }
                break;
            case 41:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, profileImage);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 42:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    profileImage.setImageBitmap(selectedImage);
                    setFileUploadCamera(selectedImage);
                }
                break;
            case 4432:
                if(resultCode == RESULT_OK){
                    Log.i("sh", "bisa share");
                }
        }
    }

    private void setImageFromGaleri(Uri imageUri, ImageView imageView) {
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream);

            byte [] data = stream.toByteArray();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), selectedImage);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bmp = drawable.getBitmap();
            Bitmap b = Bitmap.createScaledBitmap(bmp, selectedImage.getHeight() * 500 / selectedImage.getHeight(), 500, false);
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            Log.e("FileEx", e.getMessage());
        }
    }

    private void setFileUploadCamera(Bitmap selectedImage) {
        FileOutputStream out = null;
        File sd = Environment.getExternalStorageDirectory();
        fileUpload = new File(sd, randomString(11).concat(".jpg"));
        try {
            out = new FileOutputStream(fileUpload);
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            if (((TextView) findViewById(R.id.idberitaedit)).getText().length() > 0) {
                imageAction = "ubah";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String randomString( int len ){
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( secureRandom.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onPause();
    }

    private ArrayList<ImageText> _arrayImageTexts;
    private void getPopulerJson() {
        BufferedReader inputStream = null;
        URL myurl = null;
        String urlExecute = "";
        try {
            if (beritaShow.isEmpty() || beritaShow.equals("populer")) {
                urlExecute = PropertiesData.domain.concat("android/populer");
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    urlExecute = urlExecute.concat("?");
                } else {
                    urlExecute = urlExecute.concat("?usernamenik=")
                            .concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""));
                }
            } else if (beritaShow.equals("terbaru")) {
                urlExecute = PropertiesData.domain.concat("android/terbaru");
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    urlExecute = urlExecute.concat("?");
                } else {
                    urlExecute = urlExecute.concat("?usernamenik=")
                            .concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""));
                }
            } else if (beritaShow.equals("beritasaya")) {
                urlExecute = PropertiesData.domain.concat("android/beritasaya").concat("?usernamenik=")
                        .concat(sharedPreferences.getString("usernamenik", ""))
                        .concat("&password=").concat(sharedPreferences.getString("password", ""));
            } else {
                urlExecute = PropertiesData.domain.concat("android/artikel-").concat(beritaShow);
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    urlExecute = urlExecute.concat("?");
                } else {
                    urlExecute = urlExecute.concat("?usernamenik=")
                            .concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""));
                }
            }

            if (!((EditText) findViewById(R.id.searchTextListBerita)).getText().toString().isEmpty()) {
                if (!urlExecute.substring((urlExecute.length() - 1)).equalsIgnoreCase("?")) {
                    urlExecute = urlExecute.concat("&");
                }
                urlExecute = urlExecute.concat("pencarian=").concat(((EditText) findViewById(R.id.searchTextListBerita)).getText().toString());
            }
            myurl = new URL(urlExecute);
            URLConnection dc = myurl.openConnection();
            inputStream = new BufferedReader(new InputStreamReader(
                    dc.getInputStream()));
            String resultStream = inputStream.readLine();

            JSONArray jsonArray = new JSONArray(resultStream);

            _listImageText.clearList();
            _listImageTextRalat.clearList();
            _arrayImageTexts.clear();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    _arrayImageTexts.add(new ImageText(jsonObject.get("id").toString() ,jsonObject.get("judul").toString(), jsonObject.get("filename").toString()));

                }

            }
        } catch (MalformedURLException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } catch (IOException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } catch (JSONException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetPopulerData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            getPopulerJson();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            populerProgressBar.setVisibility(View.GONE);
            populerListView.setVisibility(View.VISIBLE);

            if (beritaShow.equals("beritasaya")) {
                populerListView.setAdapter(_listImageTextRalat);
            } else {
                populerListView.setAdapter(_listImageText);
            }

            if (!sharedPreferences.getString("usernamenik", "").equals("")) {
                populerTambahBeritaButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            populerProgressBar.setVisibility(View.VISIBLE);
            populerListView.setVisibility(View.GONE);
            populerTambahBeritaButton.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class registeruser extends AsyncTask<String, Void, String> {
        private String usernamenik;
        private String password;
        private String email;
        private String resultPostHTml;
        @Override
        protected String doInBackground(String... params) {
            resultPostHTml = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/mendaftar"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", usernamenik));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                BufferedReader rd = null;
                String body = "";
                try {
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((body = rd.readLine()) != null) {
                        resultPostHTml = resultPostHTml.concat(body);
                    }
                } catch (IOException e) {
                    Log.e("IOex1", e.getMessage());
                } finally {
                    if (rd != null) {
                        try {
                            rd.close();
                        } catch (IOException e) {
                            Log.e("IOex", e.getMessage());
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (resultPostHTml.isEmpty()) {
                    showAlert("Koneksi Internet Terputus");
                } else {
                    JSONObject jsonObject = new JSONObject(resultPostHTml);
                    if (jsonObject.getString("success").equals("1")) {
                        showAlert("User Tersimpan");
                        setViewLayout((View) findViewById(R.id.userregister), View.GONE);
                        setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
                    } else {
                        showAlert(jsonObject.getString("msg"));
                    }
                }
                findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
            } catch (JSONException e) {
                Log.e("JSONE", e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            email = ((EditText) findViewById(R.id.daftaremail)).getText().toString();
            usernamenik = ((EditText) findViewById(R.id.daftarusernamenik)).getText().toString();
            password = ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.daftarpassword)).getText().toString();
            findViewById(R.id.daftarprogressbar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private class loginuser extends AsyncTask<String, Void, String> {
        private String usernamenik;
        private String password;
        private JSONObject jsonObject;

        @Override
        protected String doInBackground(String... params) {
            String resultPostHTml = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/ceklogin"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", usernamenik));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                BufferedReader rd = null;
                String body = "";
                try {
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((body = rd.readLine()) != null) {
                        resultPostHTml = body;
                    }
                    if (resultPostHTml != null && !resultPostHTml.isEmpty()) {
                        jsonObject = new JSONObject(resultPostHTml);
                    }
                } catch (IOException e) {
                    Log.e("IOex1", e.getMessage());
                } catch (JSONException e) {
                    Log.e("json", e.getMessage());
                } finally {
                    if (rd != null) {
                        try {
                            rd.close();
                        } catch (IOException e) {
                            Log.e("IOex", e.getMessage());
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            loginProgressbar.setVisibility(View.GONE);
            if (jsonObject == null) {
                showAlert("Koneksi Internet Terputus");
            } else {
                try {
                    if (jsonObject.get("success").toString().equals("1")) {
                        showPesanUser();

                        editor.putString("usernamenik", usernamenik);
                        editor.putString("password", password);
                        editor.commit();

                        confMenu();
                    } else {
                        showAlert(jsonObject.get("msg").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            usernamenik = ((EditText) findViewById(R.id.loginusernamenik)).getText().toString();
            password = loginpassword.getText().toString();
            loginProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    //debug post html
    private void viewhtmlPost(HttpResponse response) {
        BufferedReader rd = null;
        String body = "";
        try {
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((body = rd.readLine()) != null)
            {
                Log.i("HttpResponse", body);
            }
        } catch (IOException e) {
            Log.e("IOex", e.getMessage());
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    Log.e("IOex", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewLayout.equalsIgnoreCase("beritadetail")) {
            closeLayouts();
            setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
        } else if (viewLayout.equalsIgnoreCase("beritaadd")) {
            closeLayouts();
            showPopulerBeritaList("beritasaya");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateSupportNavigateUpTaskStack(builder);
    }

    private void showPopulerBeritaList(String pilihan) {
        beritaShow = pilihan;
        closeLayouts();
        setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
        new GetPopulerData().execute();
    }

    private String getFileName(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void setViewLayout(View view, int status) {
        if (view.getVisibility() == View.VISIBLE) {
            if (status == View.GONE) {
                view.animate().translationX(view.getWidth() * 4);
            } else {
                view.animate().translationX(0);
            }
        } else if (status == View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

        if (status == View.VISIBLE) {
            switch (view.getId()) {
                case R.id.populer: viewLayout = "populer";
                    break;
                case R.id.beritadetail: viewLayout = "beritadetail";
                    break;
                case R.id.userregister: viewLayout = "userregister";
                    break;
                case R.id.userlogin: viewLayout = "userlogin";
                    break;
                case R.id.beritaadd: viewLayout = "beritaadd";
                    break;
                case R.id.profileuser: viewLayout = "profileuser";
                    break;
                case R.id.pesanuser: viewLayout = "pesanuser";
                    break;
                case R.id.alamatkantor: viewLayout = "alamatkantor";
                    break;
            }
        }
    }

    public void closeLayouts() {
        setViewLayout((View) findViewById(R.id.populer), View.GONE);
        setViewLayout((View) findViewById(R.id.beritadetail), View.GONE);
        setViewLayout((View) findViewById(R.id.beritaadd), View.GONE);
        setViewLayout((View) findViewById(R.id.userregister), View.GONE);
        setViewLayout((View) findViewById(R.id.userlogin), View.GONE);
        setViewLayout((View) findViewById(R.id.profileuser), View.GONE);
        setViewLayout((View) findViewById(R.id.pesanuser), View.GONE);
        setViewLayout((View) findViewById(R.id.alamatkantor), View.GONE);
        setViewLayout((View) findViewById(R.id.kirimpesanform), View.GONE);
    }

    public void closeLayoutsFirst() {
        findViewById(R.id.populer).setVisibility(View.GONE);
        findViewById(R.id.beritadetail).setVisibility(View.GONE);
        findViewById(R.id.beritaadd).setVisibility(View.GONE);
        findViewById(R.id.userregister).setVisibility(View.GONE);
        findViewById(R.id.userlogin).setVisibility(View.GONE);
        findViewById(R.id.profileuser).setVisibility(View.GONE);
        findViewById(R.id.pesanuser).setVisibility(View.GONE);
        findViewById(R.id.alamatkantor).setVisibility(View.GONE);
        findViewById(R.id.kirimpesanform).setVisibility(View.GONE);
    }

    private class GetBeritaDetail extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private String _id;
        private ArrayList<KomentarText> komentarTexts;

        public GetBeritaDetail(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id));
                } else {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id)
                            .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""))
                    );
                }

                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());

                JSONArray jsonArray = jsonObject.getJSONArray("komentars");
                komentarTexts = getKomentarTexts(jsonArray);
            } catch (MalformedURLException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (jsonObject == null) {
                    beritaDetailProgressBar.setVisibility(View.GONE);
                    imageberitadetail.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.judulBeritaDetail)).setText("");
                    ((TextView) findViewById(R.id.deskripsiberitadetail)).setText("");
                    ((TextView) findViewById(R.id.dateberitadetail)).setText("");
                    ((TextView) findViewById(R.id.beritadetailidberita)).setText("");
                    showAlert("Koneksi Internet Terputus");
                } else {
                    ((TextView) findViewById(R.id.judulBeritaDetail)).setText(jsonObject.get("judul").toString());
                    ((TextView) findViewById(R.id.deskripsiberitadetail)).setText(Html.fromHtml(jsonObject.get("deskripsi").toString()));
                    ((TextView) findViewById(R.id.dateberitadetail)).setText(jsonObject.get("tanggal").toString());
                    ((TextView) findViewById(R.id.beritadetailidberita)).setText(_id);

                    if (jsonObject.get("filename").toString().length() > 0) {
                        imageberitadetail.setImageBitmap(BitmapFactory.decodeStream((InputStream) new URL(jsonObject.get("filename").toString()).getContent()));
                        imageberitadetail.setVisibility(View.VISIBLE);
                        Glide.with(imageberitadetail.getContext())
                                .load(jsonObject.get("filename").toString())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        beritaDetailProgressBar.setVisibility(View.GONE);
                                        setKomentar(komentarTexts);
                                        return false;
                                    }
                                })
                                .into(imageberitadetail);
                    } else {
                        imageberitadetail.setVisibility(View.GONE);
                        beritaDetailProgressBar.setVisibility(View.GONE);
                        setKomentar(komentarTexts);
                    }
                    buttonShareBerita.setOnClickListener(null);
                    buttonShareBerita.setVisibility(View.VISIBLE);
                    buttonShareBerita.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                                    share.setType("text/plain");
                                    try {
                                        share.putExtra(Intent.EXTRA_SUBJECT, jsonObject.get("judul").toString());
                                        share.putExtra(Intent.EXTRA_TEXT, PropertiesData.domain +"beritadetail-" + jsonObject.get("id") + "-artikel-"+ jsonObject.get("kategori"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivityForResult(Intent.createChooser(share, "Surabaya Digital City"), 4432);
                                    if (!sharedPreferences.getString("usernamenik", "").isEmpty()) {
                                        new AddShareBeritaUser().execute();
                                    }

                                }
                            }
                    );
                    findViewById(R.id.layoutberitadetailuser).setVisibility(View.VISIBLE);

                    JSONObject user1JsonObject = (JSONObject) jsonObject.get("user1");
                    ((TextView) findViewById(R.id.profileNameUserBerita)).setText(user1JsonObject.get("name").toString());
                    if (user1JsonObject.get("gambar").toString().isEmpty()) {
                        findViewById(R.id.profileUserBerita).setVisibility(View.GONE);
                    } else {
                        CircleImageView userCircleImageView = (CircleImageView) findViewById(R.id.profileUserBerita);
                        userCircleImageView.setImageResource(R.drawable.ic_person_blue_24dp);
                        userCircleImageView.setVisibility(View.VISIBLE);
                        Glide.with(userCircleImageView.getContext())
                                .load(user1JsonObject.get("gambar").toString())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(userCircleImageView);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                bertiaDetailAddKomentar.setVisibility(View.GONE);
            } else {
                bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            ((TextView) findViewById(R.id.judulBeritaDetail)).setText("");
            ((TextView) findViewById(R.id.deskripsiberitadetail)).setText("");
            ((TextView) findViewById(R.id.dateberitadetail)).setText("");
            imageberitadetail.setVisibility(View.GONE);
            closeLayouts();
            setViewLayout((View) findViewById(R.id.beritadetail), View.VISIBLE);
            beritaDetailProgressBar.setVisibility(View.VISIBLE);

            findViewById(R.id.beritadetailkomentarform).setVisibility(View.GONE);
            findViewById(R.id.layoutberitadetailuser).setVisibility(View.GONE);
            buttonShareBerita.setVisibility(View.GONE);
            bertiaDetailAddKomentar.setVisibility(View.GONE);
            linerlayoutVerticalDAta.removeAllViews();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setKomentar(ArrayList<KomentarText> komentarTexts) {
        linerlayoutVerticalDAta.removeAllViews();
        if (komentarTexts != null) {
            for (KomentarText komentarText : komentarTexts) {
                LayoutInflater inflater = mainActivity.getLayoutInflater();
                View rowView = inflater.inflate(R.layout.listkomentar, null, true);

                ((TextView) rowView.findViewById(R.id.profileNameKomen)).setText(komentarText.getName());
                ImageView profileUserImage = (ImageView) rowView.findViewById(R.id.profileUserImage);
                if (komentarText.getUsersgambar().isEmpty()) {
                    profileUserImage.setVisibility(View.GONE);
                } else {
                    profileUserImage.setVisibility(View.VISIBLE);
                    Glide.with(profileUserImage.getContext())
                            .load(komentarText.getUsersgambar())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(profileUserImage);
                }

                ((TextView) rowView.findViewById(R.id.komentar)).setText(komentarText.getKomentar());
                ImageView imageKomentar = (ImageView) rowView.findViewById(R.id.imagekomentar);
                ((TextView) rowView.findViewById(R.id.komentarid)).setText(komentarText.getId());

                if (komentarText.getImage().length() > 0) {
                    imageKomentar.setVisibility(View.VISIBLE);
                    ((TextView) rowView.findViewById(R.id.imagelinkkomentarralat)).setText(komentarText.getImage());
                    Glide.with(imageKomentar.getContext())
                            .load(komentarText.getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imageKomentar);
                } else {
                    imageKomentar.setVisibility(View.GONE);
                }

                ((TextView) rowView.findViewById(R.id.komentar)).setText(komentarText.getKomentar());

                if (komentarText.getIsAccess().equals("1")) {
                    rowView.findViewById(R.id.ralatlayout).setVisibility(View.VISIBLE);
                    ((Button) rowView.findViewById(R.id.hapuskomentar)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LinearLayout linearLayout = (LinearLayout) v.getParent().getParent();
                                    LinearLayout linearLayoutParent = (LinearLayout) v.getParent().getParent().getParent();
                                    idKomentarDeleted = ((TextView) linearLayoutParent.findViewById(R.id.komentarid)).getText().toString();
                                    new AlertDialog.Builder(mainActivity)
                                            .setTitle("Yakin Akan Dihapus")
                                            .setMessage("Apakah Komentar ".concat(((TextView) linearLayout.findViewById(R.id.komentar)).getText().toString()).concat(" akan dihapus ?"))
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    new DeleteKomentar(idKomentarDeleted).execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null).show();
                                }
                            }
                    );
                    ((Button) rowView.findViewById(R.id.ralatkomentar)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fileUpload = null;
                                    imageAction = "";
                                    LinearLayout linearLayout = (LinearLayout) v.getParent().getParent();
                                    RelativeLayout relativeLayoutRalat = (RelativeLayout) linearLayout.findViewById(R.id.komentarformralat);
                                    relativeLayoutRalat.findViewById(R.id.komentarformralat).setVisibility(View.VISIBLE);

                                    bertiaDetailAddKomentar.setVisibility(View.GONE);
                                    linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.GONE);
                                    RelativeLayout ralatLayout = (RelativeLayout) linearLayout.findViewById(R.id.ralatlayout);
                                    ralatLayout.getLayoutParams().height = 0;
                                    ralatLayout.setVisibility(View.GONE);
                                    LinearLayout layoutkomentar = (LinearLayout) linearLayout.findViewById(R.id.layoutkomentar);
                                    layoutkomentar.getLayoutParams().height = 0;
                                    layoutkomentar.setVisibility(View.GONE);

                                    ((EditText) linearLayout.findViewById(R.id.komentaredittextralat)).setText(((TextView) linearLayout.findViewById(R.id.komentar)).getText());

                                    TextView imageLink = ((TextView) linearLayout.findViewById(R.id.imagelinkkomentarralat));
                                    ImageView imageRalatKomentar = ((ImageView) linearLayout.findViewById(R.id.komentarralatimage));
                                    if (imageLink.getText().length() > 0) {
                                        imageRalatKomentar.setVisibility(View.VISIBLE);
                                        Glide.with(imageRalatKomentar.getContext())
                                                .load(imageLink.getText().toString())
                                                .placeholder(R.drawable.ic_local_florist_black_24dp)
                                                .listener(new RequestListener<String, GlideDrawable>() {
                                                    @Override
                                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                        return false;
                                                    }
                                                })
                                                .into(imageRalatKomentar);
                                        imageRalatKomentar.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                                        linearLayout.findViewById(R.id.komentarlayouthapusgambar).setVisibility(View.VISIBLE);
                                    } else {
                                        imageRalatKomentar.setVisibility(View.GONE);
                                        linearLayout.findViewById(R.id.komentarlayouthapusgambar).setVisibility(View.GONE);
                                    }
                                }
                            });

                    ((Button) rowView.findViewById(R.id.komentarralatbatal)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fileUpload = null;
                                    imageAction = "";
                                    LinearLayout linearLayout = (LinearLayout) v.getParent().getParent().getParent();

                                    RelativeLayout ralatLayout = (RelativeLayout) linearLayout.findViewById(R.id.ralatlayout);
                                    ralatLayout.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                                    ralatLayout.setVisibility(View.VISIBLE);
                                    LinearLayout layoutkomentar = (LinearLayout) linearLayout.findViewById(R.id.layoutkomentar);
                                    layoutkomentar.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    layoutkomentar.setVisibility(View.VISIBLE);
                                    bertiaDetailAddKomentar.setVisibility(View.VISIBLE);

                                    linearLayout.findViewById(R.id.komentarformralat).setVisibility(View.GONE);
                                }
                            });

                    ((Button) rowView.findViewById(R.id.komentarralatgaleributton)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 221);
                                }
                            });

                    ((Button) rowView.findViewById(R.id.komentarlayouthapusgambar)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                                    imageViewAccess.setVisibility(View.GONE);
                                    imageAction = "hapus";
                                }
                            });

                    ((Button) rowView.findViewById(R.id.komentarralatkamerabutton)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 222);
                                }
                            });
                    ((Button) rowView.findViewById(R.id.komentarralatsimpan)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new PostRalatKomentar(((LinearLayout) v.getParent().getParent().getParent().getParent())).execute();
                                }
                            });

                    ProgressBar komentarRalatProgressbar = (ProgressBar) rowView.findViewById(R.id.komentarralatprogressbar);
                    komentarRalatProgressbar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
                    komentarRalatProgressbar.getLayoutParams().height = 60;
                } else {
                    rowView.findViewById(R.id.ralatlayout).setVisibility(View.GONE);
                }
                rowView.findViewById(R.id.komentarformralat).setVisibility(View.GONE);
                linerlayoutVerticalDAta.addView(rowView);
            }
        }
    }

    private class DeleteKomentar extends AsyncTask<String, Void, String> {
        private String id;
        public DeleteKomentar(String idKomentar) {
            super();
            id = idKomentar;
        }
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-deleted"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("idkomentar", id));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                viewhtmlPost(httpclient.execute(httpPost));
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new GetKomentar().execute();
            showAlert("Komentar Terhapus");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetKomentar extends AsyncTask<String, Void, String> {
        private JSONArray jsonArray;
        private String _id;
        private ArrayList<KomentarText> komentarTexts;

        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    myurl = new URL(PropertiesData.domain.concat("android/komentar-data-").concat(_id));
                } else {
                    myurl = new URL(PropertiesData.domain.concat("android/komentar-data-").concat(_id)
                            .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""))
                    );
                }
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonArray = new JSONArray(inputStream.readLine());
                komentarTexts = getKomentarTexts(jsonArray);
            } catch (MalformedURLException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            setKomentar(komentarTexts);
            ((ScrollView) findViewById(R.id.beritadetail)).requestChildFocus(findViewById(R.id.beritadetaillastcomponent),
                    findViewById(R.id.beritadetaillastcomponent));
        }

        @Override
        protected void onPreExecute() {
            _id = ((EditText) findViewById(R.id.beritadetailidberita)).getText().toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private ArrayList<KomentarText> getKomentarTexts(JSONArray jsonArray) {
        ArrayList<KomentarText> komentarTexts = new ArrayList<KomentarText>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObjectKomentar = jsonArray.getJSONObject(i);
                komentarTexts.add(new KomentarText(jsonObjectKomentar.get("komentar").toString(),
                        jsonObjectKomentar.get("gambar").toString(),
                        jsonObjectKomentar.get("id").toString(),
                        jsonObjectKomentar.get("useridinput").toString(),
                        jsonObjectKomentar.get("idberita").toString(),
                        jsonObjectKomentar.get("isaccess").toString(),
                        jsonObjectKomentar.get("name").toString(),
                        jsonObjectKomentar.get("usersgambar").toString()
                ));
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }
        }
        return komentarTexts;
    }

    private class DeleteBerita extends AsyncTask<String, Void, String> {
        private String _id;
        public DeleteBerita(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/berita-deleted"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("id", _id));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            showPopulerBeritaList("beritasaya");
            showAlert("Berita Terhapus");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void  openBeritaDetailAsync(String id) {
        new GetBeritaDetail(id).execute();
    }
    public void deleteBerita(String id) {
        new DeleteBerita(id).execute();
    }
    public void prePareEditBerita(String selectedID) {
        new Get1Berita(selectedID).execute();
    }

    private class Get1Berita extends AsyncTask<String, Void, String> {
        private ImageText imageText;
        private String _id;
        public Get1Berita(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                if (sharedPreferences.getString("usernamenik", "").equals("")) {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id));
                } else {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id)
                            .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", "")));
                }
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                JSONObject jsonObject = new JSONObject(inputStream.readLine());
                imageText = new ImageText(_id, jsonObject.get("judul").toString(), jsonObject.get("filename").toString()
                        , jsonObject.get("deskripsi").toString(), jsonObject.get("kategori").toString());
            } catch (MalformedURLException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (imageText == null) {
                showAlert("Koneksi Internet Terputus");
            } else {
                ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText(imageText.getJudul());
                ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText(Html.fromHtml(imageText.getBerita()));
                ((TextView) findViewById(R.id.idberitaedit)).setText(imageText.getId());
                int position = 0;
                while (position < beritaAddSpinner.getAdapter().getCount()) {
                    if (beritaAddSpinner.getAdapter().getItem(position).toString().equalsIgnoreCase(imageText.getKategori())) {
                        beritaAddSpinner.setSelection(position);
                        break;
                    }
                    position++;
                }

                if (imageText.getImage() != null && imageText.getImage().length() > 0) {
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    beritAddHapusGambar.setVisibility(View.VISIBLE);
                    Glide.with(beritaAddImageimageView.getContext())
                            .load(imageText.getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    beritaAddProgressbar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(beritaAddImageimageView);
                } else {
                    beritaAddProgressbar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            closeLayouts();
            imageAction = "";
            beritaAddProgressbar.setVisibility(View.VISIBLE);
            setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);

            beritaAddImageimageView.setVisibility(View.GONE);
            beritAddHapusGambar.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.idberitaedit)).setText("");
            fileUpload = null;
            imageAction = "";

            ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
            ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
            ((TextView) findViewById(R.id.idberitaedit)).setText("");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PostKomentar extends AsyncTask<String, Void, String> {
        private String komentar;
        private String idberita;
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            if (fileUpload != null ) {
                String extension = "";
                int i = fileUpload.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileUpload.getName().substring(i + 1);
                    if (extension.length() > 0) {
                        BufferedReader inputStream = null;
                        URL myurl = null;
                        FTPClient con = null;
                        FileInputStream in = null;

                        try {

                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension)
                                    .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                                    .concat("&password=").concat(sharedPreferences.getString("password", "")));
                            URLConnection dc = myurl.openConnection();
                            inputStream = new BufferedReader(new InputStreamReader(
                                    dc.getInputStream()));
                            fileNameForUpload = inputStream.readLine();
                            if (fileNameForUpload.length() > 0) {
                                Bitmap bitmapImage = BitmapFactory.decodeFile(fileUpload.getAbsolutePath());
                                int height = bitmapImage.getHeight();
                                int width = bitmapImage.getWidth();
                                if (width > 277) {
                                    height = 277 * height / width;
                                    width = 277;
                                }
                                Bitmap bitmapReady = Bitmap.createScaledBitmap(bitmapImage, width, height, true);
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, fileNameForUpload);

                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(dest);
                                    bitmapReady.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    fileUpload = dest;
                                    con = new FTPClient();
                                    con.connect(PropertiesData.ftpdomain);
                                    if (con.login(PropertiesData.ftpusername, PropertiesData.ftppassword)) {
                                        con.enterLocalPassiveMode(); // important!
                                        con.setFileType(FTP.BINARY_FILE_TYPE);
                                        in = new FileInputStream(fileUpload);
                                        con.storeFile(fileUpload.getName(), in);
                                        con.logout();
                                    }
                                    dest.delete();
                                    out = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                    if (con != null) {
                                        con.disconnect();
                                    }
                                    if (in != null) {
                                        in.close();
                                    }
                                }
                            }

                        } catch (MalformedURLException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idkomentar", ""));
            nameValuePairs.add(new BasicNameValuePair("komentar", komentar));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("gambar", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("idberita", idberita));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressbarKomentarAdd.setVisibility(View.GONE);
            findViewById(R.id.beritadetailkomentarform).setVisibility(View.GONE);
            if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                bertiaDetailAddKomentar.setVisibility(View.GONE);
            } else {
                bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            }
            showAlert("Tersimpan");
            new GetKomentar().execute();
            fileUpload = null;
            imageAction = "";
        }

        @Override
        protected void onPreExecute() {
            komentar = ((EditText) findViewById(R.id.beritadetailkomentar)).getText().toString();
            idberita = ((EditText) findViewById(R.id.beritadetailidberita)).getText().toString();
            progressbarKomentarAdd.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PostRalatKomentar extends AsyncTask<String, Void, String> {
        private String komentar;
        private String idKomentar;
        private LinearLayout linearLayout;
        private PostRalatKomentar(LinearLayout linearLayout1) {
            super();
            linearLayout = linearLayout1;
        }
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            if (fileUpload != null ) {
                String extension = "";
                int i = fileUpload.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileUpload.getName().substring(i + 1);
                    if (extension.length() > 0) {
                        BufferedReader inputStream = null;
                        URL myurl = null;
                        FTPClient con = null;
                        FileInputStream in = null;

                        try {

                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension)
                                    .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                                    .concat("&password=").concat(sharedPreferences.getString("password", "")));
                            URLConnection dc = myurl.openConnection();
                            inputStream = new BufferedReader(new InputStreamReader(
                                    dc.getInputStream()));
                            fileNameForUpload = inputStream.readLine();
                            if (fileNameForUpload.length() > 0) {
                                Bitmap bitmapImage = BitmapFactory.decodeFile(fileUpload.getAbsolutePath());
                                int height = bitmapImage.getHeight();
                                int width = bitmapImage.getWidth();
                                if (width > 277) {
                                    height = 277 * height / width;
                                    width = 277;
                                }
                                Bitmap bitmapReady = Bitmap.createScaledBitmap(bitmapImage, width, height, true);
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, fileNameForUpload);

                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(dest);
                                    bitmapReady.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    fileUpload = dest;
                                    con = new FTPClient();
                                    con.connect(PropertiesData.ftpdomain);
                                    if (con.login(PropertiesData.ftpusername, PropertiesData.ftppassword)) {
                                        con.enterLocalPassiveMode(); // important!
                                        con.setFileType(FTP.BINARY_FILE_TYPE);
                                        in = new FileInputStream(fileUpload);
                                        con.storeFile(fileUpload.getName(), in);
                                        con.logout();
                                    }
                                    dest.delete();
                                    out = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                    if (con != null) {
                                        con.disconnect();
                                    }
                                    if (in != null) {
                                        in.close();
                                    }
                                }
                            }

                        } catch (MalformedURLException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idkomentar", idKomentar));
            nameValuePairs.add(new BasicNameValuePair("komentar", komentar));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("gambar", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.GONE);
            new GetKomentar().execute();
            bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            fileUpload = null;
            imageAction = "";
        }

        @Override
        protected void onPreExecute() {
            idKomentar = ((TextView)linearLayout.findViewById(R.id.komentarid)).getText().toString();
            komentar = ((EditText)linearLayout.findViewById(R.id.komentaredittextralat)).getText().toString();
            linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetMyProfile extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                myurl = new URL(PropertiesData.domain.concat("android/profileuser").concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                        .concat("&password=").concat(sharedPreferences.getString("password", "")));
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());
            } catch (MalformedURLException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (jsonObject == null) {
                    profileProgressBar.setVisibility(View.GONE);
                    showAlert("Koneksi Internet Terputus");
                } else {
                    ((EditText) findViewById(R.id.profileNama)).setText(jsonObject.get("name").toString());
                    ((EditText) findViewById(R.id.profileNik)).setText(jsonObject.get("nik").toString());
                    ((EditText) findViewById(R.id.profileEmail)).setText(jsonObject.get("email").toString());
                    String imageLink = jsonObject.get("gambar").toString();
                    if (imageLink.isEmpty()) {
                        profileProgressBar.setVisibility(View.GONE);
                    } else {
                        Glide.with(beritaAddImageimageView.getContext())
                                .load(imageLink)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        profileProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(profileImage);
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            ((EditText) findViewById(R.id.profileNama)).setText("");
            ((EditText) findViewById(R.id.profileNik)).setText("");
            ((EditText) findViewById(R.id.profileEmail)).setText("");
            ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.profilePassword)).setText("");
            ((EditText) findViewById(R.id.profileRePassword)).setText("");
            profileProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class EditMyProfile extends AsyncTask<String, Void, String> {
        String name;
        String nik;
        String email;
        private JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            if (fileUpload != null ) {
                String extension = "";
                int i = fileUpload.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileUpload.getName().substring(i + 1);
                    if (extension.length() > 0) {
                        BufferedReader inputStream = null;
                        URL myurl = null;
                        FTPClient con = null;
                        FileInputStream in = null;

                        try {

                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension)
                                    .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                                    .concat("&password=").concat(sharedPreferences.getString("password", "")));
                            URLConnection dc = myurl.openConnection();
                            inputStream = new BufferedReader(new InputStreamReader(
                                    dc.getInputStream()));
                            fileNameForUpload = inputStream.readLine();
                            if (fileNameForUpload.length() > 0) {
                                Bitmap bitmapImage = BitmapFactory.decodeFile(fileUpload.getAbsolutePath());
                                int height = bitmapImage.getHeight();
                                int width = bitmapImage.getWidth();
                                if (width > 277) {
                                    height = 277 * height / width;
                                    width = 277;
                                }
                                Bitmap bitmapReady = Bitmap.createScaledBitmap(bitmapImage, width, height, true);
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, fileNameForUpload);

                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(dest);
                                    bitmapReady.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    fileUpload = dest;
                                    con = new FTPClient();
                                    con.connect(PropertiesData.ftpdomain);
                                    if (con.login(PropertiesData.ftpusername, PropertiesData.ftppassword)) {
                                        con.enterLocalPassiveMode(); // important!
                                        con.setFileType(FTP.BINARY_FILE_TYPE);
                                        in = new FileInputStream(fileUpload);
                                        con.storeFile(fileUpload.getName(), in);
                                        con.logout();
                                    }
                                    dest.delete();
                                    out = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                    if (con != null) {
                                        con.disconnect();
                                    }
                                    if (in != null) {
                                        in.close();
                                    }
                                }
                            }

                        } catch (MalformedURLException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/profileuseredit"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("nik", nik));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("gambar", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                String resultPostHTml = "";
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                BufferedReader rd = null;
                String body = "";
                try {
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((body = rd.readLine()) != null) {
                        resultPostHTml = body;
                    }
                    if (resultPostHTml != null && !resultPostHTml.isEmpty()) {
                        jsonObject = new JSONObject(resultPostHTml);
                    }
                } catch (IOException e) {
                    Log.e("IOex1", e.getMessage());
                } catch (JSONException e) {
                    Log.e("js1on", e.getMessage());
                } finally {
                    if (rd != null) {
                        try {
                            rd.close();
                        } catch (IOException e) {
                            Log.e("IOex", e.getMessage());
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            fileUpload = null;
            profileProgressBar.setVisibility(View.GONE);
            if (jsonObject == null) {
                showAlert("Koneksi Internet Terputus");
            } else {
                try {
                    if (jsonObject.get("success").toString().equalsIgnoreCase("1")) {
                        showAlert("Profile Tersimpan");
                    } else {
                        showAlert(jsonObject.get("msg").toString());
                    }
                } catch (JSONException e) {
                    Log.e("json", e.getMessage());
                }
            }

        }

        @Override
        protected void onPreExecute() {
            name = ((EditText) findViewById(R.id.profileNama)).getText().toString();
            nik = ((EditText) findViewById(R.id.profileNik)).getText().toString();
            email = ((EditText) findViewById(R.id.profileEmail)).getText().toString();
            profileProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GantiPasswordProfileProfile extends AsyncTask<String, Void, String> {
        String password;
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/profilegantipassword"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("passwordchange", password));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            findViewById(R.id.profileGantiPasswordProgressBar).setVisibility(View.GONE);
            showAlert("Password berubah");
        }

        @Override
        protected void onPreExecute() {
            password = ((com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText) findViewById(R.id.profilePassword)).getText().toString();
            findViewById(R.id.profileGantiPasswordProgressBar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PostLokasi extends AsyncTask<String, Void, String> {
        String langitude;
        String longitude;
        Location location;

        public PostLokasi(Location location1) {
            location = location1;
        }
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/lokasiadd"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("langitude", langitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
            langitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setLokasi() {
        Location location = null;
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location == null) {
            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            new PostLokasi(location).execute();
        }
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    setLokasi();
                }});
        }

    }

    private void showAlert(String alert) {
        Toast toast = Toast.makeText(MainActivity.this,alert,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private class AddShareBeritaUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/addshareberitauser"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetPesanUser extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                myurl = new URL(PropertiesData.domain.concat("android/getpesanafterlogin")
                        .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                        .concat("&password=").concat(sharedPreferences.getString("password", "")));
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());
                pesanUsers.clear();
                pesanUsers.add(new PesanUser(Html.fromHtml(jsonObject.get("pesan").toString()).toString()));

                JSONArray jsonArray = new JSONArray(jsonObject.get("pesanpribadi").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectRow = jsonArray.getJSONObject(i);
                    pesanUsers.add(new PesanUser(jsonObjectRow.get("id").toString(), jsonObjectRow.get("judul").toString(), jsonObjectRow.get("pesan").toString()));
                }

            } catch (MalformedURLException e) {
                Log.e("e4r", e.getMessage());
            } catch (IOException e) {
                Log.e("e4g7gr", e.getMessage());
            } catch (JSONException e) {
                Log.e("e443gr", e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("d5e4r", e.getMessage());
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (jsonObject == null) {
                showAlert("Koneksi Internet Terputus");
            } else {
                pesanList.notifyDataSetInvalidated();
            }
            progressBarPesanUser.setVisibility(View.GONE);
            findViewById(R.id.listpesan).setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            progressBarPesanUser.setVisibility(View.VISIBLE);
            findViewById(R.id.listpesan).setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class DeletePesanUser extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private String id;
        public DeletePesanUser(String id1) {
            super();
            id = id1;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/deletepesanuser"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("id", id));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void deletePesanUser(String id) {
        new DeletePesanUser(id).execute();
    }

    private class KirimPesanFromUser extends AsyncTask<String, Void, String> {
        private String judul;
        private String pesan;
        private String email;

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/kirimpesancs"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("judul", judul));
            nameValuePairs.add(new BasicNameValuePair("pesan", pesan));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            kirimPesanprogressBar.setVisibility(View.GONE);
            showAlert("Pesan terkirim");
        }

        @Override
        protected void onPreExecute() {
            email = ((EditText) findViewById(R.id.emailTextKirimPesan)).getText().toString();
            judul = ((EditText) findViewById(R.id.judulTextKirimPesan)).getText().toString();
            pesan = ((EditText) findViewById(R.id.pesanTextKirimPesan)).getText().toString();
            kirimPesanprogressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
