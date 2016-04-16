package info.yurushao.apkanalysis.installer;

import info.yurushao.apkanalysis.Test.Tester;
import info.yurushao.apkanalysis.ThreadController;
import info.yurushao.apkanalysis.utils.Filewalker;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jackjia on 11/12/15.
 */
public class InstallController extends ThreadController {
    private static final String TAG = InstallController.class.getSimpleName();
    private String output = ".";
    private boolean mRecursiveMode = false;
    private String mApkDir = ".";
    private String mPackageList = ".";
    public InstallController(int n) {
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
        output = d;
    }

    public void setPackageList(String d) {
        mPackageList = d;

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
        findSelectedfApks(mRecursiveMode);
        //assign();
        //runExecutor();
    }
    private List<String> findSelectedfApks(boolean recursive) {
        List<String> apks = new ArrayList<String>();
        try{
            if (recursive) {
                Filewalker fw = new Filewalker(".apk");
                //Filewalker fw = new Filewalker();
                apks.addAll(fw.listFiles(mApkDir));
                FileWriter writer = new FileWriter(output);
                for (String str:apks){
                    writer.write(str+"\n");
                }
                writer.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return apks;
    }
    private boolean isSelectedFile(){
        return true;
    }
    public void OnComplete(){

    }
}
