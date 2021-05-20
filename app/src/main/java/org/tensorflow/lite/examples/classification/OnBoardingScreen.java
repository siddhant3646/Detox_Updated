package org.tensorflow.lite.examples.classification;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class OnBoardingScreen extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsgetstarted,next;
    Animation animation;
    int currentpos;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(OnBoardingScreen.this, home.class));
        }
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsgetstarted = findViewById(R.id.get_started_btn);
        letsgetstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(OnBoardingScreen.this, before_regis.class);
                startActivity(i);
                finish();
            }
        });
        next = findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentpos + 1);
            }
        });
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    private void addDots(int position)
    {
        dots = new TextView[8];
        dotsLayout.removeAllViews();
        for(int i =0;i<3;i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.colorred));
        }
    }
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentpos = position;
            if(position == 0)
            {
                letsgetstarted.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            }
            if(position == 1)
            {
                letsgetstarted.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            }
            if(position == 2)
            {
                animation = AnimationUtils.loadAnimation(OnBoardingScreen.this,R.anim.fade);
                letsgetstarted.setAnimation(animation);
                letsgetstarted.setVisibility(View.VISIBLE);
                next.setVisibility(View.INVISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}