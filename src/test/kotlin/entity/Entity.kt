package entity

import LiteOrmSupport
import annotation.Entity
import annotation.PrimaryKey

/**
 * @author 杨晓辉 2018-06-29 21:04
 */
@Entity("person")
class Person() : LiteOrmSupport() {

    @PrimaryKey
    lateinit var name: String

    lateinit var age: String
    lateinit var sex: String
    override fun toString(): String {
        return "entity.Person( name='$name', age='$age')"
    }


}