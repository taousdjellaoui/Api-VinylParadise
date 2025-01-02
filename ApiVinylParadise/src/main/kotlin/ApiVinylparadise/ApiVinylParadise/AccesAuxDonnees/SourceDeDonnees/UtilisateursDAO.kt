package ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.SourceDeDonnees

import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.DAO
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur

interface UtilisateurDAO : DAO<Utilisateur> {
    override fun chercherTous(): List<Utilisateur>
    fun chercherParEmail(email: String): Utilisateur?
    override fun ajouter(utilisateur: Utilisateur): Utilisateur?
    fun modifierParEmail(email: String, utilisateur: Utilisateur): Utilisateur?
    fun effacer(email: String)
 
}


