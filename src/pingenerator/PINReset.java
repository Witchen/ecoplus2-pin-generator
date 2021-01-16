package pingenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PINReset {

	private List<PIN> pinList = new ArrayList<>();
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public static void main(String[] args) {
		PINReset pinReset = new PINReset();
		pinReset.openDBConnection();
		pinReset.getCurrentPIN();
		pinReset.resetPin();
		pinReset.updatePinToDB();
	}

	private void openDBConnection() {
		String DB_URL = "jdbc:mysql://103.229.72.55/k5413684_ecoplus?user=k5413684_reader&password=Readerpass312";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getCurrentPIN() {
		try {
			String query = "SELECT * FROM data_id WHERE status = 0";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				PIN pin = new PIN(resultSet.getString("id"), resultSet.getString("pin"),
						Integer.parseInt(resultSet.getString("status")));
				pinList.add(pin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resetPin() {
		for (PIN pin : pinList) {
			String pinStr = "";

			for (int b = 0; b < 6; b++) {
				if (b == 0 || b == 1 || b == 3 || b == 4) {
					int random = (int) (Math.random() * 26);
					char character = (char) (random + 65);
					pinStr = pinStr + character;
				} else {
					int random = (int) (Math.random() * 10);
					char num = (char) (random + 48);
					pinStr = pinStr + num;
				}
			}

			System.out.println(pin.toString() + " changed PIN to: " + pinStr);

			pin.setPin(pinStr);
			pinStr = "";
		}

		System.out.println(pinList.size() + " pin reset.");
	}

	private void updatePinToDB() {
		PreparedStatement preparedStatement = null;
		try {
			String query = "UPDATE `data_id` SET `pin` = ? WHERE `data_id`.`id` = ?";
			preparedStatement = connect.prepareStatement(query);

			for (PIN pin : pinList) {
				preparedStatement.setString(1, pin.getPin());
				preparedStatement.setString(2, pin.getId());
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
