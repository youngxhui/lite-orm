import entity.TableInfo
import table.CreateTable
import util.JDBCUtils
import wu.seal.jvm.kotlinreflecttools.getPropertyValue

/**
 * @author 杨晓辉 2018-06-29 17:05
 */

open class LiteOrmSupport {

    /**
     * 将类进行保存
     * **案例**
     * ```
     *      var person = Person()
     *      person.name = "张三"
     *      person.age = "18"
     *      person.sex = "男"
     *      person.save()
     * ```
     */
    fun save(): Boolean {
        // 获取数据库
        val tableName = this::class.simpleName


        val fields = this::class.java.declaredFields
        // 获取属性值
        val attributes = ArrayList<String>()
        for (i in 0 until fields.size) {
            attributes.add(fields[i].toString().substringAfterLast("."))
        }
        // 获取连接
        val connection = JDBCUtils.getConnection()

        val tableInfo = TableInfo(tableName!!, fields, primaryKey = "id")

        val createTable = CreateTable()
        createTable.create(tableInfo)
        val sql = StringBuffer("insert into $tableName (")

        for (i in 0 until fields.size - 1) {
            sql.append(fields[i].toString().substringAfterLast(".") + ",")
        }
        sql.append(fields[fields.size - 1].toString().substringAfterLast(".") + ") values (")
        for (i in 0 until fields.size - 1) {
            sql.append("?,")
        }

        sql.append("?)")

        val ps = connection.prepareStatement(sql.toString())
        for (i in 0 until fields.size) {
            val type = fields[i].type.name.substringAfterLast(".")
            val index = i + 1
            val value = this.getPropertyValue(fields[i].toString().substringAfterLast(".")).toString()
            when (type) {
                "String" -> ps.setString(index, value)

                "int" -> ps.setInt(index, value.toInt())

                "long" -> ps.setLong(index, value.toLong())

                "float" -> ps.setFloat(index, value.toFloat())

                "double" -> ps.setDouble(index, value.toDouble())

                "byte" -> ps.setByte(index, value.toByte())
            }
        }
        val row = ps.executeUpdate()
        JDBCUtils.closeConnection()
        return row != 0
    }
}