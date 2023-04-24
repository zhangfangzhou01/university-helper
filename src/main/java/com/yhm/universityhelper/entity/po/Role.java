package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_role")
@ApiModel(value = "Role", description = "")
public class Role implements Serializable {

    public static final long ADMIN = 1L;
    public static final long USER = 2L;

    private static final long serialVersionUID = 1L;

    @TableId(value = "roleId", type = IdType.AUTO)
    private Long roleId;

    @TableField("rolename")
    private String rolename;

    @Version
    @TableField("version")
    private Integer version;
}
