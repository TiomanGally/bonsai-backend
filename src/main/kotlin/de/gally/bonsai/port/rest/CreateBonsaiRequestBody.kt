package de.gally.bonsai.port.rest

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

data class CreateBonsaiRequestBody(
    @NotNull @NotEmpty val latinName: String,
    val simpleName: String?,
    val birthDate: LocalDate,
    @Min(0) val price: Double,
    val lastRepoted: LocalDate,
)