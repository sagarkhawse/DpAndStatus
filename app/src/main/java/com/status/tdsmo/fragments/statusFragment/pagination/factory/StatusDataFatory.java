package com.status.tdsmo.fragments.statusFragment.pagination.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import com.status.tdsmo.fragments.statusFragment.pagination.datasource.StatusDataSource;
import com.status.tdsmo.fragments.statusFragment.viewmodel.StatusNav;

public class StatusDataFatory extends DataSource.Factory {

    private MutableLiveData<StatusDataSource> mutableStatus = new MutableLiveData<>();
    private StatusDataSource statusDataSource;

    public StatusDataFatory(StatusNav statusNav) {
        statusDataSource = new StatusDataSource(statusNav);
    }

    @NonNull
    @Override
    public DataSource create() {
        mutableStatus.postValue(statusDataSource);
        return statusDataSource;
    }

    public MutableLiveData<StatusDataSource> GetStatusLive() {
        return mutableStatus;
    }
}
