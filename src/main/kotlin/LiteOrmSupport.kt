import annotation.PrimaryKey
import entity.TableInfo
import table.CreateTable
import util.JDBCUtils
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
                println(annotation)
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

    fun findOne() {

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
                println(annotation)
                val matches = Pattern.matches("@annotation.PrimaryKey\\(\\)", annotation.toString())
                if (matches) {
                    primaryKey = f.toString().substringAfterLast(".")
                }
            }
        }
        println(primaryKey)

        println(this.getPropertyValue(primaryKey))
        var sql = StringBuffer("select * from $tableName where id = ?")


    }

}