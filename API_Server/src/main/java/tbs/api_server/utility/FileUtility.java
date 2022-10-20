package tbs.api_server.utility;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

public class FileUtility
{
     public static class FileResult
    {
        private boolean exist;
        private File file;

        public FileResult(boolean exist, File file)
        {
            this.exist = exist;
            this.file = file;
        }

        public boolean isExist()
        {
            return exist;
        }

        public void setExist(boolean exist)
        {
            this.exist = exist;
        }

        public File getFile()
        {
            return file;
        }

        public void setFile(File file)
        {
            this.file = file;
        }
    }
    public static void existFile(String path, Consumer<FileResult> THEN)
    {
        File file=new File(path);
        FileResult result=new FileResult(file.exists(),file);
        Optional.ofNullable(THEN).orElse(res->{
            try
            {
                if(!res.isExist())
                    res.getFile().createNewFile();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }).accept(result);
    }
    public static void existDIR(String path, Consumer<FileResult> THEN)
    {
        File file=new File(path);
        FileResult result=new FileResult(file.exists(),file);
        Optional.ofNullable(THEN).orElse(
                new Consumer<FileResult>()
                {
                    @Override
                    public void accept(FileResult res)
                    {
                        try
                        {
                            if (!res.isExist())
                                res.getFile().mkdir();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).accept(result);
    }
}
