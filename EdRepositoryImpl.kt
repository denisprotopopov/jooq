package ru.raiffeisen.rise.core.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.Record
import org.jooq.impl.DSL.field
import org.springframework.data.domain.Pageable
import model.Ed
import java.util.stream.Collectors

class EdRepositoryImpl(
    val dsl: DSLContext,
    val objectMapper: ObjectMapper
): JooqRepository {
    override fun find(
        name: String,
        filter: Map<String, String>?,
        pageable: Pageable
    ): List<Any> {
        val select = dsl.select().from("ed").where("1=1")
        pageable.sort?.stream()?.forEach { k -> addSort(select, k.property, k.direction.name) }
        filter?.
        filterNot { it.key == "sort" }?.
        filterNot { it.key == "size" }?.
        filterNot { it.key == "page" }
            ?.keys?.stream()?.forEach { k -> addConditionField(select, k, filter[k]?.split(".")?.get(1) ?: "") }
        val result = select.limit(pageable.pageSize).offset(pageable.offset).fetch()

        return result.stream().map {
            toMap(it)
        }.map { objectMapper.convertValue(it, object: TypeReference<Map<String,Any?>>() {}) }
            .collect(Collectors.toList())
    }

    private fun toMap(record: Record): Map<String,Any?> {
        val rec = mutableMapOf<String,Any>()
        for(field in record.fields()) {
            rec[field.name] = record.getValue(field)
        }
        return  rec
    }
}
