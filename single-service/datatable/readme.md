# 静态表的http服务
    + 今天做数据表配置的时候发现导入数据表是个重复的劳动
    + 特别是数据表要修改的时候 需要使用 current_version + version 字段来保证
    + 数据的版本和历史性，保证可以查询到旧的版本数据，记录日志的时候不会冗余数据
    + 对于数据表的表现如下：
        - int pri_id     // 主键,
        - int reward_id  // 奖励ID
        - int product_id // 奖品ID
        - int current_version // 当前数据使用版本
        - int version         // 当前版本
    + 重复的逻辑，则可以微服务化，内部屏蔽复杂逻辑，量大的情况下则考虑缓存什么的逻辑
    
## 静态表是典型的读多写少的场景

## 数据表的每一次修改其实都要保存为一个新版本数据，这样保证之前引用的数据依然可以拿到

## examples:
    project_table:
        pri_id:int (自增)
        final_id:int (保持不变)
        text:(string),  // 一朵大红花
        
    client_table:
        reward_log_id:(int),    // 奖励日志id
        project_id:(int),       // 奖励ID project_table pri_id