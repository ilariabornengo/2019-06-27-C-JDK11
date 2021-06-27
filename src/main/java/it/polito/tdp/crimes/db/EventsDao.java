package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getCategorie(){
		String sql = "SELECT DISTINCT e.offense_category_id as id "
				+ "FROM `events` e "
				+ "ORDER BY e.offense_category_id " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Date> getDateOK(){
		String sql = "SELECT DISTINCT date(e.reported_date) AS data "
				+ "FROM `events` e "
				+ "ORDER BY date(e.reported_date) "
				+ " " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Date> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getDate("data"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public void getVertici(List<String> vertici, Date data,String categoria){
		String sql = "SELECT distinct e.offense_type_id as id "
				+ "FROM `events` e "
				+ "WHERE DATE(e.reported_date)=? "
				+ "AND e.offense_category_id=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDate(1, data);
			st.setString(2, categoria);
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!vertici.contains(res.getString("id")))
				{
					vertici.add(res.getString("id"));
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public List<Adiacenza> getAdiacenze(List<String> vertici, Date data,String categoria){
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(e1.precinct_id) AS peso "
				+ "FROM `events` e1, `events` e2 "
				+ "WHERE e1.offense_type_id> e2.offense_type_id "
				+ "AND e1.precinct_id=e2.precinct_id "
				+ "AND e1.offense_category_id=e2.offense_category_id "
				+ "AND e1.offense_category_id=? "
				+ "AND DATE(e1.reported_date)=DATE(e2.reported_date) "
				+ "AND DATE(e1.reported_date)=? "
				+ "GROUP BY e1.offense_type_id,e2.offense_type_id "
				+ "HAVING peso!=0 " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setDate(2, data);
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(vertici.contains(res.getString("id1")) && vertici.contains(res.getString("id2")))
				{
					Adiacenza a=new Adiacenza(res.getString("id1"),res.getString("id2"),res.getInt("peso"));
					list.add(a);
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
}
