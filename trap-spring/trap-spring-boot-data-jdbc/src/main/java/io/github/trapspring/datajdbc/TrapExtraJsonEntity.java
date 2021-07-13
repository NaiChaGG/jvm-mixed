package io.github.trapspring.datajdbc;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.MappedSuperclass;

/**
 * 关于 extra entity 这样设计的初衷在于对于一些 扩展信息 需求不太确定，同时这些信息又不是经常需要查询，可以优化 主 table 数据排列
 * 这样设计要新增字段 无需修改表结构
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@MappedSuperclass
@Table
public abstract class TrapExtraJsonEntity extends AutoLongIDEntity {

    // 用户扩展信息
    private String extraKey;

    // 对于 数据 entity 啥的, 先字符
    @javax.persistence.Column(columnDefinition = "json")
    private String extraValue;

    public String getExtraKey() {
        return extraKey;
    }

    public void setExtraKey(String extraKey) {
        this.extraKey = extraKey;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

}
