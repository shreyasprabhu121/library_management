package com.poc.springoracleCrud.config;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UserIDGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {

		String prefix = "USER";
		Connection connection = session.connection();

		try (Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select count(user_id) as Id from users")) {

			if (rs.next()) {
				int id = rs.getInt(1) + 101;
				if (rs.getInt(1) == 0)
					return prefix + id;
				else {
					try (Statement statement2 = connection.createStatement();
							ResultSet rs2 = statement2.executeQuery(
									"SELECT * FROM (SELECT * FROM users ORDER BY user_id DESC) WHERE ROWNUM = 1")) {
						if (rs2.next()) {
							String lastuserID = rs2.getString(1);
							return prefix + (Integer.valueOf(lastuserID.split("USER")[1]) + 1);
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
