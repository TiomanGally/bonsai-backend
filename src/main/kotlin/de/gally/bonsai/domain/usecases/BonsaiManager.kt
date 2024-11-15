package de.gally.bonsai.domain.usecases

import de.gally.bonsai.domain.Bonsai
import java.util.UUID

interface BonsaiManager {

    fun getAllBonsais(userId: UUID): List<Bonsai>

    fun getBonsai(bonsaiId: UUID): Bonsai?

    fun addBonsai(bonsai: Bonsai, userId: UUID)

    fun editBonsai(bonsai: Bonsai)

    fun deleteBonsai(bonsaiId: UUID)
}
