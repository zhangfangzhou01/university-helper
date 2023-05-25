package com.yhm.universityhelper.dao;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.PostTag;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

public interface PostTagMapper extends BaseMapper<PostTag> {
    void insertBatch(JSONArray postTags);
}
