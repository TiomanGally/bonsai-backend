package de.gally.bonsai.adapter

import de.gally.bonsai.domain.Bonsai
import de.gally.bonsai.domain.usecases.BonsaiManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class BonsaiManagerService(
    private val jdbcTemplate: JdbcTemplate
) : BonsaiManager {

    private val rowMapper = RowMapper<Bonsai> { rs: ResultSet, _ ->
        Bonsai(
            UUID.fromString(rs.getString("uuid")),
            rs.getString("latin_name"),
            rs.getString("simple_name"),
            rs.getDate("birth_date").toLocalDate(),
            rs.getDouble("price"),
            rs.getDate("last_repoted").toLocalDate(),
        )
    }

    override fun addBonsai(bonsai: Bonsai, userId: UUID) {
        val sql = "INSERT INTO bonsais (uuid, latin_name, simple_name, birth_date, price, last_repoted, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)"
        jdbcTemplate.update(sql, bonsai.uuid, bonsai.latinName, bonsai.simpleName, bonsai.birthDate, bonsai.price, bonsai.lastRepoted, userId)
    }

    override fun deleteBonsai(bonsaiId: UUID) {
        val sql = "DELETE FROM bonsais WHERE uuid = ?"
        jdbcTemplate.update(sql, bonsaiId)
    }

    override fun editBonsai(bonsai: Bonsai) {
        val sql = "UPDATE bonsais SET latin_name = ?, simple_name = ?, birth_date = ?, last_repoted = ? WHERE uuid = ?"
        jdbcTemplate.update(sql, bonsai.latinName, bonsai.simpleName, bonsai.birthDate, bonsai.lastRepoted, bonsai.uuid)
    }

    override fun getBonsai(bonsaiId: UUID): Bonsai? {
        val sql = "SELECT * FROM bonsais WHERE uuid = ?"
        return jdbcTemplate.query(sql, rowMapper, bonsaiId).firstOrNull()
    }

    override fun getAllBonsais(userId: UUID): List<Bonsai> {
        val sql = "SELECT * FROM bonsais where user_id = ?"
        return jdbcTemplate.query(sql, rowMapper, userId)
    }
}