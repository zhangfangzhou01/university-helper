package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_role")
@ApiModel(value = "Role", description = "")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "roleId", type = IdType.AUTO)
    private Integer roleId;
    @TableField("rolename")
    private String rolename;


}
