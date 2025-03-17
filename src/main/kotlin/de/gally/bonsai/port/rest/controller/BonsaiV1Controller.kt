package de.gally.bonsai.port.rest.controller

import de.gally.bonsai.config.getCurrentAuthentication
import de.gally.bonsai.config.getUuidOfUser
import de.gally.bonsai.domain.Bonsai
import de.gally.bonsai.domain.usecases.BonsaiManager
import de.gally.bonsai.domain.usecases.QRCodeGenerator
import de.gally.bonsai.domain.usecases.UserService
import de.gally.bonsai.port.rest.BonsaiNotFoundException
import de.gally.bonsai.port.rest.BonsaiResource
import de.gally.bonsai.port.rest.CreateBonsaiRequestBody
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/bonsais")
@Validated
class BonsaiV1Controller(
    private val bonsaiManager: BonsaiManager, private val qrCodeGenerator: QRCodeGenerator, private val userService: UserService
) {
    @PostMapping
    fun addBonsai(
        @Valid @RequestBody createBonsaiRequestBody: CreateBonsaiRequestBody,
    ): ResponseEntity<ByteArray> {
        val userUuid = getCurrentAuthentication().getUuidOfUser(userService)

        val uuid = UUID.randomUUID()
        val bonsai = Bonsai(
            uuid,
            createBonsaiRequestBody.latinName,
            createBonsaiRequestBody.simpleName,
            createBonsaiRequestBody.birthDate,
            createBonsaiRequestBody.price,
            createBonsaiRequestBody.lastRepoted
        )

        bonsaiManager.addBonsai(bonsai, userUuid)

        return ResponseEntity.ok().headers { it.add("x-bonsai-uuid", uuid.toString()) }.contentType(MediaType.IMAGE_PNG)
            .body(qrCodeGenerator(createBonsaiRequestBody.latinName, uuid, "/api/v1/bonsais/$uuid").readBytes())
    }

    @GetMapping("/{uuid}/qr-code")
    fun getQrCode(@PathVariable uuid: UUID): ResponseEntity<ByteArray> {
        val bonsai = bonsaiManager.getBonsai(uuid) ?: throw BonsaiNotFoundException(uuid)

        return ResponseEntity.ok().headers { it.add("x-bonsai-uuid", uuid.toString()) }.contentType(MediaType.IMAGE_PNG)
            .body(qrCodeGenerator(bonsai.latinName, uuid, "/api/v1/bonsais/$uuid").readBytes())
    }

    @GetMapping
    fun getAllBonsais(): ResponseEntity<List<BonsaiResource>> {
        return bonsaiManager.getAllBonsais(getCurrentAuthentication().getUuidOfUser(userService)).map { BonsaiResource.from(it) }.let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{bonsai-uuid}")
    fun getBonsaiById(@PathVariable("bonsai-uuid") bonsaiUUID: UUID): BonsaiResource =
        bonsaiManager.getBonsai(bonsaiUUID)?.let { BonsaiResource.from(it) } ?: throw BonsaiNotFoundException(bonsaiUUID)

    @PutMapping("/{bonsai-uuid}")
    fun editBonsai(
        @RequestBody bonsai: Bonsai,
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
    ) {
        if (bonsaiUuid != bonsai.uuid) {
            throw IllegalArgumentException("bonsai does not match UUID")
        }
        bonsaiManager.editBonsai(bonsai)
    }

    @DeleteMapping("/{bonsai-uuid}")
    fun deleteBonsai(@PathVariable("bonsai-uuid") bonsaiUUID: UUID) {
        bonsaiManager.deleteBonsai(bonsaiUUID)
    }
}
