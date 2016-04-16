package info.yurushao.apkanalysis.Test;

import info.yurushao.apkanalysis.ThreadController;
import info.yurushao.apkanalysis.decoder.Decoder;
import info.yurushao.apkanalysis.utils.Filewalker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackjia on 11/11/15.
 */
public class TestController extends ThreadController {
    private static final String TAG = TestController.class.getSimpleName();

    private boolean mRecursiveMode = false;
    private String mApkDir = ".";
    private String mOutDir = ".";

    public TestController(int n) {
        super(n);
    }

    @Override
    protected void init() {
        for (int i = 0; i < mNumThreads; ++i) {
            String name = String.format("Decoder-%d", ThreadController.getNextId());
//            mWorkers.add(new Thread(new Decoder(name), name));
            mWorkers.add(new Tester(name));
        }
    }

    public void recursiveMode(boolean r) {
        mRecursiveMode = r;
    }

    public void setApkDir(String d) {
        mApkDir = d;
    }

    public void setOutDir(String d) {
        mOutDir = d;

        //File dir = new File(mOutDir);
        //if (!dir.exists()) {
        //    dir.mkdirs();
        //}
    }

    @Override
    protected void assign() {
        int i = 0;
        /*for (;i<10;i++) {
            // TODO:
            // Describe the task using Map
            Map<String, String> e = new HashMap();

            mWorkers.get(i % mNumThreads).addToList("Hello");
        }*/
    }

    @Override
    public void start() {
        init();
        assign();
        runExecutor();
    }
    private List<String> findSelectedfApks(boolean recursive) {
        List<String> apks = new ArrayList<String>();

        if (recursive) {
//            Filewalker fw = new Filewalker(".apk");
            Filewalker fw = new Filewalker();
            apks.addAll(fw.listFiles(mApkDir));
        } else {
            for (File f : new File(mApkDir).listFiles()) {
                if (f.getName().endsWith(".apk")) {
                    apks.add(f.getAbsolutePath());
                }
            }
        }

        return apks;
    }
    public void OnComplete(){

    }
}
