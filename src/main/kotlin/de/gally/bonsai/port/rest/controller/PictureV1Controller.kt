package de.gally.bonsai.port.rest.controller

import de.gally.bonsai.domain.usecases.PictureManager
import de.gally.bonsai.port.rest.PictureNotFoundException
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import kotlin.reflect.KClass


@RestController
@RequestMapping("/api/v1/bonsais")
@Validated
class PictureV1Controller(
    private val pictureManager: PictureManager
) {

    @GetMapping("/{bonsai-uuid}/pictures")
    fun getAllPictureIdsByBonsai(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        @RequestParam("pageSize", defaultValue = "20") pageSize: Int,
        @RequestParam("page", defaultValue = "1") page: Int,
    ): List<UUID> {
        return pictureManager.getAllPictureUuids(bonsaiUuid, page, pageSize)
    }

    @PostMapping("/{bonsai-uuid}/pictures")
    fun addPictureToBonsai(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        @RequestParam("pictures") @MustBePicture pictures: List<MultipartFile>,
    ) {
        pictures.forEach {
            pictureManager.addPicture(bonsaiUuid, it.originalFilename ?: error("Filename has to be picture"), it.bytes)
        }
    }

    @GetMapping("/{bonsai-uuid}/pictures/{picture-uuid}")
    fun getPictureById(
        @PathVariable("picture-uuid") pictureUuid: UUID,
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        response: HttpServletResponse,
    ) {
        val picture = pictureManager.getPictureByUuid(bonsaiUuid, pictureUuid) ?: throw PictureNotFoundException(pictureUuid)

        response.apply {
            contentType = MediaType.IMAGE_PNG_VALUE
            addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${picture.fileName}")
            addHeader("x-created-at", picture.createdAt.toString())
            addHeader("x-filename", picture.fileName)
            outputStream.use { out ->
                out.write(picture.content)
                out.flush()
            }
        }
    }

    @DeleteMapping("/{bonsai-uuid}/pictures/{picture-uuid}")
    fun deletePicture(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID, @PathVariable("picture-uuid") pictureUUID: UUID
    ) = pictureManager.deletePicture(bonsaiUuid, pictureUUID)
}

@Constraint(validatedBy = [MustBePicture.MustBeImageImpl::class])
@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class MustBePicture(
    val message: String = "Filetype has to be an image", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = []
) {

    class MustBeImageImpl : ConstraintValidator<MustBePicture, List<MultipartFile>> {

        override fun isValid(multipartFiles: List<MultipartFile>, ctx: ConstraintValidatorContext): Boolean {
            val invalidFiles = multipartFiles.filter { it.contentType.isNullOrBlank() || it.contentType?.startsWith("image/") == true }.map { it.name }

            return invalidFiles.isNotEmpty()
        }
    }
}
