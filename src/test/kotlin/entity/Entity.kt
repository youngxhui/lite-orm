package entity

import LiteOrmSupport
import annotation.Entity
import annotation.PrimaryKey

/**
 * @author 杨晓辉 2018-06-29 21:04
 */
@Entity(name = "tests")
class Person() : LiteOrmSupport() {


    @PrimaryKey
    var ids: Int = 0

    lateinit var name: String

    lateinit var age: String
    lateinit var sex: String

    override fun toString(): String {
        return "Person(ids=$ids, name='$name', age='$age', sex='$sex')"
    }


}