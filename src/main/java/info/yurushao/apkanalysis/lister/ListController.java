package info.yurushao.apkanalysis.lister;

import info.yurushao.apkanalysis.Grepper.Grepper;
import info.yurushao.apkanalysis.ThreadController;
import info.yurushao.apkanalysis.utils.Filewalker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackjia on 11/24/15.
 */
public class ListController extends ThreadController {
    private static final String TAG = ListController.class.getSimpleName();
    private boolean mRecursiveMode = false;
    private String mSrcDir = ".";
    private String mOutDir = ".";
    public ListController(int n) {
        super(n);
    }
    public static List<String> files = new ArrayList<String>();
    public BufferedWriter out;
    @Override
    protected void init() {
        for (int i = 0; i < mNumThreads; ++i) {
            String name = String.format("Grepper-%d", ThreadController.getNextId());
            mWorkers.add(new Lister(name));
        }
    }

    public void recursiveMode(boolean r) {
        mRecursiveMode = r;
    }

    public void setSrcDir(String d) {
        mSrcDir = d;
    }

    public void setOutDir(String d) {
        mOutDir = d;


    }
    @Override
    protected void assign() {
        int i = 0;
        for (String apk : findAllDirectories(mRecursiveMode)) {
            // TODO:
            // Describe the task using Map

            mWorkers.get(i % mNumThreads).addToList(apk);
            ++i;
        }
        System.out.print("Assign Completed!");
    }

    @Override
    public void start() {
        init();
        assign();
        runExecutor();
        Runnable runnable = new monitor();
        Thread monitor_thread = new Thread(runnable);
        monitor_thread.start();
    }

    private List<String> findAllDirectories(boolean recursive) {
         int i=0;
        List<String> directories = new ArrayList<String>();

            for (File f : new File(mSrcDir).listFiles()) {
                if (f.isDirectory()) {
                    directories.add(f.getAbsolutePath());
                    System.out.println(f.getAbsolutePath());
                }
            }

        return directories;
    }
    @Override
    public void OnComplete(){
        System.out.println("OnComplete");
        System.out.println("Files length="+files.size());
        shutdownExecutor();
        try {
            File outFile = new File(mOutDir);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(outFile.getName(), true));
            for (String str:files){
                    out.write(str+"\n");
            }
            out.close();
        }
        catch (Exception e){

        }
    }
}
