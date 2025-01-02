package ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.BD

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.SourceDeDonnees.UtilisateurDAO
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.DonnéeExistanteException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.DuplicateKeyException

@Repository
class UtilisateurDAOImpl(private val jdbcTemplate: JdbcTemplate) : UtilisateurDAO {

    override fun chercherTous(): List<Utilisateur> =
        jdbcTemplate.query("SELECT nom, prénom, email FROM utilisateur") { rs, _ ->
            Utilisateur(
                nom = rs.getString("nom"),
                prenom = rs.getString("prénom"),
                email = rs.getString("email")
            )
        }

    override fun ajouter(utilisateur: Utilisateur): Utilisateur? {
        return try {
            val rowsAffected = jdbcTemplate.update(
                "INSERT INTO utilisateur (nom, prénom, email) VALUES (?, ?, ?)",
                utilisateur.nom, utilisateur.prenom, utilisateur.email
            )
            if (rowsAffected > 0) utilisateur else null
        } catch (e: DuplicateKeyException) {
            // Lancer une exception avec un code 400 si l'email existe déjà
            throw DonnéeExistanteException( "Un utilisateur avec cet email existe déjà.")
        }
    }

   

    override fun modifierParEmail(email: String, utilisateur: Utilisateur): Utilisateur? {
        val rowsAffected = jdbcTemplate.update(
            "UPDATE utilisateur SET nom = ?, prénom = ? WHERE email = ?",
            utilisateur.nom, utilisateur.prenom, email
        )
        return if (rowsAffected > 0) utilisateur else null
    }

    

   override fun effacer(email: String){
         jdbcTemplate.update("DELETE FROM utilisateur WHERE email = ?", email) > 0
    }

    fun chercherUtilisateurParNom(nom: String): List<Utilisateur> =
        jdbcTemplate.query(
            "SELECT nom, prénom, email FROM utilisateur WHERE nom LIKE ?",
            { rs, _ ->
                Utilisateur(
                    nom = rs.getString("nom"),
                    prenom = rs.getString("prénom"),
                    email = rs.getString("email")
                )
            },
            "%$nom%"
        )

    fun chercherUtilisateurParPrenom(prenom: String): List<Utilisateur> =
        jdbcTemplate.query(
            "SELECT nom, prénom, email FROM utilisateur WHERE prénom LIKE ?",
            { rs, _ ->
                Utilisateur(
                    nom = rs.getString("nom"),
                    prenom = rs.getString("prénom"),
                    email = rs.getString("email")
                )
            },
            "%$prenom%"
        )

   override fun chercherParEmail(email: String): Utilisateur? =
        try {
            jdbcTemplate.queryForObject(
                "SELECT nom, prénom, email FROM utilisateur WHERE email = ?",
                { rs, _ ->
                    Utilisateur(
                        nom = rs.getString("nom"),
                        prenom = rs.getString("prénom"),
                        email = rs.getString("email")
                    )
                },
                email
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }

   override fun effacer(id: Int) { }

   override fun modifier(id: Int, element: Utilisateur): Utilisateur? {
    throw UnsupportedOperationException("La modification via ID n'est pas supportée pour Utilisateur.")
}

   

  

   
}
