package com.nhvu95.hostingimage.utilities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ImageIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		Connection connection = session.connection();

		try {
			String generatedId = "";
			int id = 0;

			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT nextval('image_id_seq')");

			if (rs.next()) {
				id = rs.getInt(1);
				generatedId = String.format("%09d", id);
			}
			statement.close();

			PreparedStatement nextStatement = connection.prepareStatement("SELECT setval('image_id_seq', ?)");
			nextStatement.setInt(1, id + 1);
			nextStatement.execute();
			nextStatement.close();

			generatedId = Base58.encode(generatedId.getBytes());
			return generatedId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
