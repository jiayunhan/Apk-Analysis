package info.yurushao.apkanalysis.decoder;

import info.yurushao.apkanalysis.ThreadController;
import info.yurushao.apkanalysis.utils.Filewalker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yurushao on 11/4/15.
 */
public class DecodeController extends ThreadController {
    private static final String TAG = DecodeController.class.getSimpleName();

    private boolean mRecursiveMode = false;
    private String mApkDir = ".";
    private String mOutDir = ".";

    public DecodeController(int n) {
        super(n);
    }

    @Override
    protected void init() {
        for (int i = 0; i < mNumThreads; ++i) {
            String name = String.format("Decoder-%d", ThreadController.getNextId());
//            mWorkers.add(new Thread(new Decoder(name), name));
            mWorkers.add(new Decoder(name));
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

        File dir = new File(mOutDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    protected void assign() {
        int i = 0;
        for (String apk : findAllApks(mRecursiveMode)) {
            // TODO:
            // Describe the task using Map

            mWorkers.get(i % mNumThreads).addToList(apk);
            ++i;
        }
    }

    @Override
    public void start() {
        init();
        assign();
        runExecutor();
    }

    private List<String> findAllApks(boolean recursive) {
        List<String> apks = new ArrayList<String>();

        if (recursive) {
//            Filewalker fw = new Filewalker(".apk");
            Filewalker fw = new Filewalker(".apk",new filter());
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
    public static class filter implements Filewalker.Filter {
        public boolean foo(File f) {
            return true;
        }
    }
}
