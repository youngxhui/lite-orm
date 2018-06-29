package table

import entity.Person
import entity.TableInfo
import org.junit.Test
import java.lang.reflect.Field

/**
 * @author 杨晓辉 2018-06-29 21:00
 */
class CreateTableTest {

    @Test
    fun create() {
        val createTable = CreateTable()
        val tableInfo = TableInfo()
        tableInfo.tableName = "test"
        val fields = Person::class.java.declaredFields
        tableInfo.columnName=fields
        createTable.create(tableInfo)
    }
}