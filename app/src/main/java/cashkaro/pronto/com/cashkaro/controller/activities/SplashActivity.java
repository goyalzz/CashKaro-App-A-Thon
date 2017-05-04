package cashkaro.pronto.com.cashkaro.controller.activities;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.InjectView;
import cashkaro.pronto.com.cashkaro.R;
import cashkaro.pronto.com.cashkaro.controller.base.BaseActivity;

/**
 * Created by Ankush Goyal on 17/04/17.
 */

public class SplashActivity extends BaseActivity {

    @InjectView(R.id.logo)
    ImageView _logo;

    @Override
    protected void onStart() {
        super.onStart();

        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(this, R.anim.anticipate_overshoot);
        _logo.startAnimation(animAnticipateOvershoot);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }, 2000);

    }

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_splash);
    }
}
