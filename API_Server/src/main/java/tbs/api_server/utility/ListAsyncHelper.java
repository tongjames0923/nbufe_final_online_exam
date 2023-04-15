package tbs.api_server.utility;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ListAsyncHelper {

    private static class IndexFuture<T> {
        int index;
        Future<T> future;

        public IndexFuture(int index, Future<T> future) {
            this.index = index;
            this.future = future;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Future<T> getFuture() {
            return future;
        }

        public void setFuture(Future<T> future) {
            this.future = future;
        }
    }

    @Resource
    AsyncTaskCenter asyncTaskCenter;
    public <T> List<T> waitForAll(List<AsyncTaskCenter.AsyncToGet<T>> asyncToGets) {
        List<T> list = new ArrayList<>(asyncToGets.size());
        for(int i=0;i<asyncToGets.size();i++)
        {
            list.add(null);
        }
        List<IndexFuture<T>> futures = new ArrayList<>(asyncToGets.size());
        int i = 0;
        for (AsyncTaskCenter.AsyncToGet<T> task : asyncToGets) {
            try {
                futures.add( new IndexFuture<T>(i, asyncTaskCenter.getWithAsync(task)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        while (!futures.isEmpty()) {
            Iterator<IndexFuture<T>> iterator = futures.iterator();
            while (iterator.hasNext()) {
                IndexFuture<T> f = iterator.next();
                if (f.future.isDone()) {
                    try {
                        list.set(f.getIndex(), f.getFuture().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    iterator.remove();
                }
            }
        }
        return list;

    }

    public <T> List<T> waitForAll(AsyncTaskCenter.AsyncToGet<T>... asyncToGets) throws Exception {
        return waitForAll(Arrays.asList(asyncToGets));

    }

}
