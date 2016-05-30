package com.supaiclient.app.ui.fragment.contacts;

import com.supaiclient.app.ui.adapter.ViewPageFragmentAdapter;
import com.supaiclient.app.ui.base.BaseViewPagerFragment;

/**
 * Created by Administrator on 2015/12/28.
 * 选择 寄件人
 */
public class ContactsFragmemt extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

//        int type = getArguments().getInt("type");
//        if(type == 1){
//            adapter.addTab("寄件人", "寄件人", SelectPeopleFragment.class,null);
//        }else{
        adapter.addTab("通讯录", "通讯录", PhoneBookFragment.class, null);
//        }
        adapter.addTab("常用联系人", "常用联系人", SelectPeopleFragment.class, null);

    }
}
