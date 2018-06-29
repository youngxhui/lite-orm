package entity

import java.lang.reflect.Field

/**
 * @author 杨晓辉 2018-06-29 20:31
 */
class TableInfo {

    /**
     * 构造器
     */
    constructor()

    /**
     * @param tableName 表名
     * @param columnName 字段名
     * @param primaryKey 主键
     * 初始化构造器
     */
    constructor(tableName: String, columnName: Array<Field>, primaryKey: String = "id") {
        this.tableName = tableName
        this.columnName = columnName
        this.primaryKey = primaryKey
    }

    /**
     * 表名
     */
    lateinit var tableName: String

    /**
     * 字段名
     */
    lateinit var columnName: Array<Field>

    /**
     * 主键名
     * 默认主键为id
     */
    var primaryKey: String = "id"


}