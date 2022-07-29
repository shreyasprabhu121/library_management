package com.poc.springoracleCrud.config;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class BookIDGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {

		String prefix = "BOOK";
		Connection connection = session.connection();

		try (Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select count(book_id) as Id from books")) {

			if (rs.next()) {
				int id = rs.getInt(1) + 101;
				if (rs.getInt(1) == 0)
					return prefix + id;
				else {
					try (Statement statement2 = connection.createStatement();
							ResultSet rs2 = statement2.executeQuery(
									"SELECT * FROM (SELECT * FROM books ORDER BY book_id DESC) WHERE ROWNUM = 1")) {
						if (rs2.next()) {
							String lastBookID = rs2.getString(1);
							return prefix + (Integer.valueOf(lastBookID.split("BOOK")[1]) + 1);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
}
