import annotation.PrimaryKey
import entity.TableInfo
import table.CreateTable
import util.JDBCUtils
import wu.seal.jvm.kotlinreflecttools.changePropertyValue
import wu.seal.jvm.kotlinreflecttools.getPropertyValue
import java.util.regex.Pattern

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
        val tableInfo = TableInfo()
        var primaryKey = "id"
        // 获取数据库
        var tableName = this::class.simpleName!!
        val isClassAnnotation = this::class.java.isAnnotation
        if (!isClassAnnotation) {
            val annotations = this::class.annotations
            for (annotation in annotations) {
                val matches = Pattern.matches("@annotation\\.Entity?.*", annotation.toString())
                if (matches) {
                    tableName = annotation.toString().substringBeforeLast(")").substringAfterLast("=")
                }
            }
        }

        val fields = this::class.java.declaredFields
        for (f in fields) {
            val annotationPresent = f.isAnnotationPresent(PrimaryKey::class.java)
            if (annotationPresent) {
                val annotation = f.getAnnotation(PrimaryKey::class.java)
                val matches = Pattern.matches("@annotation.PrimaryKey\\(\\)", annotation.toString())
                if (matches) {
                    primaryKey = f.toString().substringAfterLast(".")
                }
            }
        }

        // 获取属性值
        val attributes = ArrayList<String>()
        for (i in 0 until fields.size) {
            attributes.add(fields[i].toString().substringAfterLast("."))
        }
        // 获取连接
        val connection = JDBCUtils.getConnection()
        tableInfo.primaryKey = primaryKey
        tableInfo.tableName = tableName
        tableInfo.columnName = fields
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

            if (fields[i].toString().substringAfterLast(".") == primaryKey) {
                ps.setString(i + 1, null)
            } else {
                val type = fields[i].type.name.substringAfterLast(".")
                val index = i + 1
                val value = this.getPropertyValue(fields[i].toString().substringAfterLast(".")).toString()
                //todo 完成其他类型转换
                when (type) {
                    "String" -> ps.setString(index, value)

                    "int" -> ps.setInt(index, value.toInt())

                    "long" -> ps.setLong(index, value.toLong())

                    "float" -> ps.setFloat(index, value.toFloat())

                    "double" -> ps.setDouble(index, value.toDouble())

                    "byte" -> ps.setByte(index, value.toByte())
                }

            }
        }
        val row = ps.executeUpdate()
        JDBCUtils.closeConnection()
        return row != 0
    }

    fun update() {

    }

    /**
     * 查找当前id的值
     */
    fun findOne(): LiteOrmSupport {

        var primaryKey = "id"

        // 获取数据库
        var tableName = this::class.simpleName!!
        val isClassAnnotation = this::class.java.isAnnotation
        if (!isClassAnnotation) {
            val annotations = this::class.annotations
            for (annotation in annotations) {
                val matches = Pattern.matches("@annotation\\.Entity?.*", annotation.toString())
                if (matches) {
                    tableName = annotation.toString().substringBeforeLast(")").substringAfterLast("=")
                }
            }
        }

        val fields = this::class.java.declaredFields
        for (f in fields) {
            val annotationPresent = f.isAnnotationPresent(PrimaryKey::class.java)
            if (annotationPresent) {
                val annotation = f.getAnnotation(PrimaryKey::class.java)
                val matches = Pattern.matches("@annotation.PrimaryKey\\(\\)", annotation.toString())
                if (matches) {
                    primaryKey = f.toString().substringAfterLast(".")
                }
            }
        }

        val id = this.getPropertyValue(primaryKey).toString()
        val sql = StringBuffer("select ")
        for (f in fields) {
            sql.append(f.toString().substringAfterLast(".") + ",")
        }
        sql.deleteCharAt(sql.length - 1).append(" from $tableName where $primaryKey = ?")

        val connection = JDBCUtils.getConnection()
        val ps = connection.prepareStatement(sql.toString())
        ps.setInt(1, id.toInt())
        val rs = ps.executeQuery()
        while (rs.next()) {
            for (i in 0 until fields.size) {
                val property = rs.metaData.getColumnLabel(i + 1)
                val columnType = rs.metaData.getColumnTypeName(i + 1)
                when (columnType) {
                    "INT" -> {
                        val value = rs.getInt(property)
                        this.changePropertyValue(property, value)
                    }
                    "VARCHAR" -> {
                        val value = rs.getString(property)
                        this.changePropertyValue(property, value)
                    }
                    //todo 完成其他类型转换
                }

            }
        }

        JDBCUtils.closeConnection()
        return this
    }

}