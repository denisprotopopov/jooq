package ru.raiffeisen.rise.core.repository

import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.SelectSeekStep1
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.springframework.data.domain.Pageable

interface JooqRepository {

    fun find(
        name: String,
        filter: Map<String, String>?,
        pageable: Pageable
    ): List<Any>

    fun addCondition(scs: SelectConditionStep<Record>, fname: String, value: String): SelectConditionStep<Record> {
        return scs.and(DSL.field("fields::json->>'$fname'").eq( "$value"))
    }

    fun addConditionField(scs: SelectConditionStep<Record>, fname: String, value: String): SelectConditionStep<Record> {
        return scs.and(DSL.field("$fname").eq( "$value"))
    }

    fun addSort(scs: SelectConditionStep<Record>, fname: String, direction: String): SelectSeekStep1<Record, Any> {
        when (direction) {
            "DESC" -> return scs.orderBy(field(fname).desc() )
            else -> return scs.orderBy(field(fname).asc() )
        }
    }
}