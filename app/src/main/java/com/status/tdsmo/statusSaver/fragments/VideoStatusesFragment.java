package com.status.tdsmo.statusSaver.fragments;


import com.status.tdsmo.statusSaver.models.Status;

import java.util.List;



public class VideoStatusesFragment extends BaseStatusesFragment {

    @Override
    public List<Status> getStatuses() {
        return getStatusManager().getVideoStatuses();
    }

}
