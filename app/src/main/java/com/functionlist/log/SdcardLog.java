package com.functionlist.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Created by imo on 2016/9/6.
 * 将日志打印到sdcard
 */
public class SdcardLog {
    private final BlockingQueue<String> queue;
    private Thread writerThread;
    private PrintWriter print;
    private boolean done;
    public static String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getPath() + File.separator + "function"+ File.separator;

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateAndtimeFormatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss SSS");
    private Date date = new Date();


    private static final int nMaxLogFileCnt = 5;	//保存最近的5个文件
    private static final long lMaxTimeSpan = 5*24*3600*1000; //5天前的文件才做清理



    private static SdcardLog instance= new SdcardLog();;
    private File rootLogFile;
    private static Context appContext;
    public static SdcardLog getInstance(){
        return instance;
    }

    public static void init(Context c) {
        appContext = c;
        instance.init();
    }

    public SdcardLog(){
        this.queue = new ArrayBlockingQueue<String>(500, true);
    }

    private void init(){
        done = false;
        rootLogFile  = new File(getRootLogFilePathByProcess(true));
        if(!rootLogFile.exists()){
            rootLogFile.mkdirs();
        }
        File logFile = new File(rootLogFile,getLogNameByCurrDate());
        try {
            print = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(logFile, true),"utf-8"));
        }catch (Exception e){
            e.printStackTrace();

        }
        writerThread = new Thread(){
            @Override
            public void run() {
                writerLog(this);
            }
        };
        writerThread.start();

    }


    public File getCurLogFile(boolean bMainProc) {
        File file = new File(getRootLogFilePathByProcess(bMainProc)+"/"+getLogNameByCurrDate());
        if (file.exists()) {
            return file;
        }
        return null;
    }

    private String getLogNameByCurrDate(){
        Calendar dt = new java.util.GregorianCalendar();
        String dateStr = String.format(Locale.getDefault(),"%04d-%02d-%02d",
                dt.get(Calendar.YEAR),
                dt.get(Calendar.MONTH)+1,
                dt.get(Calendar.DATE));
        String sFileName = dateStr + ".txt";
        return sFileName;
    }

    public String getRootLogFilePathByProcess(boolean isMain){
        String logPath = null;
        if (isMain){
          logPath = ROOT_PATH + "StackTraceLogUi" + File.separator;
        }else{
          logPath =  ROOT_PATH + "StackTraceLogCore" + File.separator;
        }
        return logPath;
    }
    public File[] getLogFiles(){
       return rootLogFile.listFiles();
    }

    private String nextLog(){
        String log = null;
        while (!done && (log = queue.poll()) == null) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            }
            catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return log;

    }
    public void addLog(int level, String log){
        date.setTime(System.currentTimeMillis());

        log = "["+dateAndtimeFormatter.format(date) + "] " + log;
        if (!done) {
            try {
                queue.put(log);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
                return;
            }
            synchronized (queue) {
                queue.notifyAll();
            }
        }
    }

    private boolean bFlush = false;

    private void writerLog(Thread writerThread) {
        String log =null;
        while (!done && this.writerThread == writerThread){
            log = nextLog();
            if (log != null && print != null) {
                print.println(log);
                if (bFlush) {
                    print.flush();
                    bFlush = false;
                }
            }
        }
    }
    public void notifyFlaush(){
        bFlush = true;
        addLog(0,"noticeFlaush");
    }
    public void clearLogFiles()
    {


        File[] files = rootLogFile.listFiles(new TxtFilter());
        if (null == files) {
            return;
        }
        ArrayList<File> lsFiles = new ArrayList<File>();
        for(File f:files)
        {
            lsFiles.add(f);
        }
        long lCur = System.currentTimeMillis();
        Collections.sort(lsFiles, new FileComparator(true));
        int nLogFiles = 0;
        for(int i = 0; i<lsFiles.size(); ++i)
        {
            if(lsFiles.get(i).getName().endsWith("zip"))
            {
                lsFiles.get(i).delete();
                continue;
            }
            if(++nLogFiles < nMaxLogFileCnt)
            {
                continue;
            }
            if(lsFiles.get(i).lastModified() <lCur - lMaxTimeSpan)
            {
                lsFiles.get(i).delete();
            }

        }
    }
    private static class TxtFilter implements FileFilter {
        public boolean accept(File pathname) {
            if (pathname.getName().endsWith(".txt")
                    || pathname.getName().endsWith(".zip"))
            {
                return true;
            }
            return false;
        }
    }
    private static class FileComparator implements Comparator<File> {
        private boolean m_bDesc = false;
        public FileComparator(boolean bDesc)
        {
            m_bDesc = bDesc;
        }
        public int compare(File file1, File file2) {
            if(file1.lastModified() < file2.lastModified())
            {
                return m_bDesc?1:-1;
            }else
            {
                return m_bDesc?-1:1;
            }
        }
    }
}
