package core.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class EdServiceImpl(
    val repository: EdRepository,
    val objectMapper: ObjectMapper
) : EdService {
    override fun find(pageable: Pageable,filter: Map<String, String>?): List<EdDto> {
        return repository.find("ed", filter, pageable).map { objectMapper.convertValue(it, EdDto::class.java) }
    }
}
