package com.CSE310.Stock_Portefolio_Tracker.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;

import java.util.List;


@Repository
public interface PortefolioRepository extends JpaRepository<Portefolio,Long>{


      // Lister tous les portefeuilles d'un utilisateur
        List<Portefolio> findByUser(Userx user);

     

}
