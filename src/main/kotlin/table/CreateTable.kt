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
        val sbf =
            StringBuffer("CREATE TABLE if not exists ${tableInfo.tableName} (  ${tableInfo.primaryKey}   int(11) NOT NULL  AUTO_INCREMENT,")

        for (c in tableInfo.columnName) {
            val type = c.type.name.substringAfterLast(".")
            val columnName = c.toString().substringAfterLast(".")
            when (type) {
                "String" -> sbf.append(" $columnName varchar(255) default Null,")

                "int" -> sbf.append(" $columnName int(255) default Null,")

                "long" -> sbf.append(" $columnName mediumtext default Null,")

                "float" -> sbf.append("$columnName  float default Null,")

                "double" -> sbf.append(" $columnName double default Null,")
            }
        }
        sbf.append(" PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8")
        println(sbf.toString())
        JDBCUtils.getConnection().createStatement().execute(sbf.toString())
        JDBCUtils.closeConnection()
    }

}