package ru.raiffeisen.rise.core.controller

import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.raiffeisen.rise.core.controller.RestConstants.REST_API_PREFIX
import ru.raiffeisen.rise.core.controller.dto.EdDto
import ru.raiffeisen.rise.core.repository.model.Ed
import ru.raiffeisen.rise.core.service.EdService

@RestController
@RequestMapping("$REST_API_PREFIX/ed")
class EdController(
    val service: EdService,
) {

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun find(
        pageable: Pageable,
        @RequestParam(required = false) filter: Map<String, String>?
    ): List<EdDto> {
        return service.find(pageable,filter)
    }
}
