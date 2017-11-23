package com.example.pc.mythreaddownload;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.mythreaddownload.gen.DaoMaster;
import com.example.pc.mythreaddownload.gen.DaoSession;
import com.example.pc.mythreaddownload.gen.PersonDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SeekBar mSb;
    /**
     * 开始下载
     */
    private Button mBtDownload;
    /**
     * 暂停下载
     */
    private Button mBtPause;
    /**
     * 继续下载
     */
    private Button mBtRestart;
    /**
     * 取消下载
     */
    private Button mBtStop;
    /**
     * 下载0%
     */
    private TextView mTv;

    private boolean b = true;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private PersonDao mPersonDao;


    DownLoadFile downLoadFile;
    private String loadUrl = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
    private String filePath = Environment.getExternalStorageDirectory() + "/" + "网易云音乐.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //初始化数据库
        openDb();

        downLoadFile = new DownLoadFile(this, loadUrl, filePath, 3);
        downLoadFile.setOnDownLoadListener(new DownLoadFile.DownLoadListener() {
            @Override
            public void getProgress(int progress) {
                mSb.setProgress(progress);
                mTv.setText("当前进度 ："+progress+" %");
            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                b = true;
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //初始化数据库
    private void openDb() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "person.db", null);
        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
        mPersonDao = mDaoSession.getPersonDao();
    }

    //插入
    public void insert(String progress){
        Person person = new Person(progress);
        mPersonDao.insert(person);
    }

    private void initView() {
        mSb = (SeekBar) findViewById(R.id.sb);
        mBtDownload = (Button) findViewById(R.id.bt_download);
        mBtDownload.setOnClickListener(this);
        mBtPause = (Button) findViewById(R.id.bt_pause);
        mBtPause.setOnClickListener(this);
        mBtRestart = (Button) findViewById(R.id.bt_restart);
        mBtRestart.setOnClickListener(this);
        mBtStop = (Button) findViewById(R.id.bt_stop);
        mBtStop.setOnClickListener(this);
        mTv = (TextView) findViewById(R.id.tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_download:

                if(b){
                    downLoadFile.downLoad();
                }else {
                    downLoadFile.onStart();
                }

                break;
            case R.id.bt_pause:
                downLoadFile.onPause();
                b = false;

                //获取当前下载的大小
                int currLength = downLoadFile.currLength;
                Log.e("TAG----",String.valueOf(currLength));

                //添加进数据库
                insert(String.valueOf(currLength));

                break;
            /*case R.id.bt_restart:
                downLoadFile.onStart();
                break;
            case R.id.bt_stop:
                downLoadFile.cancel();
                break;*/
        }
    }

    @Override
    protected void onDestroy() {
        downLoadFile.onDestroy();
        super.onDestroy();
    }
}
