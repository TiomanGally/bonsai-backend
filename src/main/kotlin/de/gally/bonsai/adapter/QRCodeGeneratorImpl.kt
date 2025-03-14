package de.gally.bonsai.adapter

import de.gally.bonsai.domain.usecases.QRCodeGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import uk.org.okapibarcode.backend.QrCode
import uk.org.okapibarcode.graphics.Color
import uk.org.okapibarcode.output.Java2DRenderer
import java.awt.image.BufferedImage
import java.io.File
import java.util.UUID
import javax.imageio.ImageIO

@Component
class QRCodeGeneratorImpl(
    @Value("\${bonsai.ui.link}") private val bonsaiUiLink: String,
    @Value("\${bonsai.root-directory-for-data}") private val rootDirectory: String,
) : QRCodeGenerator {

    private val filePath: (uuid: UUID, name: String) -> File = { uuid: UUID, name: String ->
        File("$rootDirectory/qrcode/$name-$uuid.png")
    }

    override operator fun invoke(name: String, uuid: UUID, text: String): File {
        val file = filePath(uuid, name)
        if (!file.exists()) {
            createFile(text, file)
        }
        return file
    }

    /**
     * Creates new QR Code with a link to the bonsai and saves it in the directory QR Codes.
     */
    private fun createFile(text: String, file: File) {
        val qrCode = QrCode().apply {
            content = bonsaiUiLink + text
        }

        val image = BufferedImage(qrCode.width, qrCode.height, BufferedImage.TYPE_BYTE_GRAY)
        Java2DRenderer(image.createGraphics(), 1.0, Color.WHITE, Color.BLACK).render(qrCode)

        file.mkdirs()
        ImageIO.write(image, "png", file)
    }
}
