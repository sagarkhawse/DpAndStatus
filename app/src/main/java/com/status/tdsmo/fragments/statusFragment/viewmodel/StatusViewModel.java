package com.status.tdsmo.fragments.statusFragment.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.status.tdsmo.fragments.statusFragment.pagination.datasource.StatusDataSource;
import com.status.tdsmo.fragments.statusFragment.pagination.factory.StatusDataFatory;
import com.status.tdsmo.models.Image;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StatusViewModel extends AndroidViewModel {
    private LiveData<PagedList<Image>> getStatusList;
    private StatusDataSource blogDataSource;
    private StatusDataFatory statusDataFatory;
    private LiveData<StatusDataSource> sourceLiveData;
    private Executor executor;

    public StatusViewModel(@NonNull Application application) {
        super(application);
    }

    public void LoadPaging(StatusNav statusNav){
        statusDataFatory= new StatusDataFatory(statusNav);
        //GetLiveSource
        sourceLiveData=statusDataFatory.GetStatusLive();
        //set PageList Config
        PagedList.Config config= (new PagedList.Config.Builder()).setEnablePlaceholders(false).setPrefetchDistance(1).build();
        //ThreadPool
        executor= Executors.newFixedThreadPool(6);
        //Sent LiveBarList
        getStatusList=(new LivePagedListBuilder<Long, Image>(statusDataFatory,config)).setFetchExecutor(executor).build();
    }


    public LiveData<PagedList<Image>> getStatusList() {
        return getStatusList;
    }
}