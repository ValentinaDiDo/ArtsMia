package it.polito.tdp.artsmia.db;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	//public List<ArtObject> listObjects() {
		public void  listObjects(Map<Integer, ArtObject> map) {	
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		//Map<Integer, ArtObject> map = new HashMap<Integer,ArtObject>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!map.containsKey(res.getInt("object_id"))) {
				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
				map.put(artObj.getId(), artObj);
				//La mappa viene modificata automaticamente perché è un oggetto linkato a quello della classe model, quindi 
				// non c'è bisogno di creare una nuova mappa e ritornarla. Basta solo aggiungere i nuovi elementi
				}
			}
			conn.close();
			//return map;
			
		} catch (SQLException e) {
			e.printStackTrace();
		//	return null;
		}
	}

		public int getPeso(ArtObject a1, ArtObject a2) {
		/* PER VEDERE TUTTE LE COPPIE DI OGGETTI CHE SONO STATE ESPOSTE INSIEME, DEVO FARE UN JOIN DELLA TABELLA DI ESPOSIZIONI 
		 CON SE STESSA -> IN QUESTO MODO MI MOSTRA TUTTE LE COPPIE
		
		FACCIO JOIN E METTO COME CONDIZIONE CHE L'ID DELLA MOSTRA SIA LO STESSO, QUESTO PERCHE' VOGLIO CERCARE SE GLI OGGETTI
		SONO STATI ESIBITI NELLA STESSA MOSTRA
		POI IMPOSTO IN UNA TABELLA L'ID DELL'OGGETTO 1 E NELL'ALTRA TABELLA L'ID DELL'OGGETTO 2
		
		IN QUESTO MODO AVRO' LE ESPOSIZIONI IN CUI SONO STATI ESIBITI TUTTI E DUE GLI OGGETTI
		*/
			
			String sql = "SELECT count(*) as peso "
					+ "FROM exhibitions_objects e1, exhibitions_objects e2 "
					+ "WHERE e1.`exhibition_id` = e2.`exhibition_id` AND e1.`object_id` = ? AND e2.`object_id` = ?";
			
			Connection conn = DBConnect.getConnection();
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, a1.getId());
				st.setInt(2, a2.getId());
				ResultSet res = st.executeQuery();
				
				int peso = 0;
				
				//non ho il while perché la queery ritorna un solo valore 
				if(res.next()) {
					peso = res.getInt("peso");
				}
				st.close();
				res.close();
				conn.close();
				return peso;
				//return map;
				
			} catch (SQLException e) {
				e.printStackTrace();
			//	return null;
			}
			return 0;
		}
	
		
		public List<Adiacenza> getAdiacenze() {
			
			String sql = "SELECT e1.object_id as id1, e2.`object_id` as id2, count(*) as peso "
					+ "FROM exhibition_objects e1, exhibition_objects e2 "
					+ "WHERE e1.`exhibition_id` = e2.`exhibition_id` AND e1.`object_id` > e2.`object_id n"
					+ "GROUP BY e2.`object_id`, e1.`object_id` ";
			//il maggiore è NECESSARIO per evitare coppie duplicate!!!
			
			Connection conn = DBConnect.getConnection();
			List<Adiacenza> result = new ArrayList<Adiacenza>();
			
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				
				while(res.next()) {
					result.add(new Adiacenza(res.getInt("id1"),res.getInt("id2"), res.getInt("peso")));
				}
				
				st.close();
				res.close();
				conn.close();
				return result;
				//return map;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		
			
			
			
		}
}
