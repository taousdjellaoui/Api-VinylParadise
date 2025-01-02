package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.*
import ApiVinylparadise.ApiVinylParadise.Domaine.Artist
import org.springframework.jdbc.core.RowMapper
import org.springframework.dao.EmptyResultDataAccessException
import java.sql.ResultSet

@Repository
class ArtistesDAOImpl(private val jdbcTemplate: JdbcTemplate) : ArtistesDAO {

   
    override fun chercherTous(): List<Artist> =
        jdbcTemplate.query("SELECT id, nom FROM artiste") { réponse, _ ->
            Artist(
                id = réponse.getInt("id"),
                nom = réponse.getString("nom")
            )
        }


     fun chercherParId(id: Int): Artist? =
        try {
            jdbcTemplate.queryForObject("SELECT id, nom FROM artiste WHERE id = ?", { réponse, _ ->
                Artist(
                    id = réponse.getInt("id"),
                    nom = réponse.getString("nom")
                )
            }, id)
        } catch (e: EmptyResultDataAccessException) {
            null 
        }

    fun chercherArtistParNom(nom: String): List<Artist> =
    jdbcTemplate.query("SELECT id, nom FROM artiste WHERE nom LIKE ?", { réponse, _ ->
        Artist(
            id = réponse.getInt("id"),
            nom = réponse.getString("nom")
        )
    }, "%$nom%")

    override fun ajouter(artist: Artist): Artist? {
        val rowsAffected = jdbcTemplate.update("INSERT INTO artiste (nom) VALUES (?)", artist.nom)

        return if (rowsAffected > 0) {
            val generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
            artist.copy(id = generatedId ?: 0)
        } else {
            null
        }
    }


    override fun modifier(id: Int, artist: Artist): Artist? {
        val rowsAffected = jdbcTemplate.update("UPDATE artiste SET nom = ? WHERE id = ?", artist.nom, id)

        return if (rowsAffected > 0) {
            artist.copy(id = id)
        } else {
            null
        }
    }

   
    override fun effacer(id: Int) {
        jdbcTemplate.update("DELETE FROM artiste WHERE id = ?", id)
    }
}