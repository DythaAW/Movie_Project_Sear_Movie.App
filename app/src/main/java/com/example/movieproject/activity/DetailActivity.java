package com.example.movieproject.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.movieproject.R;
import com.example.movieproject.helper.FavoriteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.movieproject.models.FavoriteModel;
import com.example.movieproject.stackwidget.ImageBannerWidget;

import static com.example.movieproject.BuildConfig.URL_POSTER;

public class DetailActivity extends AppCompatActivity {

    public static String EXTRA_ID = "extra_id";
    public static String EXTRA_TITLE = "extra_title";
    public static String EXTRA_TIME = "extra_time";
    public static String EXTRA_POSTER = "extra_poster";
    public static String IS_FAVORITE = "is_favorite";
    public static String EXTRA_OVERVIEW = "extra_overview";


    @BindView(R.id.title) TextView tvTitle;
    @BindView(R.id.overview) TextView tvOverview;
    @BindView(R.id.time) TextView tvTime;
    @BindView(R.id.poster) ImageView imgPoster;
    @BindView(R.id.btn_favorite) Button btnFovorite;

    Context context;
    private FavoriteHelper favoriteHelper;
    private boolean isFavorite = false;
    private int favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String overview = getIntent().getStringExtra(EXTRA_OVERVIEW);
        String time = getIntent().getStringExtra(EXTRA_TIME);
        String poster = getIntent().getStringExtra(EXTRA_POSTER);
        tvTitle.setText(title);
        tvOverview.setText(overview);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d, yyyy");
            String formattedDate = formatter.format(date);
            tvTime.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(URL_POSTER+poster).into(imgPoster);



        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        favorite = getIntent().getIntExtra(IS_FAVORITE, 0);
        if (favorite == 1){
            isFavorite = true;
            btnFovorite.setText("hapus favorite");
        }

        btnFovorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite){
                    addFavorit();
                    Toast.makeText(DetailActivity.this, "Favorited", Toast.LENGTH_LONG).show();
                }else {
                    deleteFavorite();
                    Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }


    private void addFavorit(){
        FavoriteModel favorites = new FavoriteModel();
        favorites.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        favorites.setOverview(getIntent().getStringExtra(EXTRA_OVERVIEW));
        favorites.setRelease_date(getIntent().getStringExtra(EXTRA_TIME));
        favorites.setPoster(getIntent().getStringExtra(EXTRA_POSTER));
        favoriteHelper.insert(favorites);


        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ImageBannerWidget.class));
        for (int id : widgetIDs)
            AppWidgetManager.getInstance(getApplication()).notifyAppWidgetViewDataChanged(id, R.id.stack_view);
    }

    private void deleteFavorite(){
        favoriteHelper.delete(getIntent().getIntExtra(EXTRA_ID, 0));

        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ImageBannerWidget.class));
        for (int id : widgetIDs)
            AppWidgetManager.getInstance(getApplication()).notifyAppWidgetViewDataChanged(id, R.id.stack_view);
        finish();
    }

}
