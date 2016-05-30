package com.supaiclient.app.interf;

import com.supaiclient.app.bean.PeopleBean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public interface OnGetPeopleListener {

    void OnBack(List<PeopleBean> peopleBeanList);
}
