package tbs.api_server.utility;

import java.io.*;
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

    public static abstract class BaseThen implements Consumer<FileResult>
    {
        private Throwable exception = null;

        private void throwError_ifHas() throws Throwable
        {
            if (exception != null)
                throw exception;
        }
        protected abstract void then(FileResult fileResult) throws Exception;

        protected abstract Object getData();

        public Object result() throws Throwable
        {
            throwError_ifHas();
            return getData();
        }

        @Override
        public void accept(FileResult fileResult)
        {
            try
            {
                then(fileResult);
            } catch (Exception e)
            {
                this.exception = e;
            }
        }
    }


    public static class FileWriteThen extends BaseThen
    {
        private InputStream stream;
        private boolean autorelease;
        private int length = 0;

        public FileWriteThen(InputStream stream, boolean autorelease)
        {
            this.autorelease = autorelease;
            this.stream = stream;
        }


        @Override
        protected void then(FileResult fileResult) throws Exception
        {
            FileOutputStream fileOutputStream;
            if (fileResult.isExist())
            {
                fileResult.file.delete();
            }
            fileOutputStream = new FileOutputStream(fileResult.file);
            byte[] buffer = new byte[1024 * 16];
            int len = 0, total = 0;
            while ((len = stream.read(buffer)) > 0)
            {
                fileOutputStream.write(buffer, 0, len);
                total += len;
            }
            fileOutputStream.close();
            if (autorelease)
                stream.close();
            length = total;
        }

        @Override
        protected Object getData()
        {
            return length;
        }
    }
    public static class FileDeleteThen extends BaseThen
    {
        boolean deleted=false;
        @Override
        protected void then(FileResult fileResult) throws Exception
        {
            if(fileResult.isExist())
               deleted= fileResult.file.delete();

        }

        @Override
        protected Object getData()
        {
            return deleted;
        }
    }

    public static class FileReadThen extends BaseThen
    {
        private byte[] data = null;
        private int length = -1;
        private Throwable exception = null;

        private void throwError_ifHas() throws Throwable
        {
            if (exception != null)
                throw exception;
        }

        @Override
        protected void then(FileResult fileResult) throws Exception
        {
            if (fileResult.isExist())
            {
                FileInputStream stream = new FileInputStream(fileResult.getFile());
                BufferedInputStream bf = new BufferedInputStream(stream);
                data = new byte[bf.available()];
                length = bf.read(data, 0, data.length);
                stream.close();
                bf.close();
            }
            else
                throw new RuntimeException("file can not be read");
        }

        @Override
        protected Object getData()
        {
            return data;
        }

        public int length()
        {
            return length;
        }

        public static FileReadThen makeOne()
        {
            return new FileReadThen();
        }
    }


    public static void existFile(String path, Consumer<FileResult> THEN)
    {
        File file = new File(path);
        FileResult result = new FileResult(file.exists(), file);
        Optional.ofNullable(THEN).orElse(res ->
                                         {
                                             try
                                             {
                                                 if (!res.isExist())
                                                     res.getFile().createNewFile();
                                             } catch (Exception e)
                                             {
                                                 e.printStackTrace();
                                             }
                                         }).accept(result);
    }

    public static void existDIR(String path, Consumer<FileResult> THEN)
    {
        File file = new File(path);
        FileResult result = new FileResult(file.exists(), file);
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
