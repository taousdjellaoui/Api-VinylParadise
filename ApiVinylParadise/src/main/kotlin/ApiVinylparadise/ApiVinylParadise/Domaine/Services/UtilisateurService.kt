package ApiVinylparadise.ApiVinylParadise.Domaine.Services

import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.SourceDeDonnees.UtilisateurDAO
import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.BD.UtilisateurDAOImpl
import org.springframework.stereotype.Service

@Service
class UtilisateurService(private val utilisateurDAO: UtilisateurDAOImpl) {

    
    fun obtenirUtilisateurs(): List<Utilisateur> {
        return utilisateurDAO.chercherTous()
    }

   
    fun chercherUtilisateurParNom(nom: String): List<Utilisateur> {
        return utilisateurDAO.chercherUtilisateurParNom(nom)
    }

    
    fun chercherUtilisateurParPrenom(prenom: String): List<Utilisateur> {
        return utilisateurDAO.chercherUtilisateurParPrenom(prenom)
    }

    
    fun chercherUtilisateurParEmail(email: String): Utilisateur? {
        return utilisateurDAO.chercherParEmail(email)
    }

   
    fun creerUtilisateur(utilisateur: Utilisateur): Utilisateur? {
        return utilisateurDAO.ajouter(utilisateur)
    }

    
    fun modifierUtilisateurParEmail(email: String, utilisateur: Utilisateur): Utilisateur? {
        return utilisateurDAO.modifierParEmail(email, utilisateur)
    }

   
    fun effacerUtilisateurParEmail(email: String) {
        utilisateurDAO.effacer(email)
    }
}
