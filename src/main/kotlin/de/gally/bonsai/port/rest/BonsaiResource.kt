package de.gally.bonsai.port.rest

import de.gally.bonsai.domain.Bonsai
import java.time.LocalDate
import java.util.UUID

data class BonsaiResource(
    val uuid: UUID,
    val latinName: String,
    val simpleName: String?,
    val birthDate: LocalDate,
    val lastRepoted: LocalDate?,
    val price: Double,
) {
    companion object {
        fun from(bonsai: Bonsai): BonsaiResource = BonsaiResource(
            bonsai.uuid,
            bonsai.latinName,
            bonsai.simpleName,
            bonsai.birthDate,
            bonsai.lastRepoted,
            bonsai.price
        )
    }
}
