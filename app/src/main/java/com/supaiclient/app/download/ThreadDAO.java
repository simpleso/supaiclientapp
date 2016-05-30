/*
 * @Title ThreadDAO.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-8 ����10:55:21
 * @version 1.0
 */
package com.supaiclient.app.download;

import java.util.List;

/**
 * ���ݷ��ʽӿ�
 *
 * @author Yann
 * @date 2015-8-8 ����10:55:21
 */
public interface ThreadDAO {

    void insertThread(ThreadInfo threadInfo);

    void deleteThread(String url);

    void updateThread(String url, int thread_id, int finished);

    List<ThreadInfo> getThreads(String url);

    boolean isExists(String url, int thread_id);
}
