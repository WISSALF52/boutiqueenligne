
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Produit {
    private String nom;
    private double prix;
    private int stock;
    private String categorie;

    public Produit(String nom, double prix, int stock, String categorie) {
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public int getStock() {
        return stock;
    }

    public String getCategorie() {
        return categorie;
    }

    public void reduireStock(int quantite) {
        if (stock >= quantite) {
            stock -= quantite;
        } else {
            System.out.println("Stock insuffisant pour " + nom);
        }
    }

    public void augmenterStock(int quantite) {
        stock += quantite;
    }

    @Override
    public String toString() {
        return nom + " - " + prix + "€ (Stock: " + stock + ")";
    }
}

abstract class Utilisateur {
    private String nom;
    private String email;
    private String motDePasse;

    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public boolean verifierMotDePasse(String mdp) {
        return motDePasse.equals(mdp);
    }

    public abstract void afficherRole();
}

class Client extends Utilisateur {
    public Client(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
    }

    @Override
    public void afficherRole() {
        System.out.println("Role: Client");
    }
}

class Administrateur extends Utilisateur {
    public Administrateur(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
    }

    @Override
    public void afficherRole() {
        System.out.println("Role: Administrateur");
    }

    public void ajouterProduit(Map<String, Produit> produits, String nom, double prix, int stock, String categorie) {
        produits.put(nom, new Produit(nom, prix, stock, categorie));
    }

    public void supprimerProduit(Map<String, Produit> produits, String nom) {
        produits.remove(nom);
    }
}

class Commande {
    private static int idCounter = 1;
    private int id;
    private Client client;
    private List<Produit> produits;
    private double total;
    private String statut;

    public Commande(Client client) {
        this.id = idCounter++;
        this.client = client;
        this.produits = new ArrayList<>();
        this.total = 0.0;
        this.statut = "En attente";
    }

    public void ajouterProduit(Produit produit, int quantite) {
        if (produit.getStock() >= quantite) {
            produits.add(produit);
            total += produit.getPrix() * quantite;
            produit.reduireStock(quantite);
        } else {
            System.out.println("Stock insuffisant pour " + produit.getNom());
        }
    }

    public void afficherCommande() {
        System.out.println("Commande ID: " + id);
        System.out.println("Client: " + client.getNom());
        System.out.println("Statut: " + statut);
        System.out.println("Produits commandés:");
        for (Produit produit : produits) {
            System.out.println("- " + produit.getNom() + ": " + produit.getPrix() + "€");
        }
        System.out.println("Total: " + total + "€");
    }

    public void changerStatut(String nouveauStatut) {
        this.statut = nouveauStatut;
    }

    public int getId() {
        return id;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public double getTotal() {
        return total;
    }
}

public class BoutiqueEnLigne {
    private static Map<String, Utilisateur> utilisateurs = new HashMap<>();
    private static Map<String, Produit> produits = new HashMap<>();
    private static Map<Integer, Commande> commandes = new HashMap<>();

    public static void main(String[] args) {
        initialiserProduits();

        initialiserUtilisateurs();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre email: ");
        String email = scanner.nextLine();
        System.out.print("Entrez votre mot de passe: ");
        String motDePasse = scanner.nextLine();

        Utilisateur utilisateur = utilisateurs.get(email);
        if (utilisateur != null && utilisateur.verifierMotDePasse(motDePasse)) {
            utilisateur.afficherRole();
            if (utilisateur instanceof Client) {
                traiterCommande((Client) utilisateur);
            } else if (utilisateur instanceof Administrateur) {
                gererProduits((Administrateur) utilisateur);
            }
        } else {
            System.out.println("Authentification échouée.");
        }
    }

    public static void initialiserProduits() {
        produits.put("T-shirt", new Produit("T-shirt", 15.0, 100, "Vêtements"));
        produits.put("Jeans", new Produit("Jeans", 40.0, 50, "Vêtements"));
        produits.put("Sac à dos", new Produit("Sac à dos", 25.0, 30, "Accessoires"));
    }

    public static void initialiserUtilisateurs() {
        utilisateurs.put("ikram@example.com", new Client("Ikram Eljazouli", "ikram@example.com", "1234"));
        utilisateurs.put("admin@example.com", new Administrateur("Admin", "admin@example.com", "admin123"));
    }

    public static void traiterCommande(Client client) {
        Scanner scanner = new Scanner(System.in);
        Commande commande = new Commande(client);
        boolean continuer = true;

        while (continuer) {
            System.out.println("\nProduits disponibles:");
            produits.forEach((nom, produit) -> System.out.println(produit));
            System.out.print("Entrez le nom du produit que vous souhaitez ajouter (ou 'fin' pour terminer): ");
            String nomProduit = scanner.nextLine();
            if (nomProduit.equals("fin")) {
                continuer = false;
            } else {
                Produit produit = produits.get(nomProduit);
                if (produit != null) {
                    System.out.print("Entrez la quantité: ");
                    int quantite = scanner.nextInt();
                    scanner.nextLine();
                    commande.ajouterProduit(produit, quantite);
                } else {
                    System.out.println("Produit non trouvé.");
                }
            }
        }

        commande.afficherCommande();
        commandes.put(commande.getId(), commande);
    }

    public static void gererProduits(Administrateur administrateur) {
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("\nOptions administrateur:");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Supprimer un produit");
            System.out.println("3. Quitter");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nom du produit: ");
                    String nom = scanner.nextLine();
                    System.out.print("Prix: ");
                    double prix = scanner.nextDouble();
                    System.out.print("Stock: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Catégorie: ");
                    String categorie = scanner.nextLine();
                    administrateur.ajouterProduit(produits, nom, prix, stock, categorie);
                    break;
                case 2:
                    System.out.print("Nom du produit à supprimer: ");
                    String nomProduitASupprimer = scanner.nextLine();
                    administrateur.supprimerProduit(produits, nomProduitASupprimer);
                    break;
                case 3:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }
}
