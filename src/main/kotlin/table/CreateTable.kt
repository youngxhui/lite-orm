package table

import entity.TableInfo
import org.intellij.lang.annotations.Language
import util.JDBCUtils

/**
 * @author 杨晓辉 2018-06-29 20:42
 * 自动创建表 如果表不存在的话
 */
class CreateTable {

    fun create(tableInfo: TableInfo) {
        @Language("MySQL")
        val sql =
            StringBuffer("CREATE TABLE if not exists ${tableInfo.tableName} (${tableInfo.primaryKey} int(11) NOT NULL AUTO_INCREMENT,")

        for (c in tableInfo.columnName) {
            val type = c.type.name.substringAfterLast(".")
            val columnName = c.toString().substringAfterLast(".")
            if (columnName == tableInfo.primaryKey) {
                continue
            }
            when (type) {
                "String" -> sql.append(" $columnName varchar(255) default Null,")

                "int" -> sql.append(" $columnName int(255) default Null,")

                "long" -> sql.append(" $columnName mediumtext default Null,")

                "float" -> sql.append("$columnName  float default Null,")

                "double" -> sql.append(" $columnName double default Null,")
            }
        }
        sql.append(" PRIMARY KEY (`${tableInfo.primaryKey}`)) ENGINE=InnoDB  DEFAULT CHARSET=utf8")
        JDBCUtils.getConnection().createStatement().execute(sql.toString())
        JDBCUtils.closeConnection()
    }


}