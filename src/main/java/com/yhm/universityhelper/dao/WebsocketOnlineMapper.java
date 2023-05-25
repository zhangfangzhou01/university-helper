package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.WebscoketOnline;

public interface WebsocketOnlineMapper extends BaseMapper<WebscoketOnline> {
    void insertOrUpdate(WebscoketOnline webscoketOnline);
}
